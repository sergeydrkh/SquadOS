package app.os.server;

import app.os.main.OS;
import app.os.server.commands.GetGuilds;
import app.os.server.commands.ThreadsCount;
import app.os.utilities.ConsoleHelper;
import net.dv8tion.jda.api.JDA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private BufferedReader in;
    private BufferedWriter out;

    private final String ip;
    private final int port;

    private final List<ServerCommand> commands = new ArrayList<>();
    private final JDA api;

    public Server(String ip, int port, JDA api) {
        commands.add(new GetGuilds());
        commands.add(new ThreadsCount());

        this.api = api;

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
                    String[] args = message.split(OS.DEFAULT_MESSAGE_DELIMITER);
                    ConsoleHelper.println("new message from server: " + message);

                    StringBuilder answer = new StringBuilder();

                    ServerExecutor executor = new ServerExecutor(api, args);

                    if (args[0].equals("help"))
                        answer.append(getHelp());
                    else
                        commands.forEach(command -> {
                            System.out.println(command.getName());
                            System.out.println(args[0]);
                            if (args[0].equals(command.getName()))
                                answer.append(command.execute(executor)).append("\n");
                        });

                    out.write(answer.toString());
                    out.flush();
                } finally {
                    in.close();
                    out.close();
                }
            } finally {
                ConsoleHelper.println("Server stopped");
                serverSocket.close();
                new Server(ip, port, api).start();
            }
        } catch (Exception e) {
            ConsoleHelper.errln(e.getMessage());
        }
    }

    private String getHelp() {
        StringBuilder helpMessage = new StringBuilder();

        helpMessage.append("All commands: ");
        commands.forEach(command -> helpMessage.append(command.getName()).append(" - ").append(command.getHelp()).append("\n"));

        return helpMessage.toString();
    }
}
