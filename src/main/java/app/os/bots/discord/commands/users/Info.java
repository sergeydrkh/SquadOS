package app.os.bots.discord.commands.users;

import app.os.thread.OS;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Info extends Command {
    public Info() {
        this.name = "info";
        this.help = "get info about bot";
        this.cooldown = 50;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        EmbedBuilder infoMsg = new EmbedBuilder();

        Date currentDate = new Date();
        infoMsg.setColor(OS.DEFAULT_COLOR);

        infoMsg.addField("Server's name", commandEvent.getGuild().getName(), true);
        infoMsg.addField("Date", OS.DEFAULT_DATE_FORMAT.format(currentDate), true);
        infoMsg.addField("", "", false);
        infoMsg.addField("Name", "SquadOS", true);
        infoMsg.addField("Ver.", "v" + OS.VERSION, true);

        commandEvent.getChannel().sendMessage(infoMsg.build()).queue(success -> {
            infoMsg.addField("Ping", String.valueOf(commandEvent.getMessage().getTimeCreated().until(success.getTimeCreated(), ChronoUnit.MILLIS)), true);

            success.getChannel().editMessageById(success.getId(), infoMsg.build()).queue();
        });
    }
}
