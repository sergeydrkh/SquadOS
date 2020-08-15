package app.os.utilities;

import java.util.Date;

public class ConsoleHelper {
    private static final String style = "%tT [%s] >> ";

    public static void println(String text) {
        System.out.println(String.format(style + text, new Date(), "INFO"));
    }

    public static void print(String text) {
        System.out.print(String.format(style + text, new Date(), "INFO"));
    }

    public static void errln(String text) {
        System.err.println(String.format(style + text, new Date(), "ERROR"));
    }

    public static void err(String text) {
        System.err.print(String.format(style + text, new Date(), "ERROR"));
    }
}
