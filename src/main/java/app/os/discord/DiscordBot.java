package app.os.discord;

import app.os.discord.commands.admin.*;
import app.os.discord.commands.creator.GetConfigs;
import app.os.discord.commands.creator.GetGuild;
import app.os.discord.commands.creator.ServerState;
import app.os.discord.commands.users.Info;
import app.os.discord.commands.users.Ping;
import app.os.discord.configs.ConfigListener;
import app.os.discord.music.commands.Play;
import app.os.discord.music.commands.Skip;
import app.os.main.OS;
import app.os.server.Server;
import app.os.utilities.ConsoleHelper;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.Date;
import java.util.Map;

public class DiscordBot {
    private final Map<DiscordProperties, String> loadProperties;

    public DiscordBot(Map<DiscordProperties, String> loadProperties) {
        this.loadProperties = loadProperties;
    }

    public static final String ADMIN_ROLES = "admin";
    public static final String WARN_ROLES = "warn";
    public static final String CREATOR_ROLE = "creator";
    public static final String DJ_ROLE = "dj";

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
            commands.addCommand(new Clear());
            commands.addCommand(new GetGuild());
            commands.addCommand(new GetConfigs());
            commands.addCommand(new BanWords());
            commands.addCommand(new ServerState(new Date()));

            commands.addCommand(new Play());
            commands.addCommand(new Skip());

            api.addEventListener(commands.build());
            api.addEventListener(new ConfigListener());

            // start server

            try {
                if (Boolean.parseBoolean(loadProperties.get(DiscordProperties.SERVER_ENABLE))) {
                    Thread server = new Server(
                            loadProperties.get(DiscordProperties.SERVER_IP),
                            Integer.parseInt(loadProperties.get(DiscordProperties.SERVER_PORT)),
                            api
                    );

                    server.start();
                }
            } catch (Exception e) {
                ConsoleHelper.errln("Произошла ошибка при создании сервера! Он будет отключен до следующего перезапуска. Ошибка: " + e.getMessage());
            }


        } catch (LoginException | InterruptedException e) {
            ConsoleHelper.errln("Ошибка! " + e + ".");
        }
    }
}