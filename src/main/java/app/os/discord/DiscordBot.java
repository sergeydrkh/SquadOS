package app.os.discord;

import app.os.discord.commands.bot_commands.admin.*;
import app.os.discord.commands.bot_commands.creator.GetConfigs;
import app.os.discord.commands.bot_commands.creator.GetGuild;
import app.os.discord.commands.bot_commands.users.ChangeNickname;
import app.os.discord.commands.bot_commands.users.Info;
import app.os.discord.commands.command.CommandClientBuilder;
import app.os.discord.commands.music_commands.*;
import app.os.discord.configs.ConfigListener;
import app.os.discord.music.reactions.ReactionListener;
import app.os.main.OS;
import app.os.sql.drivers.MySQLDriver;
import app.os.sql.drivers.SQLDriver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Date;
import java.util.Properties;

public class DiscordBot {
    private static final Logger logger = LoggerFactory.getLogger(DiscordBot.class.getName());

    public static final String ADMIN_ROLES = "admin";
    public static final String WARN_ROLES = "warn";
    public static final String CREATOR_ROLE = "creator";
    public static final String DJ_ROLE = "dj";

    public static SQLDriver sqlDriver;

    public void launch(Properties properties) {
        try {
            // load database
            sqlDriver = new MySQLDriver(
                    properties.getProperty(DiscordProperties.DB_NAME.getKey()),
                    properties.getProperty(DiscordProperties.DB_HOST.getKey()),
                    properties.getProperty(DiscordProperties.DB_USER.getKey()),
                    properties.getProperty(DiscordProperties.DB_PASS.getKey()),
                    properties.getProperty(DiscordProperties.DB_PORT.getKey())
            );

            // load api
            JDA jda = JDABuilder.createDefault(properties.getProperty(DiscordProperties.BOT_TOKEN.getKey())).build();
            jda.awaitReady();

            // create command builder
            CommandClientBuilder commands = new CommandClientBuilder()
                    .setActivity(Activity.listening(OS.VERSION))
                    .setPrefix(properties.getProperty(DiscordProperties.BOT_PREFIX.getKey()))
                    .setHelpWord(properties.getProperty(DiscordProperties.BOT_HELP_WORD.getKey()))
                    .setOwnerId(properties.getProperty(DiscordProperties.OWNER_ID.getKey()));

            // add commands
            commands.addCommand(new Info.InfoCard());
            commands.addCommand(new Info.Ping());
            commands.addCommand(new Info.ServerState(new Date()));
            commands.addCommand(new Info.BotLink(properties.getProperty(DiscordProperties.INVITE_URL.getKey())));
            commands.addCommand(new Mute.GiveMute());
            commands.addCommand(new Mute.RemoveMute());
            commands.addCommand(new Channels.Send());
            commands.addCommand(new Channels.Clear());
            commands.addCommand(new Channels.TextChannels());
            commands.addCommand(new Channels.VoiceChannels());
            commands.addCommand(new Warn.GiveWarn());
            commands.addCommand(new Warn.RemoveWarn());
            commands.addCommand(new Admins());
            commands.addCommand(new Ban());
            commands.addCommand(new GetGuild());
            commands.addCommand(new GetConfigs());
            commands.addCommand(new ChangeNickname());

            commands.addCommand(new Play(properties.getProperty(DiscordProperties.YOUTUBE_API_KEY.getKey())));
            commands.addCommand(new Skip());
            commands.addCommand(new Volume());
            commands.addCommand(new Pause());
            commands.addCommand(new Stop());
            commands.addCommand(new RemoveTrack());
            commands.addCommand(new Repeat());
            commands.addCommand(new Player());

            String tracksTable = properties.getProperty(DiscordProperties.DB_TRACKS_TABLE.getKey());
            String googleApiKey = properties.getProperty(DiscordProperties.YOUTUBE_API_KEY.getKey());

            commands.addCommand(new TrackQueue.GetQueue());
            commands.addCommand(new TrackQueue.RemoveQueue(sqlDriver, tracksTable));
            commands.addCommand(new TrackQueue.DeleteQueue(sqlDriver, tracksTable));
            commands.addCommand(new TrackQueue.PlayQueue(sqlDriver, tracksTable, googleApiKey));
            commands.addCommand(new TrackQueue.SaveQueue(sqlDriver, tracksTable, googleApiKey));
            commands.addCommand(new Disconnect());

            // add listeners
            jda.addEventListener(new ReactionListener());
            jda.addEventListener(commands.build());
            jda.addEventListener(new ConfigListener());

//            new AutoDisconnection(jda).start();
        } catch (LoginException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}