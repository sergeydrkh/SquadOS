package app.os.server.commands;

import app.os.server.ServerCommand;
import app.os.server.ServerExecutor;

public class ThreadsCount extends ServerCommand {
    public ThreadsCount() {
        super("threads", "get all active threads");
    }

    @Override
    public String execute(ServerExecutor executor) {
        return "Active threads: " + Thread.activeCount();
    }
}
