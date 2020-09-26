package app.os.discord.music.thread;

import app.os.console.ConsoleHelper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoDisconnection extends Thread {
    private static final List<Guild> guilds = new ArrayList<>();
    private static final Map<Guild, Integer> timers = new ConcurrentHashMap<>();

    private static final int LIMIT = 5000;
    private static final int RELOAD = 1000;

    public static void addGuild(Guild guild) {
        if (!guilds.contains(guild))
            guilds.add(guild);
    }

    public AutoDisconnection() {
        setDaemon(true);
        setName("Auto-Disconnection");
    }

    @Override
    public void run() {
        while (true) {
            List<Guild> tempGuilds = new ArrayList<>(guilds);

            try {
                for (Guild guild : tempGuilds) {
                    System.out.println("!");
                    AudioManager manager = guild.getAudioManager();

                    System.out.println(timers.containsKey(guild));
                    if (timers.containsKey(guild)) {
                        if (isVoiceChannelEmpty(manager.getConnectedChannel())) {
                            int timerNow = timers.get(guild);

                            if (timerNow >= LIMIT) {
                                manager.closeAudioConnection();
                                MusicManager.getInstance().getGuildAudioPlayer(guild).scheduler.clearQueue();
                                timers.remove(guild);
                            } else {
                                timers.put(guild, timerNow + RELOAD);
                            }
                        }
                    } else {
                        if (manager.isConnected()) {
                            int connectedUsers = 0;
                            for (Member member : manager.getConnectedChannel().getMembers())
                                if (!member.getUser().isBot()) {
                                    connectedUsers++;
                                }

                            if (connectedUsers <= 0) {
                                timers.put(guild, 0);
                            }
                        } else {
                            timers.remove(guild);
                            guilds.remove(guild);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(RELOAD);
            } catch (InterruptedException e) {
                ConsoleHelper.errln("Error! " + e.getMessage());
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
}
