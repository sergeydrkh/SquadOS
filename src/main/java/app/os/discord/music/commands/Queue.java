package app.os.discord.music.commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.self.GuildMusicManager;
import app.os.discord.music.self.MusicManager;
import app.os.main.OS;
import app.os.discord.commands.self.Command;
import app.os.discord.commands.self.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Queue {
    private static final Logger logger = LoggerFactory.getLogger(Queue.class.getName());

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

                int i = 0;
                for (AudioTrack track : guildMusicManager.scheduler.getTracksInQueue()) {
                    i++;
                    allTracks.append(" - ").append(i).append(". ").append(track.getInfo().title).append("\n");
                }

                queueMessage.addField("Играет сейчас", " - " + guildMusicManager.player.getPlayingTrack().getInfo().title, false);
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

