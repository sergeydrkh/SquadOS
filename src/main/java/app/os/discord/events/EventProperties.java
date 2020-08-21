package app.os.discord.events;

public enum EventProperties {
    ID("guildID"),
    TYPE("type"),
    CONTENT("content");

    private final String propName;

    EventProperties(String propName) {
        this.propName = propName;
    }

    public String getPropName() {
        return propName;
    }
}
