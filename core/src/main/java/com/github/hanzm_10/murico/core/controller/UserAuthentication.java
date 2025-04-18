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
package com.github.hanzm_10.murico.core.controller;

import org.jetbrains.annotations.NotNull;
import com.github.hanzm_10.murico.core.MuricoCore;
import com.github.hanzm_10.murico.core.config.GlobalConfig;
import com.github.hanzm_10.murico.core.constants.PropertyKey;
import com.github.hanzm_10.murico.database.AbstractSQLFactoryDAO;
import com.github.hanzm_10.murico.database.model.user.User;
import com.github.hanzm_10.murico.utils.auth.PasswordHasher;

public class UserAuthentication {
    public static User loginUser(@NotNull String _userDisplayName, @NotNull String userPassword) throws Exception {
        var factory = AbstractSQLFactoryDAO.getSQLFactoryDAO(AbstractSQLFactoryDAO.MYSQL);
        var userCredentialsDAO = factory.getUserCredentialsDAO();

        var userPasswordFromDB = userCredentialsDAO.getUserPasswordByUserDisplayName(_userDisplayName);

        if (userPasswordFromDB == null) {
            throw new Exception("User not found");
        }

        var hashedUserPassword = PasswordHasher.hash(userPassword);

        if (!userPasswordFromDB.equals(hashedUserPassword)) {
            throw new Exception("Invalid password");
        }

        var userDAO = factory.getUserDAO();
        var user = userDAO.getUserByDisplayName(_userDisplayName);

        if (user == null) {
            throw new Exception("User not found");
        }

        var sessionDAO = factory.getSessionDAO();
        var sessionUid = sessionDAO.createSession(user);

        if (sessionUid == null) {
            throw new Exception("Session creation failed");
        }

        GlobalConfig.getInstance().setProperty(PropertyKey.Session.UID, sessionUid);
        MuricoCore.setLoggedInUser(user);

        return user;
    }
}
