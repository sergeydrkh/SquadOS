package app.os.discord.events;

public enum EventType {
    TIME("time"),
    MESSAGE("message");


    private final String eventName;

    EventType(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
