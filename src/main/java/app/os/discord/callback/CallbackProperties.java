package app.os.discord.callback;

public enum CallbackProperties {
    ID("guildID"),
    TYPE("type"),
    CONTENT("content");

    private final String propName;

    CallbackProperties(String propName) {
        this.propName = propName;
    }

    public String getPropName() {
        return propName;
    }
}
