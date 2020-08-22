package app.os.discord.callback;

import app.os.main.OS;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class CallbackManager {
    public static Callback generateEvent(Map<CallbackProperties, String> args) {
        if (args.size() != CallbackProperties.values().length)
            return null;

        Properties properties = new Properties();
        for (Map.Entry<CallbackProperties, String> arg : args.entrySet())
            properties.put(arg.getKey().getPropName(), arg.getValue());

        Path file;

        do
            file = Paths.get(OS.DIR_CALLBACK + properties.getProperty(CallbackProperties.ID.getPropName()) + "." + System.currentTimeMillis() + ".event");
        while (Files.exists(file));

        try {
            Files.createFile(file);
            OutputStream out = Files.newOutputStream(file);

            properties.store(out, OS.DEFAULT_DATE_FORMAT.format(new Date()));
            return new Callback(file, properties);
        } catch (IOException e) {
            System.out.println("ERR");
            return null;
        }
    }


    public static boolean createEvent(Callback callback) {
        try {
            Files.createFile(callback.getFile());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static List<Callback> getEvents() {
        List<Callback> result = new ArrayList<>();
        Path dir = Paths.get(OS.DIR_CALLBACK);

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

                    result.add(new Callback(file, temp));
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
