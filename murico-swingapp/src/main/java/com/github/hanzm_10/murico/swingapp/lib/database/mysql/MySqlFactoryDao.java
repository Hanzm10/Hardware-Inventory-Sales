package com.github.hanzm_10.murico.swingapp.lib.database.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Properties;
import com.github.hanzm_10.murico.swingapp.constants.PropertyKey;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SessionDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.UserCredentialsDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.UserDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.dao.MySqlSessionDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.dao.MySqlUserCredentialsDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.dao.MySqlUserDao;
import com.github.hanzm_10.murico.swingapp.lib.io.PropertiesIO;

public final class MySqlFactoryDao extends AbstractSqlFactoryDao {
    public static String DB_URL;
    public static String DB_USER;
    public static String DB_PASSWORD;
    public static String DB_NAME;

    static {
        var properties = new Properties();

        try {
            PropertiesIO.loadProperties(MySqlFactoryDao.class, properties, "config");

            DB_URL = properties.getProperty(PropertyKey.Database.DB_URL);
            DB_USER = properties.getProperty(PropertyKey.Database.DB_USER);
            DB_PASSWORD = properties.getProperty(PropertyKey.Database.DB_PASSWORD);
            DB_NAME = properties.getProperty(PropertyKey.Database.DB_NAME);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load MySQL properties file", e);
        } catch (IllegalArgumentException e) {
            // DO nothing
        }
    }

    public static final Connection createConnection() throws SQLException, SQLTimeoutException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public SessionDao getSessionDao() {
        return new MySqlSessionDao();
    }

    @Override
    public UserCredentialsDao getUserCredentialsDao() {
        return new MySqlUserCredentialsDao();
    }

    @Override
    public UserDao getUserDao() {
        return new MySqlUserDao();
    }

}
