package app.os.sql.drivers;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rds.auth.GetIamAuthTokenRequest;
import com.amazonaws.services.rds.auth.RdsIamAuthTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class MySQLDriver extends SQLDriver {
    private static final Logger logger = LoggerFactory.getLogger(MySQLDriver.class.getName());

    private Connection connection;
    private Statement statement;

    public MySQLDriver(String rds_instance_hostname, int rds_instance_port, String region_name, String db_user) {
        super(rds_instance_hostname, rds_instance_port, region_name, db_user);

        try {
            //get the connection
            connection = getDBConnectionUsingIam();

            //verify the connection is successful
            Statement stmt = connection.createStatement();

            //close the connection
            stmt.close();
            connection.close();

            clearSslProperties();
        } catch (Exception e ){
            return;
        }
    }

    private Connection getDBConnectionUsingIam() throws Exception {
        setSslProperties();
        return DriverManager.getConnection(super.JDBC_URL, setMySqlConnectionProperties());
    }

    private Properties setMySqlConnectionProperties() {
        Properties mysqlConnectionProperties = new Properties();
        mysqlConnectionProperties.setProperty("verifyServerCertificate", "true");
        mysqlConnectionProperties.setProperty("useSSL", "true");
        mysqlConnectionProperties.setProperty("user", DB_USER);
        mysqlConnectionProperties.setProperty("password", generateAuthToken());
        return mysqlConnectionProperties;
    }

    private void setSslProperties() {
        System.setProperty("javax.net.ssl.trustStoreType", KEY_STORE_TYPE);
        System.setProperty("javax.net.ssl.trustStorePassword", DEFAULT_KEY_STORE_PASSWORD);
    }

    private static void clearSslProperties() throws Exception {
        System.clearProperty("javax.net.ssl.trustStoreType");
        System.clearProperty("javax.net.ssl.trustStorePassword");
    }

    private String generateAuthToken() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);

        RdsIamAuthTokenGenerator generator = RdsIamAuthTokenGenerator.builder()
                .credentials(new AWSStaticCredentialsProvider(awsCredentials)).region(REGION_NAME).build();
        return generator.getAuthToken(GetIamAuthTokenRequest.builder()
                .hostname(RDS_INSTANCE_HOSTNAME).port(RDS_INSTANCE_PORT).userName(DB_USER).build());
    }


    @Override
    public Map<String, List<Object>> getValuesFromTable(String tableName, String[] columns) {
        try {
            // create and execute query
            String sql = "SELECT * FROM " + tableName + ";";
            ResultSet queryResult;

            statement = null; // reset statement
            statement = connection.createStatement(); // get new statement
            queryResult = statement.executeQuery(sql); // execute

            // save result
            Map<String, List<Object>> receivedData = new HashMap<>(); // result map
            while (queryResult.next()) {
                for (String columnLabel : columns) {
                    // get data
                    List<Object> columnData = new ArrayList<>();
                    columnData.add(queryResult.getObject(columnLabel));

                    // put to result
                    if (receivedData.containsKey(columnLabel)) { // add to already created list
                        List<Object> current = receivedData.get(columnLabel);
                        current.addAll(columnData);

                        receivedData.put(columnLabel, current);
                    } else { // create new list
                        receivedData.put(columnLabel, columnData);
                    }
                }
            }

            return receivedData;
        } catch (Exception throwables) {
            logger.error("Error! Can't load data from database! Message: " + throwables.getMessage());
            return null;
        }
    }

    @Override
    public boolean insertDataToTable(String tableName, InsertData insertData) {
        String columnsRef = Arrays.toString(insertData.getColumns());
        columnsRef = columnsRef.substring(1);
        columnsRef = columnsRef.substring(0, columnsRef.length() - 1);

        StringBuilder valuesBuilder = new StringBuilder();
        for (String val : insertData.getData())
            valuesBuilder.append("'").append(val).append("'").append(",");

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder
                .append("INSERT INTO ")
                .append(tableName)
                .append(" (")
                .append(columnsRef)
                .append(") VALUES (")
                .append(valuesBuilder.toString(), 0, valuesBuilder.toString().length() - 1)
                .append(");");


        try {
            statement = null;
            statement = connection.createStatement();
            statement.execute(sqlBuilder.toString());

            return true;
        } catch (Exception throwables) {
            logger.error(String.format("Exception! Message: %s.", throwables.getMessage()));
            return false;
        }
    }
}
