package app.os.main;

import app.os.console.ConsoleListener;
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

    public static final String NAME = "SquadOS";
    public static final String VERSION = "0.6-beta";
    public static final String DESCRIPTION = "SquadOS - a simple and easy to use multifunctional Discord bot. Music, moderation, fun - ez!";

    @Override
    public void run() {
        new ConsoleListener().start();
        new DiscordStartThread().start();
    }
}
