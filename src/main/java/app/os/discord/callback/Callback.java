package app.os.discord.callback;

import java.nio.file.Path;
import java.util.Properties;

public class Callback {
    private final Path file;
    private final Properties properties;

    public Callback(Path file, Properties properties) {
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
