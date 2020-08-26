package app.os.discord.commands.admin;

import app.os.discord.DiscordBot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class CreateCallback extends Command {
    public CreateCallback() {
        this.name = "createCallback";
        this.help = "{name} {condition1,condition2,..} {decision1,decision2,..}";
        this.requiredRole = DiscordBot.ADMIN_ROLES;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {

    }
}
