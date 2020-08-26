package app.os.discord.commands.admin;

import app.os.discord.DiscordBot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RemoveCallback extends Command {
    public RemoveCallback() {
        this.name = "delCallback";
        this.help = "{callback name}";
        this.requiredRole = DiscordBot.ADMIN_ROLES;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {

    }
}
