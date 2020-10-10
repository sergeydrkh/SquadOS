package app.os.discord;

public enum DiscordProperties {
    BOT_TOKEN("api_token"),
    BOT_PREFIX("main_prefix"),
    BOT_HELP_WORD("help_word"),
    OWNER_ID("owner_id"),
    YOUTUBE_API_KEY("youtube_apiKey"),
    INVITE_URL("url_botInvite"),
    DB_HOSTNAME("db_hostname"),
    DB_PORT("db_port"),
    DB_REGION("db_region"),
    DB_USER("db_user"),
    DB_TRACKS_TABLE("db_tracks_table_name");

    private final String key;

    public String getKey() {
        return key;
    }

    DiscordProperties(String key) {
        this.key = key;
    }
}