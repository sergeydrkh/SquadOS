package app.os.sql;

import app.os.sql.drivers.InsertData;
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
        for (Map.Entry<String, List<Object>> entry : driver.getValuesFromTable("musicQueue", new String[]{"guildID", "name", "tracks"}).entrySet()) {
            System.out.printf("columnName: %s >> %s%n", entry.getKey(), entry.getValue().toString());
        }
    }

    @Test
    void insertSet() {
        final String dbUrl = "jdbc:mysql://localhost:3306/SquadOS?useUnicode=true&serverTimezone=UTC";
        final String username = "root";
        final String password = "CthutqLjhj32";

        MySQLDriver driver = new MySQLDriver(dbUrl, username, password);
        driver.insertDataToTable("musicQueue", new InsertData(new String[]{"guildID", "name", "tracks"}, new String[]{"123456789", "lsp", "youtube.ru"}));
    }
}