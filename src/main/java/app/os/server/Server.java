package app.os.server;

import net.dv8tion.jda.api.JDA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mercenery
 */
public class Server extends Thread {
    static ExecutorService executeIt = Executors.newFixedThreadPool(2);

    private final String ip;
    private final int port;

    private final JDA api;

    public Server(String ip, int port, JDA api) {
        setDaemon(true);
        setName("Server");

        this.ip = ip;
        this.port = port;
        this.api = api;
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port, 0, InetAddress.getByName(ip));
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server socket created, command console reader for listen to server commands");

            while (!server.isClosed()) {
                if (br.ready()) {
                    System.out.println("Main Server found any messages in channel, let's look at them.");
                    String serverCommand = br.readLine();

                    if (serverCommand.equalsIgnoreCase("quit")) {
                        System.out.println("Main Server initiate exiting...");
                        server.close();

                        break;
                    }
                }

                Socket client = server.accept();

                executeIt.execute(new TaskExecutor(client, api));
                System.out.print("Connection accepted.");
            }

            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}