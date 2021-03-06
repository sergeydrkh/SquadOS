package app.os.discord.music.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MusicManager {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    private static MusicManager instance;

    public static MusicManager getInstance() {
        if (instance == null) instance = new MusicManager();
        return instance;
    }

    private MusicManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(final TextChannel channel, boolean notify, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (notify)
                    channel.sendMessage(String.format("Добавление в очередь: ``%s``.%n**Ссылка: **%s.", track.getInfo().title, track.getInfo().uri)).queue();
                play(channel.getGuild(), musicManager, track, channel, notify);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (notify)
                    channel.sendMessage(String.format("Добавление плейлиста: ``%s`` в очередь.", playlist.getName())).queue();
                playlist.getTracks().forEach(track -> play(channel.getGuild(), musicManager, track, channel, notify));
            }

            @Override
            public void noMatches() {
                if (notify)
                    channel.sendMessage(String.format("Ничего не найдено по ссылке: %s.", trackUrl)).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                if (notify)
                    channel.sendMessage(String.format("Не удалось воспроизвести.%nОшибка: ``%s``.", exception.getMessage())).queue();
            }
        });
    }

    public void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, TextChannel channel, boolean notify) {
        AudioManager audioManager = guild.getAudioManager();
        ConnectionResult result = connectToFirstVoiceChannel(audioManager);

        if (!audioManager.isConnected()) {
            EmbedBuilder resultBuilder = new EmbedBuilder();
            resultBuilder.setTitle("Результат подключения");

            if (result.isConnected())
                resultBuilder.setColor(Color.GREEN).setDescription(String.format("Подключение к голосовому каналу ``%s``.", result.getConnectedChannel().getName()));
            else
                resultBuilder.setColor(Color.RED).setDescription("Не удалось подключиться к голосовому каналу!");

            if (notify)
                channel.sendMessage(resultBuilder.build()).complete();
        }

        musicManager.scheduler.queue(track);
    }

    private static ConnectionResult connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                if (!voiceChannel.getMembers().isEmpty()) {
                    audioManager.openAudioConnection(voiceChannel);
                    return new ConnectionResult(true, voiceChannel);
                }
            }
        }

        return new ConnectionResult(false, null);
    }

    private static class ConnectionResult {
        private final boolean connected;
        private final VoiceChannel connectedChannel;

        public ConnectionResult(boolean success, VoiceChannel connectedChannel) {
            this.connected = success;
            this.connectedChannel = connectedChannel;
        }

        public boolean isConnected() {
            return connected;
        }

        public VoiceChannel getConnectedChannel() {
            return connectedChannel;
        }

    }

    public void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        AudioTrack next = musicManager.scheduler.nextTrack();
        try {
            channel.sendMessage(String.format("Следующий трек: **%s**.", next.getInfo().title)).queue();
        } catch (NullPointerException e) {
            channel.sendMessage("Очередь пуста.").queue();
        }
    }
}
