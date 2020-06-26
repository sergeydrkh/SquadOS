package os.bots.discord.dsCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import os.bots.discord.DiscordBot;

public class DS_Info extends Command {
    {
        this.name = "info";
        this.help = "информация о боте";
        this.arguments = "";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        // if author bot - ignore
        if (event.getAuthor().isBot())
            return;

        // build info message
        EmbedBuilder infoMessage = new EmbedBuilder();

        infoMessage.setTitle("Информация о боте:");                                 // title
        infoMessage.addField("Название", DiscordBot.NAME, true);        // name
        infoMessage.addField("Версия", DiscordBot.VERSION, true);       // version

        // send message
        event.getChannel().sendMessage(infoMessage.build()).queue();
    }
}
