package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.ItemDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.inventory.Item;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class MySqlItemDao implements ItemDao {
	private static final Logger LOGGER = MuricoLogger.getLogger(MySqlItemDao.class);

	@Override
	public Item getItemByItemName(@NotNull String itemName) throws IOException, SQLException {
		Item item = null;
		var query = MySqlQueryLoader.getInstance().get("get_item_by_itemName", "items", SqlQueryType.SELECT);
		
		try(var conn = MySqlFactoryDao.createConnection();
			var statement = conn.prepareStatement(query);){
			
			var resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				item = new Item.Builder().setItemId(resultSet.getInt("_item_id"))
						.setItemCreatedAt(resultSet.getTimestamp("_item_created_at"))
						.setItemName(resultSet.getString("item_name"))
						.setItemMinQty(resultSet.getInt("item_minimum_quantity"))
						.setItemSrp(resultSet.getFloat("item_suggested_retail_price_php"))
						.setItemWsp(resultSet.getFloat("item_wholesale_price_php"))
						.setItemCatId(resultSet.getInt("_item_category_id"))
						.setItemPckId(resultSet.getInt("_item_pack_tupe_id")).build();
			}
			
					
		}
		return item;
	}

	@Override
	public Item getItemById(@Range(from = 0, to = 2147483647) int itemID) throws IOException, SQLException {
		Item item = null;
		var query = MySqlQueryLoader.getInstance().get("get_item_by_itemId", "items", SqlQueryType.SELECT);
		
		try(var conn = MySqlFactoryDao.createConnection();
			var statement = conn.prepareStatement(query);){
			
			var resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				item = new Item.Builder().setItemId(resultSet.getInt("_item_id"))
						.setItemCreatedAt(resultSet.getTimestamp("_item_created_at"))
						.setItemName(resultSet.getString("item_name"))
						.setItemMinQty(resultSet.getInt("item_minimum_quantity"))
						.setItemSrp(resultSet.getFloat("item_suggested_retail_price_php"))
						.setItemWsp(resultSet.getFloat("item_wholesale_price_php"))
						.setItemCatId(resultSet.getInt("_item_category_id"))
						.setItemPckId(resultSet.getInt("_item_pack_tupe_id")).build();
			}
			
					
		}
		return item;
	}
}