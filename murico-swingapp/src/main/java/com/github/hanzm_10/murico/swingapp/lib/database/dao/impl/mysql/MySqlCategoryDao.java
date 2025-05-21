package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.CategoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.category.ItemCategory;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;

public class MySqlCategoryDao implements CategoryDao {

	@Override
	public ItemCategory[] getAllCategories() throws SQLException, IOException {
		var query = MySqlQueryLoader.getInstance().get("get_all_categories", "categories", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement()) {
			try (var resultSet = stmt.executeQuery(query)) {
				var categories = new ArrayList<ItemCategory>();

				while (resultSet.next()) {
					categories.add(new ItemCategory(resultSet.getInt("_item_category_id"),
							resultSet.getTimestamp("_created_at"), resultSet.getString("name")));
				}

				return categories.toArray(new ItemCategory[categories.size()]);
			}
		}
	}

}
