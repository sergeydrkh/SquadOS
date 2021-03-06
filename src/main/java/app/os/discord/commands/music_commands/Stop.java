package app.os.discord.commands.music_commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.player.GuildMusicManager;
import app.os.discord.music.player.MusicManager;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

public class Stop extends Command {
    public Stop() {
        this.name = "stop";
        this.help = "остановить проигрывание";
        this.requiredRole = DiscordBot.DJ_ROLE;
        this.cooldown = 1;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Message received = commandEvent.getMessage();

        GuildMusicManager guildMusicManager = MusicManager.getInstance().getGuildAudioPlayer(commandEvent.getGuild());
        guildMusicManager.scheduler.clearQueue();
        guildMusicManager.player.stopTrack();

        received.getChannel().sendMessage("Очередь очищена.").queue();
    }
}
