package app.os.discord.commands.music_commands;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import app.os.discord.music.player.GuildMusicManager;
import app.os.discord.music.player.MusicManager;
import app.os.discord.music.utils.TrackInfo;
import app.os.main.OS;
import app.os.sql.drivers.SQLDriver;
import app.os.sql.models.MusicQueueModel;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
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
                        String.format("Количество треков в очереди: **%d**", guildMusicManager.scheduler.getTracksInQueue().size()),
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
        private final SQLDriver driver;
        private final String tracksTable;

        public DeleteQueue(SQLDriver driver, String tracksTable) {
            this.driver = driver;
            this.tracksTable = tracksTable;
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
        private final SQLDriver driver;
        private final String tracksTable;
        private final String googleApiKey;

        public SaveQueue(SQLDriver driver, String tracksTable, String googleApiKey) {
            this.driver = driver;
            this.tracksTable = tracksTable;
            this.googleApiKey = googleApiKey;
            this.name = "saveq";
            this.help = "сохранить текущий плейлист";
            this.arguments = "[название]";
            this.requiredRole = DiscordBot.DJ_ROLE;
        }

        @Override
        protected void execute(CommandEvent event) {
            String messageText = event.getMessage().getContentRaw();
            String[] args = messageText.split(OS.DEFAULT_MESSAGE_DELIMITER);
            if (args.length != 2) {
                event.getChannel().sendMessage("Неправильное использование команды!").complete();
                return;
            }

            try {
                List<AudioTrack> toSave = MusicManager.getInstance().getGuildAudioPlayer(event.getGuild()).scheduler.getTracksInQueue();
                MusicQueueModel musicQueueModel = new MusicQueueModel(googleApiKey);
                musicQueueModel.saveTracks(driver, tracksTable, toSave, event.getGuild(), args[1]);

                event.getChannel().sendMessage(new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setTitle("Сохранение")
                        .setDescription(String.format("Очередь ``%s`` сохранена!", args[1])).build()).complete();
            } catch (Exception e) {
                logger.error(e.getMessage());
                event.getChannel().sendMessage("Ошибка! " + e.getMessage()).complete();
                e.printStackTrace();
                return;
            }
        }
    }

    public static class PlayQueue extends Command {
        private final SQLDriver driver;
        private final String tracksTable;
        private final String googleApiKey;

        public PlayQueue(SQLDriver driver, String tracksTable, String googleApiKey) {
            this.driver = driver;
            this.tracksTable = tracksTable;
            this.googleApiKey = googleApiKey;
            this.name = "playq";
            this.help = "воспроизвести сохраненный плейлист";
            this.arguments = "[название]";
            this.requiredRole = DiscordBot.DJ_ROLE;
        }

        @Override
        protected void execute(CommandEvent event) {
            String messageText = event.getMessage().getContentRaw();
            String[] args = messageText.split(OS.DEFAULT_MESSAGE_DELIMITER);
            if (args.length != 2) {
                event.getChannel().sendMessage("Неправильное использование команды!").complete();
                return;
            }

            EmbedBuilder result = new EmbedBuilder();

            try {
                MusicQueueModel musicQueueModel = new MusicQueueModel(googleApiKey);
                List<String> urls = musicQueueModel.getTracksUrl(driver, tracksTable, event.getGuild(), args[1]);

                MusicManager musicManager = MusicManager.getInstance();
                for (String trackUrl : urls) {
                    musicManager.loadAndPlay(event.getTextChannel(), false, trackUrl);
                }

                result.setTitle("Добавление");
                if (!urls.isEmpty()) {
                    result.setColor(Color.GREEN);
                    result.setDescription(String.format("Очередь с названием ``%s`` успешно добавлена в общую очередь.", args[1]));
                } else {
                    result.setColor(Color.YELLOW);
                    result.setDescription("Очередь оказалась пустой, треки не были добавлены.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());

                result.setColor(Color.RED);
                result.setTitle("Ошибка!");
                result.setDescription(String.format("Во время выполнения операции произошла ошибка. ``%s``.", e.getMessage()));

                return;
            }

            event.getChannel().sendMessage(result.build()).complete();
        }
    }

    public static class RemoveQueue extends Command {
        private final SQLDriver driver;
        private final String tracksTable;

        public RemoveQueue(SQLDriver driver, String tracksTable) {
            this.driver = driver;
            this.tracksTable = tracksTable;
            this.name = "removeq";
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

