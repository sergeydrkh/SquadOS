package app.os.bots.discord;

import app.os.thread.OS;
import app.os.utilities.ConsoleHelper;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DiscordStartThread extends Thread {
    @Override
    public void run() {
        Map<DiscordProperties, String> loadProp = new HashMap<>();

        try {
            Path file = Paths.get(OS.DIR_DATA + "ds.properties");
            if (Files.notExists(file)) {
                Files.createFile(file);
                Properties create = new Properties();
                for (DiscordProperties createProp : DiscordProperties.values())
                    create.put(createProp.getKey(), "insert data");

                create.store(new FileWriter(file.toFile()), "insert data to use!");

                ConsoleHelper.println("Заполните поля файла " + file.toString() + " для использования бота!");
                return;
            } else {
                FileReader reader = new FileReader(file.toFile());
                Properties fileProps = new Properties();
                fileProps.load(reader);

                for (Map.Entry<Object, Object> prop : fileProps.entrySet())
                    for (DiscordProperties checkProp : DiscordProperties.values())
                        if (checkProp.getKey().contains((String) prop.getKey()))
                            loadProp.put(checkProp, (String) prop.getValue());
            }
        } catch (IOException e) {
            ConsoleHelper.errln("Ошибка! " + e);
            ConsoleHelper.println("Запустите install.bat для корректной работы!");
            return;
        }

        DiscordBot bot = new DiscordBot(loadProp);
        bot.launch();
    }
}
