package app.os.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DiscordStart extends Thread {
    private static final Logger logger = LoggerFactory.getLogger("Discord");

    private Properties properties;

    public DiscordStart(String stringFile) {
        // load thread
        setName("Discord");

        // load bot
        Path propertiesFile;
        long start = System.currentTimeMillis();

        // get properties file
        logger.info("Trying to read properties file...");
        propertiesFile = Paths.get(stringFile);
        if (Files.notExists(propertiesFile)) {
            logger.error("File doesn't exists!");
            return;
        }


        // load properties
        logger.info("Loading properties...");
        properties = new Properties();
        try (InputStream in = Files.newInputStream(propertiesFile)) {
            properties.load(in);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return;
        }

        if (properties.size() != DiscordProperties.values().length) {
            logger.error("lacks properties! list of actual properties:");

            StringBuilder propertiesDiff = new StringBuilder();
            propertiesDiff.append("\n---------\nPROPERTIES LIST\n");
            for (DiscordProperties dsProperty : DiscordProperties.values()) {
                propertiesDiff.append(" > ").append(dsProperty.getKey()).append(" ");
                if (!properties.containsKey(dsProperty.getKey()))
                    propertiesDiff.append("[MISSING]");
                propertiesDiff.append("\n");
            }
            propertiesDiff.append("---------\n");

            System.out.println(propertiesDiff.toString());
            properties = null;
            return;
        }

        logger.info(String.format("Loaded successfully! (%dms)", (System.currentTimeMillis() - start)));
    }


    @Override
    public void run() {
        if (properties != null) {
            DiscordBot bot = new DiscordBot();
            bot.launch(properties);
        } else {
            logger.error("Bad properties!");
        }
    }
}
