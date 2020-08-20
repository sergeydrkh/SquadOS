package app.os.bots.discord;

import app.os.bots.discord.commands.admin.*;
import app.os.bots.discord.commands.users.Info;
import app.os.bots.discord.commands.users.Ping;
import app.os.thread.OS;
import app.os.utilities.ConsoleHelper;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.Map;

public class DiscordBot {
    private final Map<DiscordProperties, String> loadProperties;

    public DiscordBot(Map<DiscordProperties, String> loadProperties) {
        this.loadProperties = loadProperties;
    }

    public static final String ADMIN_ROLES = "admin";
    public static final String WARN_ROLES = "warn";

    public void launch() {
        try {
            // load api
            JDA api = JDABuilder.createDefault(loadProperties.get(DiscordProperties.BOT_TOKEN)).build();
            api.awaitReady();

            // set commands
            CommandClientBuilder commands = new CommandClientBuilder()
                    .setActivity(Activity.playing(OS.VERSION))
                    .setPrefix(loadProperties.get(DiscordProperties.BOT_PREFIX_MAIN))
                    .setAlternativePrefix(loadProperties.get(DiscordProperties.BOT_PREFIX_ADDITIONAL))
                    .setHelpWord(loadProperties.get(DiscordProperties.BOT_HELP_WORD))
                    .setOwnerId(loadProperties.get(DiscordProperties.OWNER_ID));

            commands.addCommand(new Info());
            commands.addCommand(new Ping());
            commands.addCommand(new TextChannels());
            commands.addCommand(new VoiceChannels());
            commands.addCommand(new Send());
            commands.addCommand(new Admins());
            commands.addCommand(new Warn());
            commands.addCommand(new UnWarn());
            commands.addCommand(new Ban());
            commands.addCommand(new Mute());
            commands.addCommand(new UnMute());

            api.addEventListener(commands.build());
        } catch (LoginException | InterruptedException e) {
            ConsoleHelper.errln("Ошибка! " + e + ".");
        }
    }
}