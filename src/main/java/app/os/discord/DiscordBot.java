package app.os.discord;

import app.os.console.ConsoleHelper;
import app.os.discord.commands.all.admin.*;
import app.os.discord.commands.all.creator.GetConfigs;
import app.os.discord.commands.all.creator.GetGuild;
import app.os.discord.commands.all.creator.ServerState;
import app.os.discord.commands.all.users.Info;
import app.os.discord.commands.all.users.Link;
import app.os.discord.commands.all.users.Ping;
import app.os.discord.commands.tread.command.CommandClientBuilder;
import app.os.discord.configs.ConfigListener;
import app.os.discord.music.commands.*;
import app.os.discord.music.reaction.ReactionListener;
import app.os.main.OS;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.Date;

public class DiscordBot {


    // upload
    private static final String BOT_TOKEN = "";
    private static final String[] YOUTUBE_API_KEYS = new String[]{""};
    private static final String HELP_WORD = "";
    private static final String PREFIX = "";
    private static final String OWNER_ID = "";

    public static final String INVITE_URL = "https://discord.com/api/oauth2/authorize?client_id=699588783405793323&permissions=0&scope=bot";

    public static final String ADMIN_ROLES = "admin";
    public static final String WARN_ROLES = "warn";
    public static final String CREATOR_ROLE = "creator";
    public static final String DJ_ROLE = "dj";

    public void launch() {
        try {
            // load api
            JDA api = JDABuilder.createDefault(BOT_TOKEN).build();
            api.awaitReady();

            // set commands
            CommandClientBuilder commands = new CommandClientBuilder()
                    .setActivity(Activity.playing(OS.VERSION))
                    .setPrefix(PREFIX)
                    .setHelpWord(HELP_WORD)
                    .setOwnerId(OWNER_ID);

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
            commands.addCommand(new Link());

            commands.addCommand(new Play(YOUTUBE_API_KEYS[0]));
            commands.addCommand(new Skip());
            commands.addCommand(new Volume());
            commands.addCommand(new Pause());
            commands.addCommand(new Stop());
            commands.addCommand(new Queue());
//            commands.addCommand(new Repeat()); NOW WORKING
            commands.addCommand(new Player());

            api.addEventListener(new ReactionListener());
            api.addEventListener(commands.build());
            api.addEventListener(new ConfigListener());


        } catch (LoginException | InterruptedException e) {
            ConsoleHelper.errln("Ошибка! " + e + ".");
        }
    }
}