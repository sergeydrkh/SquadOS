package app.os.discord.commands.music_commands;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import app.os.discord.configs.ConfigManager;
import app.os.discord.configs.ConfigProperties;
import app.os.discord.music.player.GetTrack;
import app.os.discord.music.player.MusicManager;
import app.os.main.OS;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class Play extends Command {
    private final String googleApiKey;

    public Play(String googleApiKey) {
        this.googleApiKey = googleApiKey;

        this.name = "play";
        this.help = "включить музыку";
        this.arguments = "{ссылка/название}";
        this.requiredRole = DiscordBot.DJ_ROLE;
        this.cooldown = 1;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Message received = commandEvent.getMessage();
        String[] args = received.getContentRaw().split(OS.DEFAULT_MESSAGE_DELIMITER);
        if (args.length < 2) {
            received.getChannel().sendMessage(String.format("**Ошибка!** Используйте: ``%s %s``.", this.name, this.arguments)).queue();
            return;
        }

        new Thread(new Task(received, args, commandEvent.getGuild())).start();
    }

    private class Task implements Runnable {
        private final Message received;
        private final String[] args;
        private final Guild guild;

        public Task(Message received, String[] args, Guild guild) {
            this.received = received;
            this.args = args;
            this.guild = guild;
        }

        @Override
        public void run() {
            MusicManager musicManager = MusicManager.getInstance();

            int volume;
            try {
                volume = Integer.parseInt(ConfigManager.getConfigByID(guild.getIdLong()).getProperty(ConfigProperties.PLAYER_VOLUME.getKey()));
            } catch (Exception ignored) {
                volume = musicManager.getGuildAudioPlayer(guild).player.getVolume();
            }

            // create player
            musicManager.getGuildAudioPlayer(guild).player.setVolume(volume);
            musicManager.loadAndPlay(received.getTextChannel(), true, GetTrack.getLink(args[1], googleApiKey));
        }
    }
}
