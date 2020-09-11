package app.os.server;

import app.os.utilities.ConsoleHelper;
import net.dv8tion.jda.api.JDA;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TaskExecutor implements Runnable {
    private final Socket clientDialog;
    private final JDA api;

    public TaskExecutor(Socket clientDialog, JDA api) {
        this.clientDialog = clientDialog;
        this.api = api;
    }

    @Override
    public void run() {
        try (DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
             DataInputStream in = new DataInputStream(clientDialog.getInputStream())) {

            while (!clientDialog.isClosed()) {
                String entry = in.readUTF();

                if (entry.equalsIgnoreCase("quit")) {
                    out.writeUTF("Server reply - " + entry + " - OK");
                    Thread.sleep(3000);

                    break;
                }


                if (entry.equalsIgnoreCase("ping")) {
                    out.writeUTF(String.format("gateway=%d%n", api.getGatewayPing()));
                } else if (entry.equalsIgnoreCase("state")) {
                    out.writeUTF(String.format("state=%s%n errors=%d", "normal", ConsoleHelper.getErrors()));
                } else if (entry.equalsIgnoreCase("threads")) {
                    out.writeUTF(String.format("threadsCount=%d", Thread.activeCount()));
                } else {
                    out.writeUTF(String.format("error=%s%n", "unknown command"));
                }

                out.flush();
            }

            clientDialog.close();
        } catch (Exception e) {
            ConsoleHelper.errln(e.getMessage());
        }
    }
}