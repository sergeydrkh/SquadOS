package app.os.main;

import app.os.discord.DiscordStartThread;

import java.awt.*;
import java.text.SimpleDateFormat;

public class OS extends Thread {
    public static final String DIR_MAIN = "C:\\squados\\";
    public static final String DIR_DATA = DIR_MAIN + "data\\";
    public static final String DIR_CONFIGS = DIR_MAIN + "configs\\";

    public static final String DEFAULT_ARGS_DELIMITER = ",";
    public static final String DEFAULT_MESSAGE_DELIMITER = " ";
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    public static final Color DEFAULT_COLOR = Color.decode("#666666");

    public static final String VERSION = "0.5.6";

    @Override
    public void run() {
        // DS thread
        Thread discordThread = new DiscordStartThread();
        discordThread.start();
    }
}
