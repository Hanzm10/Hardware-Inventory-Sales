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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.ItemDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.inventory.Item;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;

public class MySqlItemDao implements ItemDao {

	@Override
	public Item getItemById(@Range(from = 0, to = 2147483647) int itemID) throws IOException, SQLException {
		Item item = null;
		var query = MySqlQueryLoader.getInstance().get("get_item_by_itemId", "items", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				item = new Item.Builder().setItemId(resultSet.getInt("_item_id"))
						.setItemCreatedAt(resultSet.getTimestamp("_item_created_at"))
						.setItemName(resultSet.getString("item_name"))
						.setItemMinQty(resultSet.getInt("item_minimum_quantity"))
						.setItemSrp(resultSet.getFloat("item_suggested_retail_price_php"))
						.setItemWsp(resultSet.getFloat("item_wholesale_price_php"))
						.setItemCatId(resultSet.getInt("_item_category_id"))
						.setItemPckId(resultSet.getInt("_item_pack_tupe_id")).build();
			}

			statement.close();
			conn.close();
		}
		return item;
	}

	@Override
	public Item getItemByItemName(@NotNull String itemName) throws IOException, SQLException {
		Item item = null;
		var query = MySqlQueryLoader.getInstance().get("get_item_by_itemName", "items", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				item = new Item.Builder().setItemId(resultSet.getInt("_item_id"))
						.setItemCreatedAt(resultSet.getTimestamp("_item_created_at"))
						.setItemName(resultSet.getString("item_name"))
						.setItemMinQty(resultSet.getInt("item_minimum_quantity"))
						.setItemSrp(resultSet.getFloat("item_suggested_retail_price_php"))
						.setItemWsp(resultSet.getFloat("item_wholesale_price_php"))
						.setItemCatId(resultSet.getInt("_item_category_id"))
						.setItemPckId(resultSet.getInt("_item_pack_tupe_id")).build();
			}

			statement.close();
			conn.close();
		}
		return item;
	}
}
