package app.os.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySQLDriverTest {
    @Test
    void connectTest() {
        final String dbUrl = "jdbc:mysql://localhost:3306/SquadOS?useUnicode=true&serverTimezone=UTC";
        final String username = "root";
        final String password = "CthutqLjhj32";

        MySQLDriver driver = new MySQLDriver(dbUrl, username, password);
    }
}