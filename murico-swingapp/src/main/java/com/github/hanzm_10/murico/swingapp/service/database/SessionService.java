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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.config.ApplicationConfig;
import com.github.hanzm_10.murico.swingapp.constants.PropertyKey;
import com.github.hanzm_10.murico.swingapp.lib.auth.MuricoCrypt;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.session.Session;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.session.SessionStatus;
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
			sessionDao.removeSessionByToken(sessionToken);
			return false;
		}

		if (session.status() == SessionStatus.INACTIVE) {
			sessionDao.updateSessionStatusByToken(sessionToken, SessionStatus.ACTIVE);
			// it shouldn't be done this way because of potential inaccuracies with the
			// time stamp but it's
			// whatever
			session = session.newStatus(SessionStatus.ACTIVE);
		}

		var userDao = factory.getUserDao();
		var user = userDao.getUserMetadataById(session._userId());

		if (user == null) {
			removeStoredSessionUid();
			return false;
		}

		SessionManager.getInstance().setSession(session, user);

		return true;
	}

	/** Our current implementation only allows for a user to have one session. */
	public static void login(@NotNull final String _userDisplayName, @NotNull final char[] userPassword)
			throws MuricoError {
		try {
			var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
			var user = factory.getUserDao().getUserMetadataByDisplayName(_userDisplayName);

			if (user == null) {
				throw new MuricoError(MuricoErrorCodes.INVALID_CREDENTIALS);
			}

			var hashedPasswordWithSalt = factory.getAccountDao()
					.getHashedPasswordWithSaltByUserDisplayName(_userDisplayName);

			if (hashedPasswordWithSalt == null) {
				throw new MuricoError(MuricoErrorCodes.INVALID_CREDENTIALS);
			}

			var hashedUserPassword = new MuricoCrypt().hash(userPassword, hashedPasswordWithSalt.salt());

			Arrays.fill(userPassword, '\0');

			try {
				if (!hashedPasswordWithSalt.equalsHashedString(hashedUserPassword)) {
					throw new MuricoError(MuricoErrorCodes.INVALID_CREDENTIALS);
				}
			} finally {
				hashedUserPassword.clearHashedStringBytes();
				hashedPasswordWithSalt.clearHashedStringBytes();
			}

			var sessionDao = factory.getSessionDao();
			Session session = sessionDao.getSessionByUserId(user._userId());

			if (session == null) {
				session = sessionDao.createSession(user._userId());

				if (session == null) {
					throw new MuricoError(MuricoErrorCodes.DATABASE_FAILED_INSERT);
				}
			} else {
				sessionDao.updateSessionStatusByToken(session._sessionToken(), SessionStatus.ACTIVE);
			}

			ApplicationConfig.getInstance().getConfig().setProperty(PropertyKey.Session.UID, session._sessionToken());
			SessionManager.getInstance().setSession(session, user);
		} catch (SQLException | IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to query database", e);
			throw new MuricoError(MuricoErrorCodes.DATABASE_OPERATION_FAILED, e.getMessage());
		}
	}

	public static void logout() throws MuricoError {
		var sessionToken = ApplicationConfig.getInstance().getConfig().getProperty(PropertyKey.Session.UID);

		if (sessionToken == null) {
			throw new MuricoError(MuricoErrorCodes.LOGGING_OUT_FAILURE,
					"Trying to logout despite sessionToken not existing in storage.");
		}

		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		try {
			factory.getSessionDao().updateSessionStatusByToken(sessionToken, SessionStatus.INACTIVE);
			SessionManager.getInstance().removeSession();
			ApplicationConfig.getInstance().getConfig().remove(PropertyKey.Session.UID);
		} catch (SQLException | IOException e) {
			throw new MuricoError(MuricoErrorCodes.DATABASE_OPERATION_FAILED, e.getMessage());
		}
	}

	/**
	 * This does not set the user session, since our flow requires account
	 * verification from admins.
	 *
	 * @param displayName
	 * @param email
	 * @param password
	 * @throws MuricoError
	 */
	public static void register(@NotNull final String displayName, @NotNull final String email,
			@NotNull final char[] password) throws MuricoError {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);

		try {
			var userDao = factory.getUserDao();
			var accountDao = factory.getAccountDao();

			if (userDao.isUsernameTaken(displayName) || accountDao.isEmailTaken(email)) {
				throw new MuricoError(MuricoErrorCodes.ACCOUNT_EXISTS);
			}

			var hashedPassword = new MuricoCrypt().hash(password);

			Arrays.fill(password, '\0');
			accountDao.registerAccount(displayName, email, hashedPassword);
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to query database", e);
			throw new MuricoError(MuricoErrorCodes.DATABASE_OPERATION_FAILED, e.getMessage());
		} catch (IOException e) {
			throw new MuricoError(MuricoErrorCodes.FILE_OPERATION_FAILED, e.getMessage());
		}
	}

	private static void removeStoredSessionUid() throws IOException {
		ApplicationConfig.getInstance().getConfig().remove(PropertyKey.Session.UID);
	}
}
