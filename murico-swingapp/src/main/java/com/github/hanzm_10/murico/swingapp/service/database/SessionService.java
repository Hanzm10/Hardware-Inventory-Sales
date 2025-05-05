/**
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.service.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.config.ApplicationConfig;
import com.github.hanzm_10.murico.swingapp.constants.PropertyKey;
import com.github.hanzm_10.murico.swingapp.lib.auth.MuricoCrypt;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;
import com.github.hanzm_10.murico.swingapp.lib.exceptions.MuricoError;
import com.github.hanzm_10.murico.swingapp.lib.exceptions.MuricoErrorCodes;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.utils.SessionUtils;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;

public class SessionService {
	public static final Logger LOGGER = MuricoLogger.getLogger(SessionService.class);

	public static boolean checkPreviousSessionAndStoreInSessionManager() throws IOException, SQLException {
		var sessionToken = ApplicationConfig.getInstance().getConfig().getProperty(PropertyKey.Session.UID);

		if (sessionToken == null) {
			return false;
		}

		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
		var sessionDao = factory.getSessionDao();
		var session = sessionDao.getSessionByToken(sessionToken);

		if (session == null || SessionUtils.isSessionExpired(session)) {
			removeStoredSessionUid();
			return false;
		}

		var userDao = factory.getUserDao();
		var user = userDao.getUserById(session._userId());

		if (user == null) {
			removeStoredSessionUid();
			return false;
		}

		SessionManager.getInstance().setSession(session, user);

		return true;
	}

	public static User loginUser(@NotNull String _userDisplayName, @NotNull char[] userPassword) throws MuricoError {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
		var userDao = factory.getUserDao();

		try {
			var user = userDao.getUserByDisplayName(_userDisplayName);

			if (user == null) {
				throw new MuricoError(MuricoErrorCodes.INVALID_CREDENTIALS);
			}

			var hashedPasswordWithSalt = factory.getAccountDao()
					.getHashedPasswordWithSaltByUserDisplayName(_userDisplayName);

			if (hashedPasswordWithSalt == null) {
				throw new MuricoError(MuricoErrorCodes.INVALID_CREDENTIALS);
			}

			var hashedUserPassword = new MuricoCrypt().hash(userPassword, hashedPasswordWithSalt.salt());

			if (!hashedPasswordWithSalt.equalsHashedString(hashedUserPassword)) {
				hashedUserPassword.clearHashedStringBytes();
				hashedUserPassword.clearHashedStringBytes();

				throw new MuricoError(MuricoErrorCodes.INVALID_CREDENTIALS);
			}

			hashedUserPassword.clearHashedStringBytes();
			hashedUserPassword.clearHashedStringBytes();

			var sessionDao = factory.getSessionDao();
			var sessionToken = sessionDao.createSession(user);

			if (sessionToken == null) {
				throw new MuricoError(MuricoErrorCodes.DATABASE_CONNECTION_FAILED);
			}

			var session = sessionDao.getSessionByToken(sessionToken);

			if (session == null) {
				throw new MuricoError(MuricoErrorCodes.UNREACHABLE_ERROR);
			}

			ApplicationConfig.getInstance().getConfig().setProperty(PropertyKey.Session.UID, sessionToken);
			SessionManager.getInstance().setSession(session, user);

			return user;
		} catch (SQLException | IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to login user", e);
		}

		return null;
	}

	private static void removeStoredSessionUid() throws IOException {
		ApplicationConfig.getInstance().getConfig().remove(PropertyKey.Session.UID);
	}
}
