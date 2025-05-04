package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.auth.MuricoCrypt.HashedStringWithSalt;
import com.github.hanzm_10.murico.swingapp.lib.auth.Salt;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.AccountDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;

public class MySqlAccountDao implements AccountDao {

	@Override
	public HashedStringWithSalt getHashedPasswordWithSaltByUserDisplayName(@NotNull String displayName)
			throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("select_hashed_pass_with_salt_by_display_name", "accounts",
				SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query)) {
			statement.setString(1, displayName);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				return new HashedStringWithSalt(resultSet.getString("password_hash"),
						Salt.fromBase64(resultSet.getString("password_salt")));
			}
		}

		return null;
	}

}
