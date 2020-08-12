package os.utils.database;

import os.utils.Console;
import os.utils.database.sql.MySQL;

import java.sql.*;
import java.util.*;

public class DBManager {
    // pass and login to my database
    private static final String LOGIN = "root";
    private static final String PASS = "s3r3zka";

    private static MySQL sql;
    private static Connection connection;
    private final String databaseName;

    public DBManager(String address, int port, String databaseName) throws SQLException {
        // format url
        this.databaseName = databaseName;
        String URL = "jdbc:mysql://" + address + ":" + port + "/" + databaseName + "?useUnicode=true&serverTimezone=UTC";

        // create connection
        sql = new MySQL();
        connection = DriverManager.getConnection(URL, LOGIN, PASS);
    }

    // >> DATABASE UTILS <<

    /**
     * @param tableName
     * @return data from table
     * @throws SQLException
     * @action get data from selected table in format Map<Integer, List<Object>>
     */
    public Map<Integer, List<Object>> getData(String tableName) throws SQLException {
        // get RS from database
        ResultSet resultSet = sql.getData("SELECT * FROM " + tableName + ";", connection);

        // create map to save results
        Map<Integer, List<Object>> result = new HashMap<>();

        // while RS has data get columns and save to map `result`
        while (resultSet.next()) {
            int temp_id = resultSet.getInt(1);
            List<Object> temp_data = new ArrayList<>();

            for (int i = 2; i <= resultSet.getMetaData().getColumnCount(); i++)
                temp_data.add(resultSet.getObject(i));

            result.put(temp_id, temp_data);
        }

        // return collected data
        return result;
    }

    /**
     * @param tableName (where insert)
     * @param fields    (data to insert)
     * @param values    (what insert)
     * @return if insert successfully - returns true, in other cases returns false
     * @action insert data in table
     */
    public boolean insertData(String tableName, String[] fields, String[] values) {
        // check if values and fields length both
        if (fields.length != values.length)
            return false;

        // check if arrays are empty
        if (fields.length == 0)
            return false;

        // generate query
        StringBuilder insertQuery = new StringBuilder("INSERT INTO " + databaseName + "." + tableName + "(");

        for (String field : fields) // fill fields
            insertQuery.append("`").append(field).append("` ,");
        insertQuery = new StringBuilder(insertQuery.substring(0, insertQuery.length() - 1));
        insertQuery.append(") VALUES (");

        for (String value : values) // fill values
            insertQuery.append("\"").append(value).append("\" ,");
        insertQuery = new StringBuilder(insertQuery.substring(0, insertQuery.length() - 1));
        insertQuery.append(");");

        // attempt run query, if catch error return false, if all right - true
        try {
            sql.runQuery(insertQuery.toString(), connection);
            return true;
        } catch (SQLException throwable) {
            Console.errln("Ошибка! " + throwable.getMessage());
            return false;
        }
    }

    /**
     * @param tableName (where delete)
     * @param params    (delete params, example {ID<2})
     * @return if delete successfully - returns true, in other cases returns false
     * @action delete records in database
     */
    public boolean deleteData(String tableName, String[] params) {
        // generate query
        StringBuilder deleteQuery = new StringBuilder();

        deleteQuery.append("DELETE FROM ").append(databaseName).append(".").append(tableName).append(" WHERE ");
        deleteQuery.append(params[0]);
        for (int i = 1; i < params.length; i++) // fill params
            deleteQuery.append(" AND ").append(params[i]);
        deleteQuery.append(";");

        // attempt run query, if catch error return false, if all right - true
        try {
            sql.runQuery(deleteQuery.toString(), connection);
            return true;
        } catch (SQLException throwable) {
            Console.errln("Ошибка! " + throwable.getMessage());
            return false;
        }
    }

    /**
     * @param tableName (where change)
     * @param params    (fields to change)
     * @param newValues (new values)
     * @return if update successfully - returns true, in other cases returns false
     */
    public boolean updateData(String tableName, String[] params, String[] newValues) {
        // generate query
        StringBuilder updateQuery = new StringBuilder();

        updateQuery.append("UPDATE SET ");
        Arrays.stream(params).forEach(s -> updateQuery.append(s).append(","));
        updateQuery.deleteCharAt(updateQuery.length());

        updateQuery.append(" WHERE ");
        Arrays.stream(newValues).forEach(s -> updateQuery.append(s).append(","));
        updateQuery.append(";");

        // attempt run query, if catch error return false, if all right - true
        try {
            sql.runQuery(updateQuery.toString(), connection);
            return true;
        } catch (SQLException throwable) {
            Console.errln("Ошибка! " + throwable.getMessage());
            return false;
        }
    }
}
