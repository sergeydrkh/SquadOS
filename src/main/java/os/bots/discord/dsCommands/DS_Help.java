package os.bots.discord.dsCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class DS_Help extends Command {
    {
        this.name = "help";
        this.help = "помощь по командам";
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent event) {
        // ignore
    }
}
