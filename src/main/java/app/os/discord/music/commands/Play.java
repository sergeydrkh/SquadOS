package app.os.discord.music.commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.MusicManager;
import app.os.main.OS;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

public class Play extends Command {
    public Play() {
        this.name = "play";
        this.help = "включить музыку";
        this.arguments = "{ссылка}";
        this.requiredRole = DiscordBot.DJ_ROLE;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Message received = commandEvent.getMessage();
        String[] args = received.getContentRaw().split(OS.DEFAULT_MESSAGE_DELIMITER);
        if (args.length != 2) {
            received.getChannel().sendMessage(String.format("**Ошибка!** Используйте: %s.", this.arguments)).queue();
            return;
        }

        String link = args[1];
        MusicManager musicManager = MusicManager.getInstance();
        musicManager.loadAndPlay(received.getTextChannel(), link);
    }
}
