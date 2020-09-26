package app.os.discord;

import app.os.console.ConsoleHelper;
import app.os.discord.commands.self.CommandClientBuilder;
import app.os.discord.commands.commands.admin.*;
import app.os.discord.commands.commands.creator.ServerState;
import app.os.discord.commands.commands.users.Info;
import app.os.discord.commands.commands.users.Link;
import app.os.discord.commands.commands.users.Ping;
import app.os.discord.configs.ConfigListener;
import app.os.discord.music.commands.*;
import app.os.discord.music.reaction.ReactionListener;
import app.os.discord.music.self.AutoDisconnection;
import app.os.main.OS;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.Date;
import java.util.Properties;

public class DiscordBot {
    public static final String ADMIN_ROLES = "admin";
    public static final String WARN_ROLES = "warn";
    public static final String CREATOR_ROLE = "creator";
    public static final String DJ_ROLE = "dj";

    public void launch(Properties properties) {
        try {
            // load api
            JDA jda = JDABuilder.createDefault(properties.getProperty(DiscordProperties.BOT_TOKEN.getKey())).build();
            jda.awaitReady();

            // set commands
            CommandClientBuilder commands = new CommandClientBuilder()
                    .setActivity(Activity.playing(OS.VERSION))
                    .setPrefix(properties.getProperty(DiscordProperties.BOT_PREFIX.getKey()))
                    .setHelpWord(properties.getProperty(DiscordProperties.BOT_HELP_WORD.getKey()))
                    .setOwnerId(properties.getProperty(DiscordProperties.OWNER_ID.getKey()));

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
//            commands.addCommand(new GetGuild());
//            commands.addCommand(new GetConfigs());
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
            commands.addCommand(new Queue.DeleteQueue());
            commands.addCommand(new Queue.PlayQueue());
            commands.addCommand(new Queue.SaveQueue());

            jda.addEventListener(new ReactionListener());
            jda.addEventListener(commands.build());
            jda.addEventListener(new ConfigListener());

            new AutoDisconnection(jda).start();
        } catch (LoginException | InterruptedException e) {
            ConsoleHelper.errln("Ошибка! " + e + ".");
        }
    }
}