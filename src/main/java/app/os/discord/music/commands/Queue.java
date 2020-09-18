package app.os.discord.music.commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.GuildMusicManager;
import app.os.discord.music.MusicManager;
import app.os.main.OS;
import app.os.discord.commands.tread.command.Command;
import app.os.discord.commands.tread.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;

public class Queue extends Command {
    public Queue() {
        this.name = "queue";
        this.help = "получить очередь музыки";
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
            for (AudioTrack track :  guildMusicManager.scheduler.getTracksInQueue()) {
                i++;
                allTracks.append(" - ").append(i).append(". ").append(track.getInfo().title).append("\n");
            }

            queueMessage.addField("Играет", " - " + guildMusicManager.player.getPlayingTrack().getInfo().title, false);
            if (!allTracks.toString().equals(""))
                queueMessage.addField("Очередь", allTracks.toString(), false);

            commandEvent.getChannel().sendMessage(queueMessage.build()).queue();
        } catch (NullPointerException e) {
            commandEvent.getChannel().sendMessage("Очередь пуста.").queue();
        }

    }
}
