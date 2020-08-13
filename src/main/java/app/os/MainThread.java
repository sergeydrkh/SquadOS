package app.os;

import app.os.bots.discord.DiscordStartThread;

public class MainThread extends Thread {
    public static final String MAIN_WEBSITE_URL = "test.com";

    @Override
    public void start() {
        // DS thread
        Thread discordThread = new DiscordStartThread();
        discordThread.start();
    }
}
