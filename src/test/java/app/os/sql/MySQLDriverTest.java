package app.os.sql;

import app.os.sql.drivers.MySQLDriver;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class MySQLDriverTest {
    @Test
    void connectTest() {
        final String dbUrl = "jdbc:mysql://localhost:3306/SquadOS?useUnicode=true&serverTimezone=UTC";
        final String username = "root";
        final String password = "CthutqLjhj32";

        MySQLDriver driver = new MySQLDriver(dbUrl, username, password);
        for (Map.Entry<String, List<Object>> entry : driver.getValuesFromTable("usersTest", new String[]{"id", "name", "email"}).entrySet()) {
            System.out.printf("columnName: %s >> %s%n", entry.getKey(), entry.getValue().toString());
        }
    }
}