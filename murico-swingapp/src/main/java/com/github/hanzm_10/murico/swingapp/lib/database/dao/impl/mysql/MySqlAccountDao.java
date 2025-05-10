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
package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.auth.MuricoCrypt.HashedStringWithSalt;
import com.github.hanzm_10.murico.swingapp.lib.auth.Salt;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.AccountDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.utils.CharUtils;

/** NOTE: NEVER EVER STORE PASSWORDS. */
public class MySqlAccountDao implements AccountDao {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = MuricoLogger.getLogger(MySqlAccountDao.class);

	@Override
	public HashedStringWithSalt getHashedPasswordWithSaltByUserDisplayName(@NotNull String displayName)
			throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("select_hashed_pass_with_salt_by_display_name", "accounts",
				SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query)) {
			statement.setString(1, displayName);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				byte[] passwordHash = CharUtils.fromBase64(resultSet.getString("password_hash"));
				String salt = resultSet.getString("password_salt");

				return new HashedStringWithSalt(passwordHash, Salt.fromBase64(salt));
			}
		}

		return null;
	}

	@Override
	public boolean isEmailTaken(@NotNull String email) throws IOException, SQLException {
		var emailTaken = false;
		var query = MySqlQueryLoader.getInstance().get("is_email_taken", "accounts", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			statement.setString(1, email);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				emailTaken = resultSet.getInt(1) != 0;
			}

		}

		return emailTaken;
	}

	@Override
	public void registerAccount(@NotNull String displayName, @NotNull String email,
			@NotNull HashedStringWithSalt hashedPassword) throws IOException, SQLException {
		var userCreationQuery = MySqlQueryLoader.getInstance().get("create_user", "users", SqlQueryType.INSERT);
		var accountCreationQuery = MySqlQueryLoader.getInstance().get("create_account", "accounts",
				SqlQueryType.INSERT);
		var accountPendingVerificationsQuery = MySqlQueryLoader.getInstance().get("create_account_pending_verification",
				"accounts_pending_verifications", SqlQueryType.INSERT);

		try (var conn = MySqlFactoryDao.createConnection();) {
			conn.setAutoCommit(false);

			try (var createUserStmnt = conn.prepareStatement(userCreationQuery, Statement.RETURN_GENERATED_KEYS);
					var createAccountStmnt = conn.prepareStatement(accountCreationQuery,
							Statement.RETURN_GENERATED_KEYS);
					var createAccountPendingVerificationStmnt = conn
							.prepareStatement(accountPendingVerificationsQuery);) {
				createUserStmnt.setString(1, displayName);
				createUserStmnt.executeUpdate();

				var userStmntKeys = createUserStmnt.getGeneratedKeys();

				if (!userStmntKeys.next()) {
					throw new SQLException("A user table was created but no surrogate key was generated.");
				}

				createAccountStmnt.setInt(1, userStmntKeys.getInt(1));
				createAccountStmnt.setString(2, email);
				createAccountStmnt.setString(3, CharUtils.toBase64(hashedPassword.hashedString()));
				createAccountStmnt.setString(4, hashedPassword.salt().toBase64());
				createAccountStmnt.executeUpdate();

				var accountStmntKeys = createAccountStmnt.getGeneratedKeys();

				if (!accountStmntKeys.next()) {
					throw new SQLException("An account table was created but no surrogate key was generated.");
				}

				createAccountPendingVerificationStmnt.setInt(1, accountStmntKeys.getInt(1));
				createAccountPendingVerificationStmnt.executeUpdate();

				conn.commit();
			} catch (SQLException e) {
				if (conn != null) {
					conn.rollback();
				}

				throw e;
			}
		}
	}
}
