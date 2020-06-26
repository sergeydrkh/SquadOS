package os.bots.discord;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import os.bots.discord.dsCommands.DS_Help;
import os.bots.discord.dsCommands.DS_Info;
import os.bots.discord.dsCommands.DS_Register;
import os.exceptions.NoConnectionWithDiscordException;
import os.utils.Console;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.stream.Collectors;

public class DiscordBot {
    public static final String NAME = "SquadOS";
    public static final String VERSION = "0.1.1_beta";
    public static final String OWNER_ID = "662324806187745290";

    private static final String TOKEN = "Njk5NTg4NzgzNDA1NzkzMzIz.XvN3vw.8qP5htUlzBVxo3QrCM9DAFRaLQM";

    private static JDA api;

    public void run() {
        try {
            // authorize bot
            api = new JDABuilder(AccountType.BOT)
                    .setToken(TOKEN)
                    .build();
            Console.println("DS >> Successfully logged in!");

            // build commands listener
            CommandClientBuilder commands = new CommandClientBuilder()
                    .setOwnerId(OWNER_ID)
                    .setPrefix("$")
                    .setAlternativePrefix("/")
                    .setHelpWord("help");

            commands.addCommand(new DS_Help());         // help command
            commands.addCommand(new DS_Info());         // info command
            commands.addCommand(new DS_Register());     // register command

            // add listener to api
            api.addEventListener(commands.build());
        } catch (LoginException e) {
            Console.errln("Ошибка! " + e + ".");
        }
    }

    // >> BOT UTILS <<

    /**
     * @return
     * @throws NoConnectionWithDiscordException
     * @action get online users on server, if jda isn't loaded throws exception
     */
    public static List<Member> getOnlineUsers() throws NoConnectionWithDiscordException {
        try {
            return api.getGuilds().get(0).getMembers().stream().filter(user -> user.getOnlineStatus() == OnlineStatus.ONLINE).collect(Collectors.toList());
        } catch (java.lang.NullPointerException e) {
            throw new NoConnectionWithDiscordException();
        }
    }
}
