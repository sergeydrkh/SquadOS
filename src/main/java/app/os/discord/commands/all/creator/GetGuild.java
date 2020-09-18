package app.os.discord.commands.all.creator;

import app.os.discord.DiscordBot;
import app.os.discord.commands.tread.command.Command;
import app.os.discord.commands.tread.command.CommandEvent;

public class GetGuild extends Command {
    public GetGuild() {
        this.name = "guildInfo";
        this.help = "получить информацию о Guild";
        this.requiredRole = DiscordBot.CREATOR_ROLE;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage(commandEvent.getGuild().toString()).queue();
    }
}
