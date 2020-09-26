package app.os.discord.commands.commands.users;

import app.os.discord.commands.self.Command;
import app.os.discord.commands.self.CommandEvent;

public class Link extends Command {
    private final String inviteUrl;

    public Link(String inviteUrl) {
        this.inviteUrl = inviteUrl;
        this.name = "link";
        this.help = "получить ссылку на бота";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (inviteUrl != null)
            commandEvent.getChannel().sendMessage(String.format("**Ссылка на бота:** %s.", inviteUrl)).queue();
        else
            commandEvent.getChannel().sendMessage("Ссылка недоступна!").queue();
    }
}
