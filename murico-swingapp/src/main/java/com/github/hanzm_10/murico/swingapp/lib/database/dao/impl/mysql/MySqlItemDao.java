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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.NamedPrepareStatement;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.ItemDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.inventory.Item;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.InventoryBreakdown;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.InventorySummary;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.ItemQuantityPerPackaging;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.ItemStock;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.DeleteItemsDialog.ItemToBeDeleted;

public class MySqlItemDao implements ItemDao {

	@Override
	public GeneratedItemStockIds addItem(int initQty, int minQty, String itemName, String itemDescription,
			int selectedCategory, int selectedPackaging, int selectedSupplier, BigDecimal sellingPrice, BigDecimal srp,
			BigDecimal costPrice) throws IOException, SQLException {
		var insertItemQuery = MySqlQueryLoader.getInstance().get("insert_item", "items", SqlQueryType.INSERT);
		var insertItemStockQuery = MySqlQueryLoader.getInstance().get("insert_item_stock", "items",
				SqlQueryType.INSERT);
		var insertItemCategoryQuery = MySqlQueryLoader.getInstance().get("insert_item_category", "items",
				SqlQueryType.INSERT);
		var insertSupplierItemQuery = MySqlQueryLoader.getInstance().get("insert_supplier_item", "items",
				SqlQueryType.INSERT);

		try (var conn = MySqlFactoryDao.createConnection();
				var statement = conn.prepareStatement(insertItemQuery, Statement.RETURN_GENERATED_KEYS);
				var statement2 = conn.prepareStatement(insertItemStockQuery, Statement.RETURN_GENERATED_KEYS);
				var statement3 = conn.prepareStatement(insertItemCategoryQuery);
				var statement4 = conn.prepareStatement(insertSupplierItemQuery);) {
			conn.setAutoCommit(false);
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
			}

			if (generatedItemItemId == -1) {
				throw new SQLException("Failed to retrieve generated item ID.");
			}

			statement2.setInt(1, generatedItemItemId);
			statement2.setInt(2, selectedPackaging);
			statement2.setInt(3, initQty);
			statement2.setInt(4, minQty);
			statement2.setBigDecimal(5, srp);
			statement2.setBigDecimal(6, sellingPrice);

			int generatedItemStockId = -1;

			try {
				statement2.executeUpdate();

				var generatedKeys = statement2.getGeneratedKeys();

				if (generatedKeys.next()) {
					generatedItemStockId = generatedKeys.getInt(1);
				}
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}

			if (generatedItemStockId == -1) {
				throw new SQLException("Failed to retrieve generated item stock ID.");
			}

			statement3.setInt(1, selectedCategory);
			statement3.setInt(2, generatedItemItemId);

			try {
				statement3.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}

			statement4.setInt(1, selectedSupplier);
			statement4.setInt(2, generatedItemItemId);
			statement4.setBigDecimal(3, srp);
			statement4.setBigDecimal(4, costPrice);

			try {
				statement4.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}

			conn.commit();

			return new GeneratedItemStockIds(generatedItemItemId, generatedItemStockId);
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

			for (int i = 0; i < itemsToBeDeleted.length; i++) {
				try {
					statement.setInt(1, itemsToBeDeleted[i].itemId());
					statement.executeUpdate();

					statement2.setInt(1, itemsToBeDeleted[i].itemStockId());
					statement2.executeUpdate();

					if (onDelete != null) {
						onDelete.accept(i);
					}
				} catch (SQLException e) {
					conn.rollback();
					throw e;
				}
			}

			conn.commit();
		}
	}

	@Override
	public InventoryBreakdown[] getInventoryBreakdowns(@NotNull LocalDate fromDate, @NotNull LocalDate toDate)
			throws SQLException, IOException {
		var query = MySqlQueryLoader.getInstance().get("get_inventory_breakdown", "items", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var namedStmt = new NamedPrepareStatement(conn, query);) {
			namedStmt.setTimestamp("start_date", Timestamp.valueOf(fromDate.atStartOfDay()));
			namedStmt.setTimestamp("end_date", Timestamp.valueOf(toDate.atStartOfDay()));

			var resultSet = namedStmt.executeQuery();
			var result = new ArrayList<InventoryBreakdown>();

			while (resultSet.next()) {
				result.add(new InventoryBreakdown(resultSet.getInt("_item_id"), resultSet.getString("item_name"),
						resultSet.getString("category_type"), resultSet.getString("packaging_name"),
						resultSet.getInt("initial_item_quantity"), resultSet.getInt("amount_of_items_sold"),
						resultSet.getInt("amount_of_items_restocked"), resultSet.getInt("current_item_quantity"),
						InventoryBreakdown.InventoryBreakdownRemarks.fromCurrAndMin(
								resultSet.getInt("current_item_quantity"), resultSet.getInt("minimum_quantity"))));
			}

			return result.toArray(new InventoryBreakdown[result.size()]);
		}
	}

	@Override
	public InventorySummary getInventorySummary() throws SQLException, IOException {
		var query = MySqlQueryLoader.getInstance().get("get_inventory_summary", "items", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {

			var resultSet = stmt.executeQuery(query);
			InventorySummary inventorySummary = null;

			if (resultSet.next()) {
				inventorySummary = new InventorySummary(resultSet.getInt("total_inventory_value"),
						resultSet.getInt("total_items_in_stock"), resultSet.getInt("total_items_below_critical_level"),
						resultSet.getBigDecimal("avg_quantity_per_item"));
			}

			return inventorySummary;
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
	public ItemQuantityPerPackaging[] getItemsQuantityPerPackaging() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_items_quantity_per_packaging", "items",
				SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {

			var resultSet = stmt.executeQuery(query);
			var result = new ArrayList<ItemQuantityPerPackaging>();

			int currentItemId = -1;
			String currentItemNameString = null;
			var packagingQuantities = new ArrayList<ItemQuantityPerPackaging.PackagingQuantity>();

			while (resultSet.next()) {
				var itemId = resultSet.getInt("_item_id");

				if (itemId != currentItemId) {
					if (packagingQuantities.size() != 0) {
						result.add(new ItemQuantityPerPackaging(currentItemId, currentItemNameString,
								packagingQuantities.toArray(
										new ItemQuantityPerPackaging.PackagingQuantity[packagingQuantities.size()])));
					}

					currentItemId = itemId;
					currentItemNameString = resultSet.getString("item_name");
					packagingQuantities.clear();
				}

				packagingQuantities
						.add(new ItemQuantityPerPackaging.PackagingQuantity(resultSet.getInt("_packaging_id"),
								resultSet.getString("packaging_name"), resultSet.getInt("packaging_quantity")));
			}

			if (packagingQuantities.size() != 0) {
				result.add(new ItemQuantityPerPackaging(currentItemId, currentItemNameString, packagingQuantities
						.toArray(new ItemQuantityPerPackaging.PackagingQuantity[packagingQuantities.size()])));
			}

			return result.toArray(new ItemQuantityPerPackaging[result.size()]);
		}
	}

	@Override
	public ItemStock[] getItemStocks() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_item_stocks", "items", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {
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

			statement.setInt(1, newQuantity);
			statement.setInt(2, itemStockId);

			try {
				statement.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
			statement2.setInt(1, itemStockId);
			statement2.setInt(2, currentQuantity);
			statement2.setInt(3, newQuantity);
			statement2.setInt(4, quantityToAdd);

			try {
				statement2.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}

			conn.commit();
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

			statement.setBigDecimal(1, unitPrice);
			statement.setInt(2, minQty);
			statement.setInt(3, itemStockId);

			statement.executeUpdate();
		}
	}
}
