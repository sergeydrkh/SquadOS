package app.os.main;

import app.os.bots.discord.DiscordStartThread;

public class OS {
    public static final String DIR_MAIN = "C:\\squados\\";
    public static final String DIR_DATA = DIR_MAIN + "data\\";

    public static final String VERSION = "0.3.7-stable";

    public void start() {
        // DS thread
        Thread discordThread = new DiscordStartThread();
        discordThread.start();
    }
}
