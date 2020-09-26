package app.os.discord.commands.self.users;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;

public class Link extends Command {

    public Link() {
        this.name = "link";
        this.help = "получить ссылку на бота";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage(String.format("**Ссылка на бота:** %s.", DiscordBot.INVITE_URL)).queue();
    }
}
