package app.os.discord.commands.music_commands;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import app.os.discord.music.player.AutoDisconnection;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class Disconnect extends Command {
    public Disconnect() {
        this.name = "disconnect";
        this.help = "принудительно отключить бота от голосового канала";
        this.requiredRole = DiscordBot.DJ_ROLE;
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder().setTitle("Результат");

        if (event.getGuild().getAudioManager().isConnected()) {
            AutoDisconnection.disconnect(event.getGuild().getAudioManager());
            builder.setColor(Color.GREEN).setDescription("**Отключение** от голосового канала.\nОчередь **очищена**.");
        } else {
            builder.setColor(Color.RED).setDescription("Бот на данный момент не находится **ни в одном** голосовом канале!");
        }

        event.getChannel().sendMessage(builder.build()).complete();
    }
}
