package app.os.discord.commands.users;

import app.os.discord.DiscordBot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Link extends Command {

    public Link() {
        this.name = "link";
        this.help = "get link to invite bot";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage(String.format("**Bot invite URL:** %s.", DiscordBot.INVITE_URL)).queue();
    }
}
