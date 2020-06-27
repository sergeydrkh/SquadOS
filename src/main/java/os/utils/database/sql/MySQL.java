package os.utils.database.sql;

import java.sql.*;

public class MySQL {
    public ResultSet getData(String query, Connection db_con) throws SQLException {
        Statement stmt = db_con.createStatement();
        return stmt.executeQuery(query);
    }

    public void runQuery(String query, Connection db_con) throws SQLException {
        Statement stmt = db_con.createStatement();
        stmt.execute(query);
    }
}
