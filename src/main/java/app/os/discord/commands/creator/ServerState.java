package app.os.discord.commands.creator;

import app.os.discord.DiscordBot;
import app.os.main.OS;
import app.os.utilities.ConsoleHelper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ServerState extends Command {
    private final Date launchDate;

    public ServerState(Date launchDate) {
        this.launchDate = launchDate;

        this.name = "state";
        this.help = "получить информацию о состоянии сервера";
        this.requiredRole = DiscordBot.CREATOR_ROLE;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        EmbedBuilder stateMessage = new EmbedBuilder();
        stateMessage.setColor(Color.GRAY);

        StringBuilder activeThreads = new StringBuilder();
        Thread.getAllStackTraces().keySet().forEach(thread -> activeThreads.append(" - ").append(thread.getName()).append("\n"));

        stateMessage.addField(String.format("Active threads (%d)", Thread.activeCount()), activeThreads.toString(), false);

        stateMessage.addField("", "", false);

        long milliseconds = new Date().getTime() - launchDate.getTime();
        int hours = (int) (milliseconds / (60 * 60 * 1000));
        int minutes = Math.max((int) (milliseconds / (60 * 1000)) - (hours * 60), 0);
        int seconds = Math.max((int) (milliseconds / (1000)) - (minutes * 60), 0);

        stateMessage.addField("Time",
                String.format(" - Now: %s%n - Working time: %s",
                        OS.DEFAULT_DATE_FORMAT.format(new Date()),
                        hours + "h" + " " + minutes + "m" + " " + seconds + "s"),
                true);

        stateMessage.addField(
                "RAM",
                String.format(" - Used: %s%n - Free: %s%n - Total: %s",
                        byteToMiB(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()),
                        byteToMiB(Runtime.getRuntime().freeMemory()),
                        byteToMiB(Runtime.getRuntime().totalMemory())),
                true);

        stateMessage.addField("", "", false);

        stateMessage.addField(
                "Errors",
                String.format(" - Count: %d" , ConsoleHelper.getErrors()),
                true
        );

        commandEvent.getChannel().sendMessage(stateMessage.build())
                .queue(q -> {
                    stateMessage.addField(
                            "Ping",
                            String.format(" - Gateway: %dms%n - Bot: %dms",
                                    commandEvent.getJDA().getGatewayPing(),
                                    commandEvent.getMessage().getTimeCreated().until(q.getTimeCreated(), ChronoUnit.MILLIS)),
                            true
                    );

                    q.editMessage(stateMessage.build()).queue();
                });
    }

    public static String byteToMiB(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024)
            return bytes + " B";

        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");

        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }

        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }
}
