package app.os.discord.music.player;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoDisconnection extends Thread {
    private static final Map<Guild, Integer> timers = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(AutoDisconnection.class);

    private final JDA jda;

    private static final int LIMIT = 5000;
    private static final int RELOAD = 1000;

    public AutoDisconnection(JDA jda) {
        this.jda = jda;
        setDaemon(true);
        setName("Auto-Disconnection");
    }

    @Override
    public void run() {
        // close all connections
        jda.getGuilds().forEach(guild -> guild.getAudioManager().closeAudioConnection());

        MusicManager musicManager = MusicManager.getInstance();

        while (true) {
            for (Guild guild : jda.getGuilds()) {
                AudioManager audioManager = guild.getAudioManager();
                GuildMusicManager guildMusicManager = musicManager.getGuildAudioPlayer(guild);

                // check if connected
                if (audioManager.isConnected()) {
                    // check users
                    if (isVoiceChannelEmpty(audioManager.getConnectedChannel())) {
                        disconnect(audioManager);
                    }

                    // check queue
                    if (guildMusicManager.scheduler.getTracksInQueue().isEmpty() && guildMusicManager.player.getPlayingTrack() == null) {
                        disconnect(audioManager);
                    }
                }

                // disconnected
                else {
                    clearQueue(guildMusicManager);
                }
            }
        }
    }

    private boolean isVoiceChannelEmpty(VoiceChannel vc) {
        int connectedUsers = 0;
        for (Member member : vc.getMembers())
            if (!member.getUser().isBot()) {
                connectedUsers++;
            }

        return connectedUsers == 0;
    }

    public static void disconnect(AudioManager manager) {
        GuildMusicManager guildMusicManager = MusicManager.getInstance().getGuildAudioPlayer(manager.getGuild());
        clearQueue(guildMusicManager);
        manager.closeAudioConnection();
    }

    private static void clearQueue(GuildMusicManager guildMusicManager) {
        if (!guildMusicManager.scheduler.getTracksInQueue().isEmpty())
            guildMusicManager.scheduler.clearQueue();
        if (guildMusicManager.player.getPlayingTrack() != null)
            guildMusicManager.player.stopTrack();
    }
}
