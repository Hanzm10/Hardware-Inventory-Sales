package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.PackagingDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.packaging.ItemPackaging;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;

public class MySqlPackagingDao implements PackagingDao {

	@Override
	public ItemPackaging[] getAllPackagings() throws SQLException, IOException {
		var query = MySqlQueryLoader.getInstance().get("get_all_packagings", "packagings", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement()) {
			try (var resultSet = stmt.executeQuery(query)) {
				var packagings = new ArrayList<ItemPackaging>();

				while (resultSet.next()) {
					packagings.add(
							new ItemPackaging(resultSet.getInt("_packaging_id"), resultSet.getTimestamp("_created_at"),
									resultSet.getString("name"), resultSet.getString("description")));
				}

				return packagings.toArray(new ItemPackaging[packagings.size()]);
			}
		}
	}

}
