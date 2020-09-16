package app.os.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleListener extends Thread {
    public ConsoleListener() {
        setDaemon(true);
        setName("ConsoleReader Thread");
    }

    @Override
    public void run() {
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
                String input = consoleReader.readLine();

                if (input.equals("stop")) {
                    ConsoleHelper.println("STOPPED");
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            ConsoleHelper.errln(e.getMessage());
        }
    }
}
