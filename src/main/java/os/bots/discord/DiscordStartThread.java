package os.bots.discord;

import os.utils.Console;

public class DiscordStartThread extends Thread {
    @Override
    public void run() {
        Console.println("Starting discord bot...");
        DiscordBot bot = new DiscordBot();
        bot.run();
    }
}
