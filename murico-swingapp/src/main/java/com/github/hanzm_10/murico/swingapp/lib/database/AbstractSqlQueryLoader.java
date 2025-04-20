package com.github.hanzm_10.murico.swingapp.lib.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import com.github.hanzm_10.murico.swingapp.exceptions.UnreachableException;
import com.github.hanzm_10.murico.swingapp.lib.cache.LRU;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

/**
 * Abstract class to load SQL queries from files. This class implements a cache for the SQL queries
 * to avoid loading them multiple times. The cache is an LRU (Least Recently Used) cache.
 * {@link LRU}
 * 
 * <p>
 * The SQL files should be located in the resources directory under the following structure:
 *
 * <pre>
 * sql/
 * └── database_name/
 *     ├── select/
 *     │   ├── table_name/
 *     │   │   ├── query_name.sql
 *     │   │   └── ...
 *     │   └── ...
 *     ├── insert/
 *     │   ├── table_name/
 *     │   │   ├── query_name.sql
 *     │   │   └── ...
 *     │   └── ...
 *     ├── update/
 *     │   ├── table_name/
 *     │   │   ├── query_name.sql
 *     │   │   └── ...
 *     │   └── ...
 *     └── delete/
 *         ├── table_name/
 *         │   ├── query_name.sql
 *         │   └── ...
 *         └── ...
 * </pre>
 */
public abstract class AbstractSqlQueryLoader {
    public enum SqlQueryType {
        SELECT, INSERT, UPDATE, DELETE;

        @Override
        public String toString() {
            return switch (this) {
                case SELECT -> "select";
                case INSERT -> "insert";
                case UPDATE -> "update";
                case DELETE -> "delete";
            };
        }
    }

    private static final Logger LOGGER = MuricoLogger.getLogger(AbstractSqlQueryLoader.class);
    private static final String SEPARATOR = "/";
    private static final String SQL_QUERY_DIRECTORY = "sql" + SEPARATOR;

    private static final String SQL_QUERY_FILE_EXTENSION = ".sql";

    protected LRU<String, String> queryCache;

    public @NotNull String get(@NotNull final String name, @NotNull final String tableName,
            @NotNull final SqlQueryType queryType) throws FileNotFoundException, IOException {
        var path = SQL_QUERY_DIRECTORY + getDatabaseName() + SEPARATOR + queryType.toString()
        + SEPARATOR + tableName + SEPARATOR + name
        + SQL_QUERY_FILE_EXTENSION;

        if (queryCache.containsKey(path)) {
            return queryCache.get(path);
        }

        var query = loadQuery(path);

        if (query != null) {
            queryCache.update(path, query);

            return query;
        }

        throw new UnreachableException("An exception should have been thrown before this point.");
    }

    public abstract String getDatabaseName();

    private String loadQuery(final String path) throws FileNotFoundException, IOException {
        try (var inputStream = AbstractSqlQueryLoader.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Sql file not found: " + path);
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (OutOfMemoryError e) {
            // implement a way to read the file in chunks
            LOGGER.severe("Out of memory error while reading SQL file: " + path);
        } catch (NullPointerException e) {
            // DO nothing
        }

        // to appease the compiler, shouldn't be reached
        return null;
    }
}
