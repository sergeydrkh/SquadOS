package app.os.discord.configs;

import app.os.main.OS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigManager {
    private static final String FILE_EXTENSION = ".properties";
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class.getName());

    public static Properties getConfigByID(long guildID) {
        for (Properties config : getAllConfigs()) {
            if (Long.parseLong(config.getProperty(ConfigProperties.GUILD_ID.getKey())) == guildID) {
                return config;
            }
        }

        return createConfig(guildID);
    }

    public static List<Properties> getAllConfigs() {
        List<Properties> result = new ArrayList<>();

        createPath(OS.DIR_MAIN);
        createPath(OS.DIR_CONFIGS);

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
            if (Long.parseLong(config.getProperty(ConfigProperties.GUILD_ID.getKey())) == guildID)
                return true;

        return false;
    }

    public static Properties createConfig(long guildID) {
        createPath(OS.DIR_MAIN);
        createPath(OS.DIR_CONFIGS);

        if (!isExists(guildID)) {
            try {
                Path newFile = Files.createFile(Paths.get(OS.DIR_CONFIGS + guildID + FILE_EXTENSION));
                Properties toStore = new Properties();
                toStore.setProperty(ConfigProperties.GUILD_ID.getKey(), String.valueOf(guildID));

                toStore.store(Files.newOutputStream(newFile), "generated");
                return toStore;
            } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        return null;
    }

    private static void createPath(String dir) {
        Path root = Paths.get(dir);
        if (Files.notExists(root)) {
            try {
                Files.createDirectory(root);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static boolean saveConfig(Properties config) {
        createPath(OS.DIR_MAIN);
        createPath(OS.DIR_CONFIGS);
        Path toSave = Paths.get(OS.DIR_CONFIGS + config.getProperty(ConfigProperties.GUILD_ID.getKey()) + FILE_EXTENSION);
        try (OutputStream out = Files.newOutputStream(toSave)) {
            config.store(out, "saved");

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
