package PCHWRMServer;

import javafx.scene.control.Label;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class server {

    Socket socket;
    ServerSocket serverSocket;

    DataInputStream is;

    io io;

    public server(int port, Label notConnectedPaneSubHeadingLabel) throws Exception
    {
        io = new io();


    }

    public void close() throws Exception
    {
        serverSocket.close();
        socket.close();
    }

    public DataInputStream getDataInputStream()
    {
        return is;
    }

}
