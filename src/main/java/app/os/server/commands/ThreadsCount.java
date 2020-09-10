package app.os.server.commands;

import app.os.server.ServerCommand;
import app.os.server.ServerExecutor;
import org.json.JSONObject;

public class ThreadsCount extends ServerCommand {
    public ThreadsCount() {
        super("threads", "get all active threads");
    }

    @Override
    public JSONObject execute(ServerExecutor executor) {
        JSONObject answer = new JSONObject();
        answer.put("activeThreads_count", Thread.activeCount());

        return answer;
    }
}
