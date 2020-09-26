package app;

import app.os.main.OS;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Start {
    private static final Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {
        new Start().run(args);
    }

    private void run(String[] args) {
        BasicConfigurator.configure(); // configure logger

        logger.info("Starting main thread...");
        new OS(args).start(); // start main thread
    }
}
