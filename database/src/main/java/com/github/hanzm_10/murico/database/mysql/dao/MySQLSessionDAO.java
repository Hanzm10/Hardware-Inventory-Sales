/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.database.mysql.dao;

import java.sql.Statement;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.database.AbstractSQLFactoryDAO;
import com.github.hanzm_10.murico.database.dao.SessionDAO;
import com.github.hanzm_10.murico.database.model.Session;
import com.github.hanzm_10.murico.database.model.user.User;
import com.github.hanzm_10.murico.database.mysql.MySQLFactoryDAO;
import com.github.hanzm_10.murico.database.query.SQLQueryCache;
import com.github.hanzm_10.murico.database.query.SQLQueryCache.SQLQueryType;
import com.github.hanzm_10.murico.utils.MuricoLogUtils;

public class MySQLSessionDAO implements SessionDAO {
	private static final Logger LOGGER = MuricoLogUtils.getLogger(MySQLSessionDAO.class);

	@Override
	public String createSession(@NotNull User user) {
		return createSession(user, "", "");
	}

	@Override
	public String createSession(@NotNull User user, String ipAddress) {
		return createSession(user, ipAddress, "");
	}

	@Override
	public String createSession(@NotNull User user, String ipAddress, String userAgent) {
		String _sessionUid = null;

		try (var conn = MySQLFactoryDAO.createConnection()) {
			var query = SQLQueryCache.getInstance().getQuery("sessions_insert_session", AbstractSQLFactoryDAO.MYSQL,
					SQLQueryType.INSERT);
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

			LOGGER.info("Session created: " + _sessionUid);

			statement.close();
		} catch (Exception e) {
			LOGGER.severe("Error creating session: " + e.getMessage());
		}

		return _sessionUid;
	}

	@Override
	public boolean deleteSession() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Session getSessionByUid(@NotNull String _sessionUid) {
		Session session = null;

		try (var conn = MySQLFactoryDAO.createConnection()) {
			var query = SQLQueryCache.getInstance().getQuery("session_by_uid", AbstractSQLFactoryDAO.MYSQL,
					SQLQueryType.SELECT);
			var statement = conn.prepareStatement(query);
			statement.setString(1, _sessionUid);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				session = new Session.Builder().setUserId(resultSet.getInt("_user_id"))
						.setIpAddress(resultSet.getString("session_ip_address"))
						.setUserAgent(resultSet.getString("session_user_agent"))
						.setSessionId(resultSet.getInt("_session_id"))
						.setSessionUid(resultSet.getString("_session_uid"))
						.setSessionCreatedAt(resultSet.getTimestamp("_session_created_at"))
						.setSessionExpiresAt(resultSet.getTimestamp("_session_expires_at")).build();
			}

			System.out.println("Session retrieved: " + session);

			statement.close();
		} catch (Exception e) {
			LOGGER.severe("Error retrieving session: " + e.getMessage());
		}

		return session;
	}

	@Override
	public void printSessionTable() {
		try (var conn = MySQLFactoryDAO.createConnection()) {
			var query = SQLQueryCache.getInstance().getQuery("session_table", AbstractSQLFactoryDAO.MYSQL,
					SQLQueryType.SELECT);
			var statement = conn.prepareStatement(query);

			var rsmd = statement.getMetaData();
			var columnCount = rsmd.getColumnCount();
			var resultSet = statement.executeQuery();

			for (var i = 1; i <= columnCount; i++) {
				System.out.print(rsmd.getColumnName(i) + "\t");
			}

			while (resultSet.next()) {
				System.out.println();
				for (var i = 1; i <= columnCount; i++) {
					System.out.print(resultSet.getString(i) + "\t");
				}
			}
			System.out.println();

			statement.close();
		} catch (Exception e) {
			LOGGER.severe("Error printing session table: " + e.getMessage());
		}
	}

	@Override
	public boolean sessionExists(@NotNull String _sessionUid) {
		var exists = false;

		try (var conn = MySQLFactoryDAO.createConnection()) {
			var query = SQLQueryCache.getInstance().getQuery("session_exists", AbstractSQLFactoryDAO.MYSQL,
					SQLQueryType.SELECT);
			var statement = conn.prepareStatement(query);
			statement.setString(1, _sessionUid);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				exists = resultSet.getInt(1) != 0;
			}

			LOGGER.info("Session exists: " + exists);

			statement.close();
		} catch (Exception e) {
			LOGGER.severe("Error checking session existence: " + e.getMessage());
		}

		return exists;
	}

	@Override
	public int updateSession() {
		// TODO Auto-generated method stub
		return 0;
	}
}
