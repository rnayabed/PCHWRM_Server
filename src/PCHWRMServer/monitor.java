package PCHWRMServer;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class monitor extends monitorUI {
    io io;

    HashMap<String,String> config = new HashMap<>();

    public monitor()
    {
        io = new io();

        readConfig();

        setPrefSize(Integer.parseInt(config.get("SCREEN_WIDTH")), Integer.parseInt(config.get("SCREEN_HEIGHT")));
        loadNodes();

        new Thread(new Task<Void>() {
            @Override
            protected Void call()
            {
                try {
                    System.out.println("C");
                    startServer();
                }
                catch (Exception e)
                {
                    isConnected = false;
                    Platform.runLater(()->switchPane(pane.notConnected));
                    e.printStackTrace();
                }
                return null;
            }
        }).start();
    }

    Socket socket;
    ServerSocket serverSocket;

    BufferedReader is;

    public void startServer() throws Exception
    {

        io.pln("Starting Server ...");
        if(serverSocket!=null)
        {
            if(!serverSocket.isClosed())
            {
                serverSocket.close();
            }
        }

        serverSocket = new ServerSocket(Integer.parseInt(config.get("SERVER_PORT")), 0, InetAddress.getByName(io.getShellOutput("hostname -I").replace("\n","").replace(" ","")));
        io.pln("... Done!\n" +
                "Listening for Clients on "+serverSocket.getInetAddress().getHostAddress());

        notConnectedPaneSubHeadingLabel.setText("Server running on "+serverSocket.getInetAddress().getHostAddress()+", Port "+config.get("SERVER_PORT"));
        socket = serverSocket.accept();
        io.pln("... Connected!");

        is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        isConnected = true;
        Platform.runLater(()->switchPane(pane.gauges));

        new Thread(new Task<Void>() {
            @Override
            protected Void call()
            {
                try {
                    while(isConnected)
                    {
                        String xx = readFromIS();
                        if(xx.equals("")) continue;

                        System.out.println("Message : "+xx);

                        String[] msg = xx.split("!!");
                        String msgHeader = msg[0];
                        switch (msgHeader) {
                            case "QUIT" -> {
                                io.pln("Request to quit!");
                                isConnected = false;
                                socket.close();
                                Platform.runLater(() -> switchPane(pane.notConnected));
                                System.out.println("A");
                                startServer();
                            }
                            case "PC_DATA" -> dataReceived(msg[1]);
                            case "CPU_GPU_MODELS" -> {
                                String[] cc = msg[1].split("::");
                                cpuModel = cc[0];
                                gpuModel = cc[1];
                                Platform.runLater(() -> {
                                    CPUModelNameLabel.setText(cpuModel);
                                    GPUModelNameLabel.setText(gpuModel);
                                });
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    isConnected = false;
                    Platform.runLater(()->switchPane(pane.notConnected));
                    try {
                        System.out.println("B");
                        startServer();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
                return null;
            }
        }).start();
    }

    String cpuModel, gpuModel;

    public String readFromIS()
    {
        try {
            return is.readLine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    double totalVRAM=-1, usedVRAM=-1;
    double usedRAM=-1, freeRAM=-1;
    double cpuFANSpeedMaxValue=0, gpuFANSpeedMaxValue=0;

    void dataReceived(String dataFromClient)
    {
        for(String eachChunk : dataFromClient.split("::"))
        {
            String[] rawData = eachChunk.split("<>");
            String dataType = rawData[0];
            switch (dataType) {
                case "GPU_LOAD" -> Platform.runLater(() -> GPULoadGauge.setValue(Double.parseDouble(rawData[1])));
                case "CPU_LOAD" -> Platform.runLater(() -> CPULoadGauge.setValue(Double.parseDouble(rawData[1])));
                case "CPU_TEMP" -> Platform.runLater(() -> CPUTempGauge.setValue(Double.parseDouble(rawData[1])));
                case "GPU_TEMP" -> Platform.runLater(() -> GPUTempGauge.setValue(Double.parseDouble(rawData[1])));
                case "TOTAL_VRAM" -> {
                    if(totalVRAM==-1) totalVRAM = Math.ceil(Double.parseDouble(rawData[1]));
                }
                case "USED_VRAM" -> usedVRAM = Double.parseDouble(rawData[1]);
                case "FREE_RAM" -> freeRAM = Double.parseDouble(rawData[1]);
                case "USED_RAM" -> usedRAM = Double.parseDouble(rawData[1]);
                case "CPU_FAN" -> {
                    int cpuFANSpeed = (int) Double.parseDouble(rawData[1]);
                    Platform.runLater(() -> CPUFanSpeedGauge.setValue(cpuFANSpeed));
                    if (cpuFANSpeed > cpuFANSpeedMaxValue) {
                        Platform.runLater(() -> CPUFanSpeedGauge.setMaxValue(cpuFANSpeed));
                        cpuFANSpeedMaxValue = cpuFANSpeed;
                    }
                }
                case "GPU_FAN" -> {
                    int gpuFANSpeed = (int) Double.parseDouble(rawData[1]);
                    Platform.runLater(() -> GPUFanSpeedGauge.setValue(gpuFANSpeed));
                    if (gpuFANSpeed > gpuFANSpeedMaxValue) {
                        Platform.runLater(() -> GPUFanSpeedGauge.setMaxValue(gpuFANSpeed));
                        gpuFANSpeedMaxValue = gpuFANSpeed;
                    }
                }
            }
        }

        if(totalVRAM>-1 || usedVRAM>-1)
        {
            Platform.runLater(()-> {
                videoMemoryGauge.setValue((usedVRAM/totalVRAM)*100);
                videoMemorySubHeadingLabel.setText(((int)(usedVRAM/1024))+"GB / "+((int)(totalVRAM/1024))+"GB");
            });
        }

        if(freeRAM>-1 || usedRAM>-1)
        {
            Platform.runLater(()-> {
                memoryGauge.setValue((usedRAM/(usedRAM+freeRAM))*100);
                memorySubHeadingLabel.setText(((int)usedRAM)+"GB / "+((int)(usedRAM+freeRAM))+"GB");
            });
        }
    }

    boolean isConnected = false;

    public void readConfig()
    {
        try
        {
            String[] configArray = io.readFileArranged("config","::");

            config.clear();
            config.put("SCREEN_WIDTH",configArray[0]);
            config.put("SCREEN_HEIGHT",configArray[1]);
            config.put("SERVER_PORT",configArray[2]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
