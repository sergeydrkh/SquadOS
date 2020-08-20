package app.os.bots.discord;

public enum DiscordProperties {
    BOT_TOKEN("token"),
    BOT_PREFIX_MAIN("mainPrefix"),
    BOT_PREFIX_ADDITIONAL("additionalPrefix"),
    BOT_HELP_WORD("helpWord"),
    OWNER_ID("ownerId");

    private final String key;

    public String getKey() {
        return key;
    }

    DiscordProperties(String key) {
        this.key = key;
    }
}