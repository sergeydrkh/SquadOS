package app.os.discord.music.commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.MusicManager;
import app.os.discord.commands.tread.command.Command;
import app.os.discord.commands.tread.command.CommandEvent;

public class Skip extends Command {
    public Skip() {
        this.name = "skip";
        this.help = "проскипать текущий трек";
        this.requiredRole = DiscordBot.DJ_ROLE;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        MusicManager musicManager = MusicManager.getInstance();
        musicManager.skipTrack(commandEvent.getTextChannel());
    }
}
