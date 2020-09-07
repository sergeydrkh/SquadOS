package app.os.discord.configs;

public enum ConfigProperties {
    GUILD_ID("guildID"),
    BAN_WORDS_LIST("banWords_list"),
    BAN_WORDS_STATE("banWords_state");

    private final String key;

    public String getKey() {
        return key;
    }

    ConfigProperties(String key) {
        this.key = key;
    }
}
