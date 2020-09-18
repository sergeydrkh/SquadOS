package app.os.discord.commands.tread.commons;

public final class SafeIdUtil {
    public static long safeConvert(String id) {
        try {
            long l = Long.parseLong(id.trim());
            return Math.max(l, 0L);
        } catch (NumberFormatException var3) {
            return 0L;
        }
    }

    public static boolean checkId(String id) {
        try {
            long l = Long.parseLong(id.trim());
            return l >= 0L;
        } catch (NumberFormatException var3) {
            return false;
        }
    }

    private SafeIdUtil() {
    }
}
