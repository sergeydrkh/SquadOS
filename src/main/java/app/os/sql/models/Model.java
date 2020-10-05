package app.os.sql.models;

import app.os.sql.drivers.SQLDriver;

import java.util.List;
import java.util.Map;

public abstract class Model {
    private final String[] columnsName;
    private final String modelName;

    public Model(String[] columnsName, String modelName) {
        this.columnsName = columnsName;
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public Map<String, List<Object>> get(SQLDriver driver, String tableName) {
        return driver.getValuesFromTable(tableName, this.columnsName);
    }

    public boolean insert(SQLDriver driver, String tableName) {
        // TODO
        return false;
    }
}
