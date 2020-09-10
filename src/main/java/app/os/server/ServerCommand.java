package app.os.server;

import org.json.JSONObject;

public abstract class ServerCommand {
    private final String name;
    private final String help;

    public ServerCommand(String name, String help) {
        this.name = name;
        this.help = help;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", help='" + help + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public abstract JSONObject execute(ServerExecutor executor);
}
