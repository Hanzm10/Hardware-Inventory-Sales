package com.github.hanzm_10.murico.swingapp.lib.database.mysql.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import com.github.hanzm_10.murico.swingapp.exceptions.NeedsDeveloperAttentionException;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.UserDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserGender;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.UserRole;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class MySqlUserDao implements UserDao {
    private static final Logger LOGGER = MuricoLogger.getLogger(MySqlUserDao.class);

    @Override
    public User getUserByDisplayName(@NotNull String _userDisplayName) throws SQLException {
        User user = null;

        try (var conn = MySqlFactoryDao.createConnection()) {
            var query = MySqlQueryLoader.getInstance().get("get_user_by_display_name", "users",
                    SqlQueryType.SELECT);
            var statement = conn.prepareStatement(query);

            statement.setString(1, _userDisplayName);

            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User.Builder()
                        .setUserId(resultSet.getInt("_user_id"))
                        .setUserCreatedAt(resultSet.getTimestamp("_user_created_at"))
                        .setUserDisplayName(resultSet.getString("user_display_name"))
                        .setUserDisplayImage(resultSet.getString("user_display_image"))
                        .setUserGender(UserGender.fromString(resultSet.getString("user_gender")))
                        .setUserRole(UserRole.fromString(resultSet.getString("user_role"))).build();
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "SQL file not found", e);
            throw new NeedsDeveloperAttentionException();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading SQL file", e);
        }

        return user;
    }

    @Override
    public User getUserByEmail(@NotNull String _userEmail) throws SQLException {
        return null;
    }

    @Override
    public User getUserById(@Range(from = 0, to = 2147483647) int _userID) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}
