package app.os.server;

import app.os.utilities.ConsoleHelper;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private BufferedReader in;
    private BufferedWriter out;

    private final String ip;
    private final int port;

    public Server(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            try {
                serverSocket = new ServerSocket(port, 0, InetAddress.getByName(ip));
                ConsoleHelper.println("Server started");

                try (Socket clientSocket = serverSocket.accept()) {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    String message = in.readLine();
                    ConsoleHelper.println("new message from server: " + message);


                    out.write(message + "\n");
                    out.flush();
                } finally {
                    in.close();
                    out.close();
                }
            } finally {
                ConsoleHelper.println("Server stopped");
                serverSocket.close();
            }
        } catch (Exception e) {
            ConsoleHelper.errln(e.getMessage());
        }
    }
}
