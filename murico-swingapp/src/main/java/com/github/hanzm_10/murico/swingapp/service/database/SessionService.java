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
package com.github.hanzm_10.murico.swingapp.service.database;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.config.ApplicationConfig;
import com.github.hanzm_10.murico.swingapp.constants.PropertyKey;
import com.github.hanzm_10.murico.swingapp.exceptions.ApplicationAuthException;
import com.github.hanzm_10.murico.swingapp.exceptions.ApplicationAuthException.ErrorCode;
import com.github.hanzm_10.murico.swingapp.lib.auth.PasswordHasher;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;
import com.github.hanzm_10.murico.swingapp.state.SessionManager;

public class SessionService {
	public static User loginUser(@NotNull String _userDisplayName, @NotNull String userPassword)
			throws ApplicationAuthException, Exception {
		var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
		var userDao = factory.getUserDao();
		var user = userDao.getUserByDisplayName(_userDisplayName);

		if (user == null) {
			throw new ApplicationAuthException(ErrorCode.USER_NOT_FOUND);
		}

		var userCredentialsDao = factory.getUserCredentialsDao();
		var userPasswordFromDB = userCredentialsDao.getUserPasswordByUserDisplayName(_userDisplayName);

		if (userPasswordFromDB == null) {
			throw new ApplicationAuthException(
					"User password not found for user with display name " + _userDisplayName);
		}

		var hashedUserPassword = PasswordHasher.hash(userPassword);

		if (!userPasswordFromDB.equals(hashedUserPassword)) {
			throw new ApplicationAuthException(ErrorCode.INVALID_PASSWORD);
		}

		var sessionDao = factory.getSessionDao();
		var sessionUid = sessionDao.createSession(user);

		if (sessionUid == null) {
			throw new Exception("Session creation failed");
		}

		var session = sessionDao.getSessionByUid(sessionUid);

		if (session == null) {
			throw new Exception("Session was created but not found with uid " + sessionUid);
		}

		ApplicationConfig.getInstance().getConfig().setProperty(PropertyKey.Session.UID, sessionUid);
		SessionManager.getInstance().setSession(session, user);

		return user;
	}
}
