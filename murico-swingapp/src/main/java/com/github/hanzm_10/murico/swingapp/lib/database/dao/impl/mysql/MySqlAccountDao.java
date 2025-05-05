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

import java.io.CharArrayWriter;
import java.io.IOException;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.auth.MuricoCrypt.HashedStringWithSalt;
import com.github.hanzm_10.murico.swingapp.lib.auth.Salt;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.AccountDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.lib.utils.CharUtils;

/**
 * NOTE: NEVER EVER STORE PASSWORDS.
 */
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
				try (var streamReader = resultSet.getCharacterStream("password_hash");) {
					CharArrayWriter writer = new CharArrayWriter();
					char[] buf = new char[1024];
					int numOfCharsRead;

					while ((numOfCharsRead = streamReader.read(buf)) != -1) {
						writer.write(buf, 0, numOfCharsRead);
					}

					char[] passChars = writer.toCharArray();

					return new HashedStringWithSalt(CharUtils.charArrayToByteArray(passChars),
							Salt.fromBase64(resultSet.getString("password_salt")));
				}
			}
		}

		return null;
	}
}
