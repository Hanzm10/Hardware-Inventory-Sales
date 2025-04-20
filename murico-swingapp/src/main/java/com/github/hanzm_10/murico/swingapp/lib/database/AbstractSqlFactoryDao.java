package com.github.hanzm_10.murico.swingapp.lib.database;

import java.util.logging.Logger;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SessionDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.UserCredentialsDao;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.UserDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

/**
 * This class is responsible for creating the appropriate SQL factory DAO based on the database
 * type. It uses a factory method pattern to create the appropriate instance.
 *
 * @see <a href= "https://www.oracle.com/java/technologies/dataaccessobject.html">DAO Pattern by
 *      Oracle</a>
 */
public abstract class AbstractSqlFactoryDao {
    protected static final Logger LOGGER = MuricoLogger.getLogger(AbstractSqlFactoryDao.class);
    public static final int MYSQL = 1;

    public static AbstractSqlFactoryDao getSqlFactoryDao(int type) {
        return switch (type) {
            case MYSQL -> new MySqlFactoryDao();
            default -> throw new IllegalArgumentException("Invalid database type: " + type);
        };
    }

    public abstract SessionDao getSessionDao();

    public abstract UserCredentialsDao getUserCredentialsDao();

    public abstract UserDao getUserDao();
}
