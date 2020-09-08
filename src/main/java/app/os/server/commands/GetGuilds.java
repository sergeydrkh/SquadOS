package app.os.server.commands;

import app.os.server.ServerCommand;
import app.os.server.ServerExecutor;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public class GetGuilds extends ServerCommand {
    public GetGuilds() {
        super("guilds", "get all guilds");
    }

    @Override
    public String execute(ServerExecutor executor) {
        List<Guild> allGuilds = executor.getApi().getGuilds();
        StringBuilder answer = new StringBuilder();

        answer.append("All guilds: \n");
        System.out.println(allGuilds);
        allGuilds.forEach(guild -> answer.append(guild.getName()).append("\n"));

        return answer.toString();
    }
}
