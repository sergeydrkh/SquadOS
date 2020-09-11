package app.os.discord.music.commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.GuildMusicManager;
import app.os.discord.music.MusicManager;
import app.os.main.OS;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class GetQueue extends Command {
    public GetQueue() {
        this.name = "queue";
        this.help = "получить очередь музыки";
        this.requiredRole = DiscordBot.DJ_ROLE;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        EmbedBuilder queueMessage = new EmbedBuilder();
        queueMessage.setColor(OS.DEFAULT_COLOR);

        StringBuilder allTracks = new StringBuilder();
        GuildMusicManager guildMusicManager = MusicManager.getInstance().getGuildAudioPlayer(commandEvent.getGuild());
        guildMusicManager.scheduler.getTracksInQueue().forEach(track -> allTracks.append(" - ").append(track.getInfo().title).append("\n"));
        queueMessage.addField("Очередь", allTracks.toString(), false);

        commandEvent.getChannel().sendMessage(queueMessage.build()).queue();

    }
}
