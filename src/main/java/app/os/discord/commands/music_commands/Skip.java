package app.os.discord.commands.music_commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.player.MusicManager;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;

public class Skip extends Command {
    public Skip() {
        this.name = "skip";
        this.help = "проскипать текущий трек";
        this.requiredRole = DiscordBot.DJ_ROLE;
        this.cooldown = 1;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        MusicManager musicManager = MusicManager.getInstance();
        musicManager.skipTrack(commandEvent.getTextChannel());
    }
}
