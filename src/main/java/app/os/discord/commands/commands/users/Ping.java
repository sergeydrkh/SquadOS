package app.os.discord.commands.commands.users;

import app.os.discord.commands.self.Command;
import app.os.discord.commands.self.CommandEvent;

import java.time.temporal.ChronoUnit;

public class Ping extends Command {
    public Ping() {
        this.name = "ping";
        this.help = "получить пинг бота";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getChannel().sendMessage("Измерение...")
                .queue(s -> s.editMessage(String.format("Пинг: **%dms**", commandEvent.getMessage().getTimeCreated().until(s.getTimeCreated(), ChronoUnit.MILLIS))).queue());
    }
}
