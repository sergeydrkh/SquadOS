package app.os.server.commands;

import app.os.server.ServerCommand;
import app.os.server.ServerExecutor;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetGuilds extends ServerCommand {
    public GetGuilds() {
        super("guilds", "get all guilds");
    }

    @Override
    public JSONObject execute(ServerExecutor executor) {
        JSONObject answer = new JSONObject();

        JSONArray guilds = new JSONArray();
        executor.getApi().getGuilds().forEach(guild -> guilds.put(guild.getName()));

        answer.put("guilds", guilds);

        return answer;
    }
}
