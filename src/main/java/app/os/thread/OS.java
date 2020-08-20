package app.os.thread;

import app.os.bots.discord.DiscordStartThread;

import java.awt.*;
import java.text.SimpleDateFormat;

public class OS {
    public static final String DIR_MAIN = "C:\\squados\\";
    public static final String DIR_DATA = DIR_MAIN + "data\\";
    public static final String DIR_CONFIGS = DIR_MAIN + "servers\\";

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    public static final Color DEFAULT_COLOR = Color.decode("#666666");

    public static final String VERSION = "0.4.9-pre_final";

    public void start() {
        // DS thread
        Thread discordThread = new DiscordStartThread();
        discordThread.start();
    }
}
