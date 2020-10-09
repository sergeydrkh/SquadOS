package app.os.discord.commands.music_commands;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import app.os.discord.music.player.GuildMusicManager;
import app.os.discord.music.player.MusicManager;
import app.os.discord.music.player.TrackScheduler;
import app.os.main.OS;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class RemoveTrack extends Command {
    public RemoveTrack() {
        this.name = "remove";
        this.requiredRole = DiscordBot.DJ_ROLE;
        this.help = "убрать трек с определённой позицией из очереди";
        this.arguments = "[часть названия трека]";
        this.cooldown = 2;
    }

    @Override
    protected void execute(CommandEvent event) {
        String messageText = event.getMessage().getContentRaw();
        String[] args = messageText.split(OS.DEFAULT_MESSAGE_DELIMITER);

        EmbedBuilder result = new EmbedBuilder();
        if (args.length != 2) {
            result.setColor(Color.RED);
            result.setTitle("Ошибка!");
            result.setDescription(String.format("Неправильное использование команды! Используйте: %s %s.", this.name, this.arguments));
        } else {
            try {
                String partOfName = args[1];

                GuildMusicManager guildMusicManager = MusicManager.getInstance().getGuildAudioPlayer(event.getGuild());
                TrackScheduler scheduler = guildMusicManager.scheduler;

                AudioTrack removedTrack = scheduler.removeTrack(partOfName);
                if (removedTrack != null) {
                    result.setColor(Color.GREEN);
                    result.setTitle("Успешно.");
                    result.setDescription(String.format("Трек с названием ``%s`` был убран из очереди.", removedTrack.getInfo().title));
                } else {
                    result.setColor(Color.RED);
                    result.setTitle("Ошибка!");
                    result.setDescription(String.format("Трек с названием (частью) ``%s`` не был найден.", partOfName));
                }
            } catch (Exception e) {
                result.setColor(Color.RED);
                result.setTitle("Ошибка!");
                result.setDescription(String.format("Во время выполнения команды произошла ошибка! %s.", e.getMessage()));
            }
        }

        event.getChannel().sendMessage(result.build()).complete();
    }
}