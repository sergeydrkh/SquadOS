package app.os.discord.commands.music_commands;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import app.os.discord.music.player.GuildMusicManager;
import app.os.discord.music.player.MusicManager;
import app.os.discord.music.utils.TrackInfo;
import app.os.main.OS;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TrackQueue {
    private static final Logger logger = LoggerFactory.getLogger(TrackQueue.class.getName());

    public static class GetQueue extends Command {
        public GetQueue() {
            this.name = "queue";
            this.help = "получить список проигрываемой музыки";
            this.requiredRole = DiscordBot.DJ_ROLE;
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            try {
                EmbedBuilder queueMessage = new EmbedBuilder();
                queueMessage.setColor(OS.DEFAULT_COLOR);

                StringBuilder allTracks = new StringBuilder();
                GuildMusicManager guildMusicManager = MusicManager.getInstance().getGuildAudioPlayer(commandEvent.getGuild());

                long allDuration = 0;

                int i = 0;
                for (AudioTrack track : guildMusicManager.scheduler.getTracksInQueue()) {
                    i++;
                    allTracks
                            .append(" > ")
                            .append(String.format("**%.1f мин** - ", ((double) TrackInfo.Duration.getLength(track) / 60)))
                            .append(track.getInfo().title)
                            .append("\n");

                    allDuration += TrackInfo.Duration.getLength(track);
                }

                AudioTrack playingTrack = guildMusicManager.player.getPlayingTrack();
                allDuration += TrackInfo.Duration.getLength(playingTrack);

                queueMessage.addField(String.format("Общая длительность: %.1f минут", ((double) allDuration / 60)),
                        String.format("Количество треков в очереди: %d", guildMusicManager.scheduler.getTracksInQueue().size()),
                        false);

                queueMessage.addField("Играет сейчас",
                                String.format("> **%.1f мин** - %s",
                                        (double) TrackInfo.Duration.getLength(playingTrack) / 60,
                                        playingTrack.getInfo().title),
                        false);

                if (!allTracks.toString().equals(""))
                    queueMessage.addField("В очереди", allTracks.toString(), false);

                commandEvent.getChannel().sendMessage(queueMessage.build()).queue();
            } catch (NullPointerException e) {
                commandEvent.getChannel().sendMessage("Очередь пуста.").queue();
            }
        }
    }

    public static class DeleteQueue extends Command {
        public DeleteQueue() {
            this.name = "delq";
            this.help = "удалить плейлист";
            this.arguments = "[название]";
            this.requiredRole = DiscordBot.DJ_ROLE;
        }

        @Override
        protected void execute(CommandEvent event) {

            // delq
        }
    }

    public static class SaveQueue extends Command {
        public SaveQueue() {
            this.name = "saveq";
            this.help = "сохранить текущий плейлист";
            this.arguments = "[название]";
            this.requiredRole = DiscordBot.DJ_ROLE;
        }

        @Override
        protected void execute(CommandEvent event) {
            List<AudioTrack> toSave = MusicManager.getInstance().getGuildAudioPlayer(event.getGuild()).scheduler.getTracksInQueue();
            logger.debug(toSave.toString());
            // saveq
        }
    }

    public static class PlayQueue extends Command {
        public PlayQueue() {
            this.name = "playq";
            this.help = "воспроизвести сохраненный плейлист";
            this.arguments = "[название]";
            this.requiredRole = DiscordBot.DJ_ROLE;
        }

        @Override
        protected void execute(CommandEvent event) {
            // playq
        }
    }

    public static class RemoveTrack extends Command {
        public RemoveTrack() {
            this.name = "remove";
            this.arguments = "[часть названия]";
            this.help = "убрать трек с определённым индексом из очереди";
            this.requiredRole = DiscordBot.DJ_ROLE;
        }

        @Override
        protected void execute(CommandEvent event) {
            MusicManager musicManager = MusicManager.getInstance();
            GuildMusicManager guildMusic = musicManager.getGuildAudioPlayer(event.getGuild());

            String partOfName = event.getMessage().getContentRaw().trim().substring(this.name.length() + 3);
            AudioTrack removedTrack = guildMusic.scheduler.removeTrack(partOfName);

            if (removedTrack != null)
                event.getChannel().sendMessage(String.format("Убрано из очереди: **%s**.", removedTrack.getInfo().title)).complete();
            else
                event.getChannel().sendMessage("Трека с таким названием не было найдено!").complete();
        }
    }
}

