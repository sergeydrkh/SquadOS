package os.bots.discord.dsCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.ChannelType;

public class DS_Register extends Command {
    {
        this.name = "reg";
        this.help = "получить код регистрации";
        this.arguments = "";
        this.guildOnly = false;
        this.cooldown = 300;
    }

    @Override
    protected void execute(CommandEvent event) {
        // if author bot - ignore
        if (event.getAuthor().isBot())
            return;

        // generate registration code and add to database
        String regCode = System.currentTimeMillis() + ":" + (Math.random() * 100000000) + "." + event.getAuthor().getId();
        String message = "**Ваш код регистрации:**\n``" + regCode + "``";

        // send code in private msg
        if (event.getChannel().getType() == ChannelType.PRIVATE)
            event.getChannel().sendMessage(message).queue();
        else
            event.getMember().getUser().openPrivateChannel().complete().sendMessage(message).queue();
    }
}
