package app.os.discord.commands.users;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.time.temporal.ChronoUnit;

public class Ping extends Command {
    public Ping() {
        this.name = "ping";
        this.help = "получить пинг бота";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage("Calculating...")
                .queue(s -> s.editMessage(String.format("Ping is **%dms**", commandEvent.getMessage().getTimeCreated().until(s.getTimeCreated(), ChronoUnit.MILLIS))).queue());
    }
}
