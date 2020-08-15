package app.os.bots.discord;

public enum DiscordProperties {
    BOT_USERNAME("username"),
    BOT_NAME("name"),
    BOT_TOKEN("token"),
    BOT_PREFIX("prefix"),
    BOT_ADDITIONAL_PREFIX("additionalPrefix");

    private final String key;

    public String getKey() {
        return key;
    }

    DiscordProperties(String key) {
        this.key = key;
    }
}