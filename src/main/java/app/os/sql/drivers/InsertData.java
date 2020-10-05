package app.os.sql.drivers;

public class InsertData {
    private final String[] columns;
    private final String[] data;

    public InsertData(String[] columns, String[] data) {
        this.columns = columns;
        this.data = data;
    }

    public String[] getColumns() {
        return columns;
    }

    public String[] getData() {
        return data;
    }
}
