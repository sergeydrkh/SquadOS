package os.bots.discord.dsCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import os.MainThread;
import os.bots.discord.DiscordBot;

public class DS_Register extends Command {
    {
        this.name = "reg";
        this.help = "получить код регистрации";
        this.arguments = "";
        this.guildOnly = true;
        this.cooldown = 20;
    }

    @Override
    protected void execute(CommandEvent event) {
        // if author bot - ignore
        if (event.getAuthor().isBot())
            return;

        // generate registration code and add to database
        String regCode = System.currentTimeMillis() + ":" + (Math.random() * 100000000) + "." + event.getAuthor().getId();
        String message = "**Ваш код регистрации:**\n``" + regCode + "``\n\nВведите его на сайте " + MainThread.MAIN_WEBSITE_URL + "\n**Не передавайте никому данный код!**";

        // get private channel
        MessageChannel sendTo;
        if (event.getChannel().getType() == ChannelType.PRIVATE)
            sendTo = event.getChannel();
        else
            sendTo = event.getMember().getUser().openPrivateChannel().complete();

        // add code to database
        if (!DiscordBot.dbManager.insertData(DiscordBot.DB_REGCODES_TABLE, new String[]{"regcode", "userID"}, new String[]{regCode,
                event.getMember().getId()})) {
            sendTo.sendMessage("**Произошла ошибка!** Нету связи с базой данных.\nПовторите попытку позже.").queue();
            return;
        }

        // send code
        sendTo.sendMessage("**Ваш код регистрации:**")
                .queue(s1 -> sendTo.sendMessage(regCode)
                        .queue(s2 -> sendTo.sendMessage("\n\nВведите его на сайте https://" + MainThread.MAIN_WEBSITE_URL)
                                .queue(s3 -> sendTo.sendMessage("``Не передавайте никому данный код!``").queue())));
    }
}
