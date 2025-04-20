package com.github.hanzm_10.murico.swingapp.lib.database.mysql;

import com.github.hanzm_10.murico.swingapp.constants.Misc;
import com.github.hanzm_10.murico.swingapp.lib.cache.LRU;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader;

/**
 * This class is responsible for loading SQL queries from files for MySQL database. <br>
 * <br>
 * It extends the AbstractSqlQueryLoader class and implements the getDatabaseName method. The
 * queries are loaded from the resources directory under the sql/mysql/ directory. <br>
 * <br>
 *
 * @see {@link AbstractSqlQueryLoader}
 */
public class MySqlQueryLoader extends AbstractSqlQueryLoader {
    private static MySqlQueryLoader instance;

    public static synchronized MySqlQueryLoader getInstance() {
        if (instance == null) {
            instance = new MySqlQueryLoader();
        }

        return instance;
    }

    private MySqlQueryLoader() {
        super();

        queryCache = new LRU<>(Misc.DEFAULT_MAX_STRING_CACHE_SIZE);
    }

    private MySqlQueryLoader(int maxCacheSize) {
        super();

        if (maxCacheSize <= 0) {
            throw new IllegalArgumentException("Cache size must be greater than 0");
        }

        queryCache = new LRU<>(maxCacheSize);
    }

    @Override
    public String getDatabaseName() {
        return "mysql";
    }

}
