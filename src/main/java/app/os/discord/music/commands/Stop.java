package app.os.discord.music.commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.MusicManager;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Message;

public class Stop extends Command {
    public Stop() {
        this.name = "stop";
        this.help = "остановить проигрывание";
        this.requiredRole = DiscordBot.DJ_ROLE;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Message received = commandEvent.getMessage();

        AudioPlayer player = MusicManager.getInstance().getGuildAudioPlayer(commandEvent.getGuild()).player;
        player.stopTrack();

        received.getChannel().sendMessage("Плеер **остановлен**.").queue();
    }
}
