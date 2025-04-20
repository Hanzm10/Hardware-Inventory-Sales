package com.github.hanzm_10.murico.swingapp.lib.database.mysql.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import com.github.hanzm_10.murico.swingapp.exceptions.NeedsDeveloperAttentionException;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.UserCredentialsDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class MySqlUserCredentialsDao implements UserCredentialsDao {
    private static final Logger LOGGER = MuricoLogger.getLogger(MySqlUserCredentialsDao.class);

    @Override
    public String getUserPasswordByUserDisplayName(@NotNull String _userDisplayName)
            throws SQLException {
        String userPassword = null;

        try (var conn = MySqlFactoryDao.createConnection()) {
            var query = MySqlQueryLoader.getInstance().get("user_credentials",
                    "select_user_password_by_display_name", SqlQueryType.SELECT);
            var statement = conn.prepareStatement(query);

            statement.setString(1, _userDisplayName);

            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userPassword = resultSet.getString("user_password");
            }
        } catch (FileNotFoundException e) {
            LOGGER.severe("SQL query file not found: " + e.getMessage());
            throw new NeedsDeveloperAttentionException();
        } catch (IOException e) {
            LOGGER.severe("Error reading SQL query file: " + e.getMessage());
        }

        return userPassword;
    }

}
