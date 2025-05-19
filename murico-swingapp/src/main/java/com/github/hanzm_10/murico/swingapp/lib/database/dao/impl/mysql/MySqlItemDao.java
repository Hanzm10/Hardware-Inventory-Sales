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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.ItemDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.inventory.Item;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.ItemStock;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.DeleteItemsDialog.ItemToBeDeleted;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;

public class MySqlItemDao implements ItemDao {

	@Override
	public void addItem(int initQty, int minQty, String itemName, String itemDescription, int selectedCategory,
			int selectedPackaging, int selectedSupplier, BigDecimal sellingPrice, BigDecimal srp, BigDecimal costPrice)
			throws IOException, SQLException {
		var insertItemQuery = MySqlQueryLoader.getInstance().get("insert_item", "items", SqlQueryType.INSERT);
		var insertItemStockQuery = MySqlQueryLoader.getInstance().get("insert_item_stock", "items",
				SqlQueryType.INSERT);
		var insertItemCategoryQuery = MySqlQueryLoader.getInstance().get("insert_item_category", "items",
				SqlQueryType.INSERT);
		var insertSupplierItemQuery = MySqlQueryLoader.getInstance().get("insert_supplier_item", "items",
				SqlQueryType.INSERT);

		try (var conn = MySqlFactoryDao.createConnection();
				var statement = conn.prepareStatement(insertItemQuery, Statement.RETURN_GENERATED_KEYS);
				var statement2 = conn.prepareStatement(insertItemStockQuery);
				var statement3 = conn.prepareStatement(insertItemCategoryQuery);
				var statement4 = conn.prepareStatement(insertSupplierItemQuery);) {
			conn.setAutoCommit(false);

			ConnectionManager.register(Thread.currentThread(), statement);

			int generatedItemItemId = -1;

			try {
				statement.setString(1, itemName);
				statement.setString(2, itemDescription);
				statement.executeUpdate();

				var generatedKeys = statement.getGeneratedKeys();

				if (generatedKeys.next()) {
					generatedItemItemId = generatedKeys.getInt(1);
				}
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				ConnectionManager.unregister(Thread.currentThread());
			}

			if (generatedItemItemId == -1) {
				throw new SQLException("Failed to retrieve generated item ID.");
			}

			ConnectionManager.register(Thread.currentThread(), statement2);

			statement2.setInt(1, generatedItemItemId);
			statement2.setInt(2, selectedPackaging);
			statement2.setInt(3, initQty);
			statement2.setInt(4, minQty);
			statement2.setBigDecimal(5, srp);
			statement2.setBigDecimal(6, sellingPrice);

			try {
				statement2.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				ConnectionManager.unregister(Thread.currentThread());
			}

			ConnectionManager.register(Thread.currentThread(), statement3);

			statement3.setInt(1, selectedCategory);
			statement3.setInt(2, generatedItemItemId);

			try {
				statement3.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				ConnectionManager.unregister(Thread.currentThread());
			}

			ConnectionManager.register(Thread.currentThread(), statement4);
			statement4.setInt(1, selectedSupplier);
			statement4.setInt(2, generatedItemItemId);
			statement4.setBigDecimal(3, srp);
			statement4.setBigDecimal(4, costPrice);

			try {
				statement4.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				ConnectionManager.unregister(Thread.currentThread());
			}

			conn.commit();
		}
	}

