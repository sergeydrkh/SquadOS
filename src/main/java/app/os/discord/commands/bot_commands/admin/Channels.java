package app.os.discord.commands.bot_commands.admin;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import app.os.main.OS;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Channels {
    public static class Clear extends Command {
        public Clear() {
            this.name = "clear";
            this.requiredRole = DiscordBot.ADMIN_ROLES;
            this.help = "удалить все сообщения за последнюю неделю";
            this.cooldown = 10;
            this.cooldown = 20;
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);

            new Thread(() -> {
                while (true) {
                    List<Message> messages = commandEvent.getChannel().getHistory().retrievePast(50).complete();
                    messages.removeIf(m -> m.getTimeCreated().isBefore(twoWeeksAgo));

                    if (messages.isEmpty() || messages.size() < 2)
                        return;

                    commandEvent.getTextChannel().deleteMessages(messages).complete();
                }
            }).start();
        }
    }

    public static class Send extends Command {
        public Send() {
            this.name = "send";
            this.arguments = "[channels] [text]";
            this.help = "отправить сообщение от имени бота в текстовый канал";
            this.requiredRole = DiscordBot.ADMIN_ROLES;
            this.cooldown = 5;
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            Message received = commandEvent.getMessage();

            // process data
            String rawData = received.getContentRaw();
            List<TextChannel> mentionedChannels = received.getMentionedChannels();
            if (mentionedChannels.isEmpty()) {
                received.getChannel().sendMessage("Вы не указали текстовые каналы!").queue();
                return;
            }

            // get text
            String messageText = rawData.substring(rawData.split(" ")[0].length());
            for (TextChannel mentionedChannel : mentionedChannels)
                messageText = messageText.replaceAll("<#" + mentionedChannel.getId() + ">", "");

            // send received
            for (TextChannel mentionedChannel : mentionedChannels) {
                mentionedChannel.sendMessage(
                        new EmbedBuilder()
                                .setDescription(messageText)
                                .setColor(OS.DEFAULT_COLOR).build()).queue(s -> received.getChannel().sendMessage((String.format("Сообщение в чат <#%s> **успешно** отправлено!", mentionedChannel.getId()))).queue());
            }

        }
    }

    // TextChannels COMMAND
    public static class TextChannels extends Command {
        public TextChannels() {
            this.name = "text";
            this.help = "получить все текстовые чаты на сервере";
            this.requiredRole = DiscordBot.ADMIN_ROLES;
            this.cooldown = 20;
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            StringBuilder textChannels = new StringBuilder();

            textChannels.append("Текстовые чаты на сервере **").append(commandEvent.getGuild().getName()).append("**\n");
            commandEvent.getGuild().getTextChannels().forEach(channel -> textChannels.append("- <#").append(channel.getId()).append(">\n"));

            commandEvent.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setDescription(textChannels.toString())
                            .setColor(OS.DEFAULT_COLOR).build()).queue();
        }
    }

    // VoiceChannels COMMAND
    public static class VoiceChannels extends Command {
        public VoiceChannels() {
            this.name = "voice";
            this.help = "получить все голосовые команты на сервере";
            this.requiredRole = DiscordBot.ADMIN_ROLES;
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            StringBuilder voiceChannels = new StringBuilder();

            voiceChannels.append("Голосовые комнаты на сервере **").append(commandEvent.getGuild().getName()).append("**\n");
            commandEvent.getGuild().getVoiceChannels().forEach(channel -> voiceChannels.append("- ").append(channel.getName()).append("\n"));

            commandEvent.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setDescription(voiceChannels.toString())
                            .setColor(OS.DEFAULT_COLOR).build()).queue();
        }
    }

}
