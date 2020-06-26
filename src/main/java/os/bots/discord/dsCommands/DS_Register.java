package os.bots.discord.dsCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class DS_Register extends Command {
    {
        this.name = "reg";
        this.help = "получить код регистрации";
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent event) {
        // if author bot - ignore
        if (event.getAuthor().isBot())
            return;

        // generate registration code and add to database
        String regCode = System.currentTimeMillis() + ":" + (Math.random() * 10000);

        // send code in private msg
        event.getMember().getUser().openPrivateChannel().complete().sendMessage("**Ваш код регистрации:**\n``" + regCode + "``").queue();
    }
}
