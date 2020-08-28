package app.os.discord.server_config;

import app.os.main.OS;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigManager {
    private static final String FILE_EXTENSION = ".properties";

    public static List<Properties> getAllConfigs() {
        List<Properties> result = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get(OS.DIR_CONFIGS), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Properties temp = new Properties();
                    temp.load(Files.newInputStream(file));

                    result.add(temp);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            return result;
        }

        return result;
    }

    public static boolean isExists(long guildID) {
        for (Properties config : getAllConfigs())
            if (Long.parseLong(config.getProperty("guildID")) == guildID)
                return true;

        return false;
    }

    public static void createConfig(long guildID) {
        if (!isExists(guildID)) {
            try {
                Path newFile = Files.createFile(Paths.get(OS.DIR_CONFIGS + guildID + FILE_EXTENSION));
                Properties toStore = new Properties();
                toStore.setProperty(ConfigProperties.GUILD_ID.getKey(), String.valueOf(guildID));

                toStore.store(Files.newOutputStream(newFile), "generated");
            } catch (IOException ignored) {
            }
        }
    }

    public static boolean saveConfig(Properties config) {
        try(OutputStream out = Files.newOutputStream(Paths.get(OS.DIR_CONFIGS + config.getProperty(ConfigProperties.GUILD_ID.getKey() + FILE_EXTENSION)))) {
            config.store(out, "saved");
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
