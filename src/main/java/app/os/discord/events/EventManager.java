package app.os.discord.events;

import app.os.main.OS;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class EventManager {
    public static Event generateEvent(Map<EventProperties, String> args) {
        if (args.size() != EventProperties.values().length)
            return null;

        Properties properties = new Properties();
        for (Map.Entry<EventProperties, String> arg : args.entrySet())
            properties.put(arg.getKey().getPropName(), arg.getValue());

        Path file;

        do
            file = Paths.get(OS.DIR_EVENTS + properties.getProperty(EventProperties.ID.getPropName()) + "." + System.currentTimeMillis() + ".event");
        while (Files.exists(file));

        try {
            Files.createFile(file);
            OutputStream out = Files.newOutputStream(file);

            properties.store(out, OS.DEFAULT_DATE_FORMAT.format(new Date()));
            return new Event(file, properties);
        } catch (IOException e) {
            System.out.println("ERR");
            return null;
        }
    }


    public static boolean createEvent(Event event) {
        try {
            Files.createFile(event.getFile());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static List<Event> getEvents() {
        List<Event> result = new ArrayList<>();
        Path dir = Paths.get(OS.DIR_EVENTS);

        try {
            Files.walkFileTree(dir, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Properties temp = new Properties();
                    temp.load(Files.newInputStream(file));

                    result.add(new Event(file, temp));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            return result;
        }

        return result;
    }
}
