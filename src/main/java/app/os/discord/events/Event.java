package app.os.discord.events;

import java.nio.file.Path;
import java.util.Properties;

public class Event {
    private final Path file;
    private final Properties properties;

    public Event(Path file, Properties properties) {
        this.file = file;
        this.properties = properties;
    }

    public Path getFile() {
        return file;
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "Event{" +
                "file=" + file.toString() +
                ", properties=" + properties.toString() +
                '}';
    }
}
