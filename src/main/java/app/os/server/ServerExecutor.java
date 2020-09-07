package app.os.server;

import net.dv8tion.jda.api.JDA;

public class ServerExecutor {
    private final JDA api;
    private final String[] args;

    public ServerExecutor(JDA api, String[] args) {
        this.api = api;
        this.args = args;
    }

    public JDA getApi() {
        return api;
    }

    public String[] getArgs() {
        return args;
    }
}
