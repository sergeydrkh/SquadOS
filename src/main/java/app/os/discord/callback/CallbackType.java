package app.os.discord.callback;

public enum CallbackType {
    TIME("time"),
    MESSAGE("message");


    private final String eventName;

    CallbackType(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
