package app.os.discord.music.commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.thread.GuildMusicManager;
import app.os.discord.music.thread.MusicManager;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;

public class Repeat extends Command {
    public Repeat() {
        this.name = "repeat";
        this.help = "повторять текущий трек // в разработке";
        this.requiredRole = DiscordBot.DJ_ROLE;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        GuildMusicManager guildMusicManager = MusicManager.getInstance().getGuildAudioPlayer(commandEvent.getGuild());

        boolean repeat = !guildMusicManager.scheduler.isRepeat();
        guildMusicManager.scheduler.setRepeat(repeat);
        commandEvent.getChannel().sendMessage(repeat ? "Повторение включено!" : "Повторение выключено!").queue();
    }
}
