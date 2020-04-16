package PCHWRMServer;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class monitor extends monitorUI {
    io io;

    HashMap<String,String> config = new HashMap<>();

    server server;

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
                    try {
                        System.out.println("D");
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

    Socket socket;
    ServerSocket serverSocket;

    DataInputStream is;

    public void startServer() throws Exception
    {
        io.pln("Starting Server ...");
        if(serverSocket!=null)
        {
            if(!serverSocket.isClosed()) serverSocket.close();
        }

        serverSocket = new ServerSocket(Integer.parseInt(config.get("SERVER_PORT")), 0, InetAddress.getByName(io.getShellOutput("hostname -I").replace("\n","").replace(" ","")));
        serverSocket.setReuseAddress(true);
        io.pln("... Done!\n" +
                "Listening for Clients on "+serverSocket.getInetAddress().getHostAddress());

        notConnectedPaneSubHeadingLabel.setText("Server running on "+serverSocket.getInetAddress().getHostAddress()+", Port "+config.get("SERVER_PORT"));
        socket = serverSocket.accept();
        io.pln("... Connected!");

        is = new DataInputStream(socket.getInputStream());

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
                        if(xx ==null)
                        {
                            isConnected = false;
                            server.close();
                        }

                        System.out.println("Message : "+xx);

                        if(xx!=null)
                        {
                            String[] msg = xx.split("!!");
                            String msgHeader = msg[0];
                            if(msgHeader.equals("QUIT"))
                            {
                                io.pln("Request to quit!");
                                isConnected = false;
                                socket.close();
                                Platform.runLater(()->switchPane(pane.notConnected));
                                System.out.println("A");
                                startServer();
                                break;
                            }
                            else if(msgHeader.equals("PC_DATA"))
                            {
                                dataReceived(msg[1]);
                            }
                            else if(msgHeader.equals("CPU_GPU_MODELS"))
                            {
                                String[] cc = msg[1].split("::");
                                cpuModel = cc[0];
                                gpuModel = cc[1];
                                Platform.runLater(()->{
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

    int uniByteLen = 0;
    public String readFromIS()
    {
        try {
            String bg = is.readUTF();
            byte[] str = new byte[uniByteLen];
            if(bg.startsWith("buff_length"))
            {
                uniByteLen = Integer.parseInt(bg.split("::")[1]);
                System.out.println("GOT @ "+uniByteLen);
                str = is.readNBytes(uniByteLen);
            }

            if(uniByteLen>0)
            {
                uniByteLen = 0;
            }
            return new String(str);
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
            if(dataType.equals("GPU_LOAD"))
            {
                Platform.runLater(()->GPULoadGauge.setValue(Double.parseDouble(rawData[1])));
            }
            else if(dataType.equals("CPU_LOAD"))
            {
                Platform.runLater(()->CPULoadGauge.setValue(Double.parseDouble(rawData[1])));
            }
            else if(dataType.equals("CPU_TEMP"))
            {
                Platform.runLater(()->CPUTempGauge.setValue(Double.parseDouble(rawData[1])));
            }
            else if(dataType.equals("GPU_TEMP"))
            {
                Platform.runLater(()->GPUTempGauge.setValue(Double.parseDouble(rawData[1])));
            }
            else if(dataType.equals("TOTAL_VRAM"))
            {
                totalVRAM = Double.parseDouble(rawData[1]);
            }
            else if(dataType.equals("USED_VRAM"))
            {
                usedVRAM = Double.parseDouble(rawData[1]);
            }
            else if(dataType.equals("FREE_RAM"))
            {
                freeRAM = Double.parseDouble(rawData[1]);
            }
            else if(dataType.equals("USED_RAM"))
            {
                usedRAM = Double.parseDouble(rawData[1]);
            }
            else if(dataType.equals("CPU_FAN"))
            {
                int cpuFANSpeed = (int) Double.parseDouble(rawData[1]);
                Platform.runLater(()->CPUFanSpeedGauge.setValue(cpuFANSpeed));
                if(cpuFANSpeed>cpuFANSpeedMaxValue)
                {
                    Platform.runLater(()->CPUFanSpeedGauge.setMaxValue(cpuFANSpeed));
                    cpuFANSpeedMaxValue = cpuFANSpeed;
                }
            }
            else if(dataType.equals("GPU_FAN"))
            {
                int gpuFANSpeed = (int) Double.parseDouble(rawData[1]);
                Platform.runLater(()->GPUFanSpeedGauge.setValue(gpuFANSpeed));
                if(gpuFANSpeed>gpuFANSpeedMaxValue)
                {
                    Platform.runLater(()->GPUFanSpeedGauge.setMaxValue(gpuFANSpeed));
                    gpuFANSpeedMaxValue = gpuFANSpeed;
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
