package app.os.sql.drivers;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

import java.util.List;
import java.util.Map;

public abstract class SQLDriver {
    protected final DefaultAWSCredentialsProviderChain creds = new DefaultAWSCredentialsProviderChain();
    protected final String AWS_ACCESS_KEY = creds.getCredentials().getAWSAccessKeyId();
    protected final String AWS_SECRET_KEY = creds.getCredentials().getAWSSecretKey();

    protected final String RDS_INSTANCE_HOSTNAME;
    protected final int RDS_INSTANCE_PORT;
    protected final String REGION_NAME;
    protected final String DB_USER;
    protected final String JDBC_URL;

    protected static final String KEY_STORE_TYPE = "JKS";
    protected static final String KEY_STORE_PROVIDER = "SUN";
    protected static final String KEY_STORE_FILE_PREFIX = "sys-connect-via-ssl-test-cacerts";
    protected static final String KEY_STORE_FILE_SUFFIX = ".jks";
    protected static final String DEFAULT_KEY_STORE_PASSWORD = "changeit";

    protected SQLDriver(String rds_instance_hostname, int rds_instance_port, String region_name, String db_user) {
        RDS_INSTANCE_HOSTNAME = rds_instance_hostname;
        RDS_INSTANCE_PORT = rds_instance_port;
        REGION_NAME = region_name;
        DB_USER = db_user;
        JDBC_URL = "jdbc:mysql://" + RDS_INSTANCE_HOSTNAME + ":" + RDS_INSTANCE_PORT;
    }

    public abstract Map<String, List<Object>> getValuesFromTable(String tableName, String[] columns);

    public abstract boolean insertDataToTable(String tableName, InsertData insertData);
}
