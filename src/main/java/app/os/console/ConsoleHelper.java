package app.os.console;

import java.util.Date;

public class ConsoleHelper {
    private static int errors = 0;

    private static final String style = "%s [%s] >> %s";

    public static void println(String text) {
        System.out.println(String.format(style, "INFO", new Date(), text));
    }

    public static void print(String text) {
        System.out.print(String.format(style, text, new Date(), "INFO"));
    }

    public static void errln(String text) {
        System.err.println(String.format(style, text, new Date(), "ERROR"));
        errors++;
    }

    public static void err(String text) {
        System.err.print(String.format(style, text, new Date(), "ERROR"));
        errors++;
    }

    public static int getErrors() {
        return errors;
    }
}
