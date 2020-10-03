package app.os.discord.commands.music_commands;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import app.os.discord.music.player.GuildMusicManager;
import app.os.discord.music.player.MusicManager;
import app.os.discord.music.utils.TrackInfo;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class Player extends Command {
    private static final List<UpdateBar> activePlayers = new ArrayList<>();

    public Player() {
        this.name = "player";
        this.help = "открыть плеер";
        this.requiredRole = DiscordBot.DJ_ROLE;
        this.cooldown = 50;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
            GuildMusicManager guildMusicManager = MusicManager.getInstance().getGuildAudioPlayer(commandEvent.getGuild());

            commandEvent.getChannel().sendMessage("**Загрузка...**").queue(q -> {
                        UpdateBar bar = new UpdateBar(guildMusicManager, q);

                        activePlayers.forEach(player -> {
                            if (bar.getGuildID() == player.getGuildID())
                                player.stopUpdate();
                        });

                        activePlayers.add(0, bar);

                        bar.start();
                    }
            );

        } catch (NullPointerException e) {
            commandEvent.getChannel().sendMessage("Ничего не воспроизводится!").queue();
        }
    }


    private static class UpdateBar extends Thread {
        private final GuildMusicManager guildMusicManager;
        private final Message toUpdate;
        private boolean active = true;

        public long getGuildID() {
            return toUpdate.getGuild().getIdLong();
        }

        public UpdateBar(GuildMusicManager guildMusicManager, Message toUpdate) {
            setDaemon(true);
            setName("UpdateBar Thread - " + toUpdate.getIdLong());

            this.guildMusicManager = guildMusicManager;
            this.toUpdate = toUpdate;
        }

        public void stopUpdate() {
            toUpdate.editMessage("Плеер закрыт.").queue();
            this.active = false;
            removeMessage();
        }

        private String timeToString(long secs) {
            long hour = secs / 3600,
                    min = secs / 60 % 60,
                    sec = secs % 60;
            return String.format("%02d:%02d:%02d", hour, min, sec);
        }

        private void setButtons(Message message) {
            try {
                message.addReaction("\u23EF").queue(); // ⏯
                message.addReaction("\u23ED").queue(); // ⏭
            } catch (Exception ignored) {
            }
        }

        @Override
        public void run() {
            while (active) {
                try {
                    StringBuilder toSend = new StringBuilder();

                    toSend.append("**").append(guildMusicManager.player.getPlayingTrack().getInfo().title).append("**").append("\n");
                    toSend.append(TrackInfo.Duration.getStringRange(guildMusicManager.player.getPlayingTrack()));

                    toUpdate.editMessage(toSend.toString()).queue(this::setButtons);
                } catch (Exception e) {
                    toUpdate.editMessage("Воспроизведение завершено.").queue();
                    removeMessage();
                    break;
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        public void removeMessage() {
            toUpdate.delete().queue();
        }
    }
}
