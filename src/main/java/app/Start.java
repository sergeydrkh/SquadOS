package app;

import app.os.main.OS;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Start {
    private static final Logger logger = LoggerFactory.getLogger(Start.class.getName());

    public static void main(String[] args) {
        new Start().run();
    }

    private void run() {
        BasicConfigurator.configure(); // configure logger

        logger.info("Starting main thread...");
        new OS().start(); // start main thread
    }
}
