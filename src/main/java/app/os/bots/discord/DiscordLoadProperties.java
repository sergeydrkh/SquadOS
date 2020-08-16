package app.os.bots.discord;

public enum DiscordLoadProperties {
    BOT_USERNAME("username"),
    BOT_NAME("name"),
    BOT_TOKEN("token"),
    BOT_PREFIX("prefix"),
    MESSAGES_COLOR("msgColor"),
    DATE_FORMAT("dateFormat"),
    SERVER_NAME("serverName");

    private final String key;

    public String getKey() {
        return key;
    }

    DiscordLoadProperties(String key) {
        this.key = key;
    }
}