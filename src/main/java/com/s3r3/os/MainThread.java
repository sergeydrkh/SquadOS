package com.s3r3.os;

import com.s3r3.os.bots.discord.DiscordStartThread;

public class MainThread extends Thread {
    public static final String MAIN_WEBSITE_URL = "test.com";

    @Override
    public void start() {
        // DS thread
        Thread discordThread = new DiscordStartThread();
        discordThread.start();
    }
}
