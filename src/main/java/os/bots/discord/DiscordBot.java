package os.bots.discord;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import os.utils.Console;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private static final String TOKEN = "Njk5NTg4NzgzNDA1NzkzMzIz.XvN3vw.8qP5htUlzBVxo3QrCM9DAFRaLQM";

    public void run() {
        try {
            JDA api = new JDABuilder(AccountType.BOT)
                    .setToken(TOKEN)
                    .build();

            Console.println("DS >> Successfully logged in!");
        } catch (LoginException e) {
            Console.errln("Ошибка! " + e + ".");
        }
    }
}
