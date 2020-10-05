package app.os.main;

import app.os.console.ConsoleListener;
import app.os.discord.DiscordStart;

import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OS extends Thread {
    public static final String DIR_MAIN = File.listRoots()[0].getAbsolutePath() + "squados\\";
    public static final String DIR_DATA = DIR_MAIN + "data\\";
    public static final String DIR_CONFIGS = DIR_MAIN + "configs\\";

    public static final String DEFAULT_ARGS_DELIMITER = ",";
    public static final String DEFAULT_MESSAGE_DELIMITER = " ";
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    public static final Color DEFAULT_COLOR = Color.decode("#666666");

    public static final Date DATE_LAUNCH = new Date();
    public static final String NAME = "SquadOS";
    public static final String VERSION = "0.7.5";
    public static final String DESCRIPTION = "SquadOS - простой и лёгкий в использовании многофункциональный Discord бот. Музыка, модерация и веселье - легко!";

    private final String[] args;

    public OS(String[] args) {
        this.args = args;
    }

    @Override
    public void run() {
        new ConsoleListener().start();
        new DiscordStart(args[0]).start();
    }
}
