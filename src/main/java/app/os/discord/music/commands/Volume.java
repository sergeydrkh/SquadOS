package app.os.discord.music.commands;

import app.os.discord.DiscordBot;
import app.os.discord.configs.ConfigManager;
import app.os.discord.configs.ConfigProperties;
import app.os.discord.music.self.MusicManager;
import app.os.main.OS;
import app.os.discord.commands.self.Command;
import app.os.discord.commands.self.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Message;

import java.util.Properties;

public class Volume extends Command {
    public Volume() {
        this.name = "volume";
        this.help = "изменить громкость";
        this.arguments = "{громкость}";
        this.requiredRole = DiscordBot.DJ_ROLE;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Message received = commandEvent.getMessage();
        String[] args = received.getContentRaw().split(OS.DEFAULT_MESSAGE_DELIMITER);

        if (args.length == 1) {
            AudioPlayer player = MusicManager.getInstance().getGuildAudioPlayer(commandEvent.getGuild()).player;
            received.getChannel().sendMessage(String.format("Текущая громкость: **%d%%**", player.getVolume())).queue();
            return;
        } else if (args.length != 2) {
            received.getChannel().sendMessage(String.format("**Ошибка!** Используйте: %s.", this.arguments)).queue();
            return;
        }

        try {
            AudioPlayer player = MusicManager.getInstance().getGuildAudioPlayer(commandEvent.getGuild()).player;

            int newVolume = Integer.parseInt(args[1]);
            int oldVolume = player.getVolume();

            try {
                Properties config = ConfigManager.getConfigByID(commandEvent.getGuild().getIdLong());
                config.setProperty(ConfigProperties.PLAYER_VOLUME.getKey(), String.valueOf(newVolume));

                ConfigManager.saveConfig(config);
            } catch (Exception ignored) {
            }

            player.setVolume(newVolume);
            received.getChannel().sendMessage(String.format("Громкость изменена с **%d%%** на **%d%%**.", oldVolume, newVolume)).queue();
        } catch (NumberFormatException e) {
            received.getChannel().sendMessage("**Ошибка!** Вы ввели не число!").queue();
            return;
        }
    }
}
