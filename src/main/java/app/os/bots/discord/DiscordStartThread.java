package app.os.bots.discord;

import app.os.ConsoleHelper;
import app.os.OS;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DiscordStartThread extends Thread {
    @Override
    public void run() {
        Map<DiscordProperties, String> loadProp = new HashMap<>();

        try {
            Path file = Paths.get(OS.DIR_DATA + "ds.properties");
            if (Files.notExists(file))
                Files.createFile(file);

            FileReader reader = new FileReader(file.toFile());
            Properties fileProps = new Properties();
            fileProps.load(reader);

            for (Map.Entry<Object, Object> prop : fileProps.entrySet())
                for (DiscordProperties checkProp : DiscordProperties.values())
                    if (checkProp.getKey().contains((String) prop.getKey()))
                        loadProp.put(checkProp, (String) prop.getValue());

        } catch (IOException e) {
            ConsoleHelper.errln("Ошибка! " + e.getMessage());
            return;
        }

        DiscordBot bot = new DiscordBot(loadProp);
        bot.launch();
    }
}
