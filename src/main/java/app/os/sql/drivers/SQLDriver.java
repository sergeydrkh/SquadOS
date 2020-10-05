package app.os.sql.drivers;

import java.util.List;
import java.util.Map;

public abstract class SQLDriver {
    protected final String DB_URL;
    protected final String USERNAME;
    protected final String PASSWORD;

    protected SQLDriver(String db_url, String username, String password) {
        DB_URL = db_url;
        USERNAME = username;
        PASSWORD = password;
    }

    public abstract Map<String, List<Object>> getValuesFromTable(String tableName, String[] columns);

    public abstract boolean insertDataToTable(String tableName, InsertData insertData);
}
