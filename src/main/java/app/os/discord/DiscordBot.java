package app.os.discord;

import app.os.discord.commands.bot_commands.admin.*;
import app.os.discord.commands.bot_commands.creator.GetConfigs;
import app.os.discord.commands.bot_commands.creator.GetGuild;
import app.os.discord.commands.bot_commands.creator.ServerState;
import app.os.discord.commands.bot_commands.users.Info;
import app.os.discord.commands.bot_commands.users.Link;
import app.os.discord.commands.bot_commands.users.Ping;
import app.os.discord.commands.command.CommandClientBuilder;
import app.os.discord.commands.music_commands.*;
import app.os.discord.configs.ConfigListener;
import app.os.discord.music.reactions.ReactionListener;
import app.os.discord.music.player.AutoDisconnection;
import app.os.main.OS;
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

    public void launch(Properties properties) {
        try {
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
            commands.addCommand(new ServerState(new Date()));
            commands.addCommand(new Link(properties.getProperty(DiscordProperties.INVITE_URL.getKey())));

            commands.addCommand(new Play(properties.getProperty(DiscordProperties.YOUTUBE_API_KEY.getKey())));
            commands.addCommand(new Skip());
            commands.addCommand(new Volume());
            commands.addCommand(new Pause());
            commands.addCommand(new Stop());
//            commands.addCommand(new Repeat()); NOT WORKING (in progress)
            commands.addCommand(new Player());
            commands.addCommand(new Queue.GetQueue());
            commands.addCommand(new Queue.RemoveTrack());
            commands.addCommand(new Queue.DeleteQueue());
            commands.addCommand(new Queue.PlayQueue());
            commands.addCommand(new Queue.SaveQueue());

            // add listeners
            jda.addEventListener(new ReactionListener());
            jda.addEventListener(commands.build());
            jda.addEventListener(new ConfigListener());

            new AutoDisconnection(jda).start();
        } catch (LoginException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}