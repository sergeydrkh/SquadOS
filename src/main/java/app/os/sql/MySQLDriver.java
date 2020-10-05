package app.os.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQLDriver {
    private static final Logger logger = LoggerFactory.getLogger(MySQLDriver.class.getName());

    private Connection connection;
    private Statement statement;

    private final String DB_URL;
    private final String USERNAME;
    private final String PASSWORD;

    public MySQLDriver(final String DB_URL, final String USERNAME, final String PASSWORD) {
        // load params
        this.DB_URL = DB_URL;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;

        // try to connect
        long startConnecting = System.currentTimeMillis();

        logger.info("Trying connect to database...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(this.DB_URL, this.USERNAME, this.PASSWORD);
        } catch (Exception throwables) {
            logger.error(String.format("Error! Can't connect to database. Error: %s", throwables.getMessage()));
            return;
        }

        logger.info(String.format("Connected successfully in %dms", (System.currentTimeMillis() - startConnecting)));
    }
}
