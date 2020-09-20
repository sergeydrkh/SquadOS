package app.os.main;

import app.os.console.ConsoleListener;
import app.os.discord.DiscordStartThread;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OS extends Thread {
    public static final String DIR_MAIN = "C:\\squados\\";
    public static final String DIR_DATA = DIR_MAIN + "data\\";
    public static final String DIR_CONFIGS = DIR_MAIN + "configs\\";

    public static final String DEFAULT_ARGS_DELIMITER = ",";
    public static final String DEFAULT_MESSAGE_DELIMITER = " ";
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    public static final Color DEFAULT_COLOR = Color.decode("#666666");

    public static final Date DATE_LAUNCH = new Date();
    public static final String NAME = "SquadOS";
    public static final String VERSION = "0.6.7";
    public static final String DESCRIPTION = "SquadOS - простой и лёгкий в использовании многофункциональный Discord бот. Музыка, модерация и веселье - легко!";

    @Override
    public void run() {
        new ConsoleListener().start();
        new DiscordStartThread().start();
    }
}
