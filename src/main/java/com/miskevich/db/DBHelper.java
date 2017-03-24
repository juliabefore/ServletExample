package com.miskevich.db;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.sql.PooledConnection;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DBHelper {

    public Map<String, String> connectionMap;

    public DBHelper() {
        connectionMap = new HashMap<>();
        connectionMap.put("SERVLET", "/servlet.properties");
    }

    public PooledConnection getConnectionPool(String dbConnectionPath){
        Properties properties = new Properties();
        MysqlConnectionPoolDataSource poolDataSource = new MysqlConnectionPoolDataSource();
        PooledConnection pooledConnection;
        try {
            properties.load(DBHelper.class.getResourceAsStream(dbConnectionPath));
            poolDataSource.setURL(properties.getProperty("DB_URL"));
            poolDataSource.setUser(properties.getProperty("USER"));
            poolDataSource.setPassword(properties.getProperty("PASS"));
            pooledConnection = poolDataSource.getPooledConnection();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

        return pooledConnection;
    }

}
