package com.github.hanzm_10.murico.swingapp.lib.database.mysql.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import com.github.hanzm_10.murico.swingapp.exceptions.NeedsDeveloperAttentionException;
import com.github.hanzm_10.murico.swingapp.exceptions.UnimplementedException;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SessionDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.Session;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class MySqlSessionDao implements SessionDao {
    private static final Logger LOGGER = MuricoLogger.getLogger(MySqlSessionDao.class);

    @Override

    public @NotNull String createSession(@NotNull User user)
            throws SQLException {
        return createSession(user, "", "");
    }

    @Override
    public @NotNull String createSession(@NotNull User user, String ipAddress)
            throws SQLException {
        return createSession(user, ipAddress, "");
    }

    @Override
    public @NotNull String createSession(@NotNull User user, String ipAddress, String userAgent)
            throws SQLException {
        String _sessionUid = null;

        try (var conn = MySqlFactoryDao.createConnection()) {
            var query = MySqlQueryLoader.getInstance().get("sessions", "create_session",
                    SqlQueryType.INSERT);
            var statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, user._userId());
            statement.setString(2, ipAddress);
            statement.setString(3, userAgent);

            var resultSet = statement.executeUpdate();
            var RETURNED_SOMETHING = resultSet == 1;

            if (RETURNED_SOMETHING) {
                var generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    _sessionUid = generatedKeys.getString("_session_uid");
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "SQL file not found", e);
            throw new NeedsDeveloperAttentionException();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading SQL file", e);
        } catch (SQLFeatureNotSupportedException e) {
            LOGGER.log(Level.SEVERE, "SQL feature not supported", e);
            throw new NeedsDeveloperAttentionException();
        }

        return _sessionUid;
    }

    @Override
    public Session getSessionByUid(@NotNull String _sessionUid)
            throws SQLException {
        Session session = null;

        try (var conn = MySqlFactoryDao.createConnection()) {
            var query = MySqlQueryLoader.getInstance().get("sessions", "select_by_uid",
                    SqlQueryType.SELECT);

            var statement = conn.prepareStatement(query);

            statement.setString(1, _sessionUid);

            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                session = new Session.Builder().setSessionUid(resultSet.getString("_session_uid"))
                        .setUserId(resultSet.getInt("_user_id"))
                        .setIpAddress(resultSet.getString("_ip_address"))
                        .setUserAgent(resultSet.getString("_user_agent"))
                        .setSessionCreatedAt(resultSet.getTimestamp("_created_at"))
                        .setSessionExpiresAt(resultSet.getTimestamp("_updated_at")).build();
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "SQL file not found", e);
            throw new NeedsDeveloperAttentionException();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading SQL file", e);
        }

        return session;
    }

    @Override
    public void printSessionTableOfUser(@NotNull User user)
            throws SQLException {
        throw new UnimplementedException();
    }

    @Override
    public boolean sessionExists(@NotNull String _sessionUid)
            throws SQLException {
        var sessionExists = false;

        try (var conn = MySqlFactoryDao.createConnection()) {
            var query = MySqlQueryLoader.getInstance().get("sessions", "select_exists_by_uid",
                    SqlQueryType.SELECT);

            var statement = conn.prepareStatement(query);

            statement.setString(1, _sessionUid);

            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                sessionExists = resultSet.getInt(1) != 0;
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "SQL file not found", e);
            throw new NeedsDeveloperAttentionException();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading SQL file", e);
        }

        return sessionExists;
    }

}
