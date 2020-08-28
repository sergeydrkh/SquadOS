package app.os.discord.server_config;

public enum ConfigProperties {
    GUILD_ID("guildID");

    private final String key;

    public String getKey() {
        return key;
    }

    ConfigProperties(String key) {
        this.key = key;
    }
}
