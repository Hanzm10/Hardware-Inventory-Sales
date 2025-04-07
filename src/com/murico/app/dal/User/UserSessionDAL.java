package com.murico.app.dal.User;

import java.sql.SQLException;
import com.murico.app.dal.database.mysql.DatabaseManagerMySQL;
import com.murico.app.model.user.UserSession;

/**
 * Data Access Layer (DAL) class for managing user sessions.
 * <p>
 * This class provides methods to interact with the user session table in the database.
 * </p>
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class UserSessionDAL {
  public static String getTableName() {
    return "sessions";
  }

  /**
   * Selects a user session from the database by session ID.
   *
   * @param sessionId The unique identifier for the session.
   * @return A UserSession object representing the selected session, or null if not found.
   * 
   * @throws AssertionError if sessionUid is null
   */
  public static UserSession selectSessionById(String sessionUid) throws AssertionError {
    assert sessionUid != null : "Session UID cannot be null";

    try {
      UserSession userSession = null;
      var conn = DatabaseManagerMySQL.getInstance().connect();

      var sql = String.format("SELECT * FROM %s WHERE _session_uid = ?;", getTableName());
      var preparedStatement = conn.prepareStatement(sql);

      preparedStatement.setString(1, sessionUid);

      var resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        userSession = new UserSession.Builder().setUserId(resultSet.getInt("_user_id"))
          .setIpAddress(resultSet.getString("session_ip_address"))
          .setUserAgent(resultSet.getString("session_user_agent"))
          .setSessionId(resultSet.getInt("_session_id"))
          .setSessionUid(resultSet.getString("_session_uid"))
          .setSessionCreatedAt(resultSet.getTimestamp("_session_created_at"))
          .setSessionExpiresAt(resultSet.getTimestamp("_session_expires_at")).build();
      }

      preparedStatement.close();
      DatabaseManagerMySQL.getInstance().disconnect(conn);

      return userSession;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;

  }

}
