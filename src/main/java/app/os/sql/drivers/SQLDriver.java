package app.os.sql.drivers;

import java.util.List;
import java.util.Map;

public abstract class SQLDriver {
    protected final String DB_HOST;
    protected final String DB_NAME;
    protected final String USERNAME;
    protected final String PASSWORD;
    protected final String PORT;

    public SQLDriver(String DB_HOST, String DB_NAME, String USERNAME, String PASSWORD, String PORT) {
        this.DB_HOST = DB_HOST;
        this.DB_NAME = DB_NAME;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.PORT = PORT;
    }

    public abstract Map<String, List<Object>> getValuesFromTable(String tableName, String[] columns);

    public abstract boolean insertDataToTable(String tableName, InsertData insertData);
}
