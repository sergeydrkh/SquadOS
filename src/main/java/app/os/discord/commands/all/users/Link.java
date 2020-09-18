package app.os.discord.commands.all.users;

import app.os.discord.DiscordBot;
import app.os.discord.commands.tread.command.Command;
import app.os.discord.commands.tread.command.CommandEvent;

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
