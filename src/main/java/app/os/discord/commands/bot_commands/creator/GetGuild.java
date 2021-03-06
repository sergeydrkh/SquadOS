package app.os.discord.commands.bot_commands.creator;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;

public class GetGuild extends Command {
    public GetGuild() {
        this.name = "guildInfo";
        this.help = "получить информацию о Guild";
        this.requiredRole = DiscordBot.CREATOR_ROLE;
        this.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage(commandEvent.getGuild().toString()).queue();
    }
}
