package app.os.discord.commands.bot_commands.users;

import app.os.console.ConsoleHelper;
import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import app.os.utilities.Emoji;
import app.os.main.OS;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Info {
    // INFO COMMAND
    public static class InfoCard extends Command {
        public InfoCard() {
            this.name = "info";
            this.help = "получить информацию о боте";
            this.cooldown = 30;
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            EmbedBuilder infoMsg = new EmbedBuilder();

            infoMsg.setColor(OS.DEFAULT_COLOR);

            infoMsg.setTitle(OS.NAME);
            infoMsg.setDescription(OS.DESCRIPTION);
            infoMsg.setImage("https://sun9-32.userapi.com/y2Bhtmpmv84Moc_3cMLCAUdVxvZQpp9YcLXjVA/ogKcTeIZ41A.jpg");
            infoMsg.setFooter("© 2020 SquadOS, Inc.", "https://sun9-52.userapi.com/kCqPd1nYHIqx95X1c2YpM3vOnlXR_PtPInWgBA/Ohplrp5Bpm8.jpg");

            infoMsg.addField("Название", "SquadOS", true);
            infoMsg.addField("Версия.", "v" + OS.VERSION, true);
            infoMsg.addField("Серверов", String.valueOf(commandEvent.getJDA().getGuilds().size()), true);
            infoMsg.addField("Аптайм", String.format("%dh", (new Date().getTime() - OS.DATE_LAUNCH.getTime()) / (60 * 60 * 1000)), true);

            String[] wishes = new String[]{"Удачного дня", "Доброе утро", "Хорошего настроения", "Не унывать"};
            infoMsg.addField("Пожелание", wishes[(int) (Math.random() * wishes.length)], true);

            infoMsg.addField("", "", true);

            commandEvent.getChannel().sendMessage(infoMsg.build()).queue();
        }
    }

    // LINK COMMAND
    public static class BotLink extends Command {
        private final String inviteUrl;

        public BotLink(String inviteUrl) {
            this.inviteUrl = inviteUrl;
            this.name = "link";
            this.help = "получить ссылку на бота";
            this.cooldown = 10;
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            if (inviteUrl != null)
                commandEvent.getChannel().sendMessage(String.format("**Ссылка на бота:** %s.", inviteUrl)).queue();
            else
                commandEvent.getChannel().sendMessage("Ссылка недоступна!").queue();
        }
    }

    // PING COMMAND
    public static class Ping extends Command {
        public Ping() {
            this.name = "ping";
            this.help = "получить пинг бота";
            this.cooldown = 2;
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            commandEvent.getChannel().sendMessage("Измерение...")
                    .queue(s -> s.editMessage(String.format("Bot's ping: **%dms**%nGateway ping: **%dms**",
                            commandEvent.getMessage().getTimeCreated().until(s.getTimeCreated(), ChronoUnit.MILLIS),
                            commandEvent.getJDA().getGatewayPing())).queue());
        }
    }

    // SERVER STATE COMMAND
    public static class ServerState extends Command {
        private final Date launchDate;

        public ServerState(Date launchDate) {
            this.launchDate = launchDate;

            this.name = "state";
            this.help = "получить информацию о состоянии сервера";
            this.requiredRole = DiscordBot.CREATOR_ROLE;
            this.cooldown = 5;
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            EmbedBuilder stateMessage = new EmbedBuilder();
            stateMessage.setColor(Color.GRAY);

            StringBuilder activeThreads = new StringBuilder();
            Thread.getAllStackTraces().keySet().forEach(thread -> activeThreads.append(" - ").append(thread.getName()).append("\n"));

            stateMessage.addField(Emoji.THREADS.getEmoji() + String.format("Активно потоков (%d)", Thread.activeCount()), activeThreads.toString(), false);

            stateMessage.addField("", "", false);

            stateMessage.addField(
                    Emoji.GRAPH.getEmoji() + "Память",
                    String.format(" - Использовано: %s%n - Свободно: %s%n - Всего: %s",
                            byteToMiB(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()),
                            byteToMiB(Runtime.getRuntime().freeMemory()),
                            byteToMiB(Runtime.getRuntime().totalMemory())),
                    true);

            stateMessage.addField(
                    Emoji.TIME.getEmoji() + "Время",
                    String.format(" - Сейчас: %s%n - Время работы: %sh",
                            OS.DEFAULT_DATE_FORMAT.format(new Date()),
                            (new Date().getTime() - OS.DATE_LAUNCH.getTime()) / (60 * 60 * 1000)
                    ),
                    true);

            stateMessage.addField("", "", false);

            stateMessage.addField(
                    Emoji.RED_SQUARE.getEmoji() + "Ошибок",
                    String.format(" - Кол-во: %d", ConsoleHelper.getErrors()),
                    true
            );
            commandEvent.getChannel().sendMessage(stateMessage.build())
                    .queue(q -> {
                        stateMessage.addField(
                                Emoji.INTERNET.getEmoji() + "Пинг",
                                String.format(" - Gateway: %dms%n - Bot: %dms",
                                        commandEvent.getJDA().getGatewayPing(),
                                        commandEvent.getMessage().getTimeCreated().until(q.getTimeCreated(), ChronoUnit.MILLIS)),
                                true
                        );

                        q.editMessage(stateMessage.build()).queue();
                    });
        }

        private String timeToString(long secs) {
            long hour = secs / 3600,
                    min = secs / 60 % 60,
                    sec = secs % 60;
            return String.format("%02d:%02d:%02d", hour, min, sec);
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

}
