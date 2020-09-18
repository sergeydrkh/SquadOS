package app.os.discord.music.commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.MusicManager;
import app.os.discord.commands.tread.command.Command;
import app.os.discord.commands.tread.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Message;

public class Pause extends Command {
    public Pause() {
        this.name = "pause";
        this.help = "поставить на паузу";
        this.requiredRole = DiscordBot.DJ_ROLE;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Message received = commandEvent.getMessage();

        AudioPlayer player = MusicManager.getInstance().getGuildAudioPlayer(commandEvent.getGuild()).player;
        boolean paused = !player.isPaused();
        player.setPaused(paused);

        received.getChannel().sendMessage(!paused ? "Плеер **снят** с паузы." : "Плеер **поставлен** на паузу.").queue();
    }
}