	@Override
	public void archiveItems(@NotNull ItemToBeDeleted[] itemsToBeDeleted, Consumer<Integer> onDelete)
			throws SQLException, IOException {
		var delItemQuery = MySqlQueryLoader.getInstance().get("archive_item", "items", SqlQueryType.DELETE);
		var delItemStockQuery = MySqlQueryLoader.getInstance().get("archive_item_stock", "items", SqlQueryType.DELETE);

		try (var conn = MySqlFactoryDao.createConnection();
				var statement = conn.prepareStatement(delItemQuery);
				var statement2 = conn.prepareStatement(delItemStockQuery);) {
			conn.setAutoCommit(false);

			ConnectionManager.register(Thread.currentThread(), statement);

			for (int i = 0; i < itemsToBeDeleted.length; i++) {
				try {
					statement.setInt(1, itemsToBeDeleted[i].itemId());
					statement.executeUpdate();

					ConnectionManager.unregister(Thread.currentThread());
					ConnectionManager.register(Thread.currentThread(), statement2);

					statement2.setInt(1, itemsToBeDeleted[i].itemStockId());
					statement2.executeUpdate();

					if (onDelete != null) {
						onDelete.accept(i);
					}
				} catch (SQLException e) {
					conn.rollback();
					throw e;
				} finally {
					ConnectionManager.unregister(Thread.currentThread());
				}
			}

			conn.commit();
		} finally {
			ConnectionManager.unregister(Thread.currentThread());
		}
	}

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

		}
		return item;
	}

	@Override
	public ItemStock[] getItemStocks() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_item_stocks", "items", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {
			ConnectionManager.register(Thread.currentThread(), stmt);

			var resultSet = stmt.executeQuery(query);
			var result = new ArrayList<ItemStock>();

			while (resultSet.next()) {
				result.add(new ItemStock(resultSet.getInt("_item_stock_id"), resultSet.getInt("_item_id"),
						stringOrNA(resultSet.getString("category_type_name")),
						stringOrNA(resultSet.getString("packaging_type_name")),
						stringOrNA(resultSet.getString("supplier_name")), stringOrNA(resultSet.getString("item_name")),
						resultSet.getInt("stock_quantity"), resultSet.getBigDecimal("unit_price_php"),
						resultSet.getInt("minimum_quantity")));
			}

			return result.toArray(new ItemStock[result.size()]);
		} finally {
			ConnectionManager.unregister(Thread.currentThread());
		}
	}

	@Override
	public void restockItem(@Range(from = 0, to = 2147483647) int itemStockId,
			@Range(from = 0, to = 2147483647) int quantityToAdd, @Range(from = 0, to = 2147483647) int currentQuantity)
			throws SQLException, IOException {
		var restockItemQuery = MySqlQueryLoader.getInstance().get("restock_item_stocks", "items", SqlQueryType.UPDATE);
		var insertItemRestocksQuery = MySqlQueryLoader.getInstance().get("insert_item_restock", "items",
				SqlQueryType.INSERT);
		var newQuantity = currentQuantity + quantityToAdd;

		try (var conn = MySqlFactoryDao.createConnection();
				var statement = conn.prepareStatement(restockItemQuery);
				var statement2 = conn.prepareStatement(insertItemRestocksQuery);) {
			conn.setAutoCommit(false);

			ConnectionManager.register(Thread.currentThread(), statement);

			statement.setInt(1, newQuantity);
			statement.setInt(2, itemStockId);

			try {
				statement.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				ConnectionManager.unregister(Thread.currentThread());
			}

			ConnectionManager.register(Thread.currentThread(), statement2);

			statement2.setInt(1, itemStockId);
			statement2.setInt(2, currentQuantity);
			statement2.setInt(3, newQuantity);
			statement2.setInt(4, quantityToAdd);

			try {
				statement2.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				ConnectionManager.unregister(Thread.currentThread());
			}

			conn.commit();
		} finally {
			ConnectionManager.unregister(Thread.currentThread());
		}
	}

	private String stringOrNA(String name) {
		return name == null ? "N/A" : name;
	}

	@Override
	public void updateItemStock(@Range(from = 0, to = 2147483647) int itemStockId, @NotNull BigDecimal unitPrice,
			@Range(from = 0, to = 2147483647) int minQty) throws SQLException, IOException {
		var query = MySqlQueryLoader.getInstance().get("update_item_stock", "items", SqlQueryType.UPDATE);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			ConnectionManager.register(Thread.currentThread(), statement);

			statement.setBigDecimal(1, unitPrice);
			statement.setInt(2, minQty);
			statement.setInt(3, itemStockId);

			statement.executeUpdate();
		} finally {
			ConnectionManager.unregister(Thread.currentThread());
		}
	}
}
