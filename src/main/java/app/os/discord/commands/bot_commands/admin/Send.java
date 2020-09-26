package app.os.discord.commands.bot_commands.admin;

import app.os.discord.DiscordBot;
import app.os.main.OS;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Send extends Command {
    public Send() {
        this.name = "send";
        this.arguments = "[channels] [text]";
        this.help = "отправить сообщение от имени бота в текстовый канал";
        this.requiredRole = DiscordBot.ADMIN_ROLES;
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
