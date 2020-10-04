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
        jda.getGuilds().forEach(guild -> disconnect(guild.getAudioManager()));

        // check new connections
        while (true) {
            try {
                // walk by all guilds
                for (Guild guild : jda.getGuilds()) {
                    AudioManager manager = guild.getAudioManager();
                    MusicManager musicManager = MusicManager.getInstance();
                    GuildMusicManager guildMusic = musicManager.getGuildAudioPlayer(guild);

                    // check if connected
                    if (manager.isConnected()) {
                        if (isVoiceChannelEmpty(manager.getConnectedChannel())) {
                            // if contains key in timer list
                            if (timers.containsKey(guild)) {
                                int timerNow = timers.get(guild);

                                if (timerNow >= LIMIT) {
                                    disconnect(manager);
                                    timers.remove(guild);
                                } else {
                                    timers.put(guild, timerNow + RELOAD);
                                }
                            }
                        }

                        // check queue is empty
                        else if (guildMusic.scheduler.getTracksInQueue().isEmpty() && guildMusic.player.getPlayingTrack() == null) {
                            disconnect(manager);
                        }

                        // add to timers
                        else {
                            timers.put(guild, 0);
                        }
                    }

                    // if disconnected remove from timer list
                    else {
                        timers.remove(guild);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

            try {
                Thread.sleep(RELOAD);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

        }
    }

    private void disconnect(AudioManager manager) {
        GuildMusicManager guildMusicManager = MusicManager.getInstance().getGuildAudioPlayer(manager.getGuild());

        if (!guildMusicManager.scheduler.getTracksInQueue().isEmpty())
            guildMusicManager.scheduler.clearQueue();
        if (guildMusicManager.player.getPlayingTrack() != null)
            guildMusicManager.player.stopTrack();

        manager.closeAudioConnection();
    }

    private boolean isVoiceChannelEmpty(VoiceChannel vc) {
        int connectedUsers = 0;
        for (Member member : vc.getMembers())
            if (!member.getUser().isBot()) {
                connectedUsers++;
            }

        return connectedUsers == 0;
    }
}
