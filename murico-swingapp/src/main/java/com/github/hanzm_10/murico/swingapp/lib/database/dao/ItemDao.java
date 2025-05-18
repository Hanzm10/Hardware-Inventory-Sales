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
package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.inventory.Item;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.item.ItemStock;
import com.github.hanzm_10.murico.swingapp.scenes.home.inventory.components.dialogs.DeleteItemsDialog.ItemToBeDeleted;

public interface ItemDao {
	public void addItem(int initQty, int minQty, String itemName, String itemDescription, int selectedCategory,
			int selectedPackaging, int selectedSupplier, BigDecimal sellingPrice, BigDecimal srp, BigDecimal costPrice)
			throws IOException, SQLException;

	public void archiveItems(@NotNull final ItemToBeDeleted[] itemsToBeDeleted, Consumer<Integer> onDelete)
			throws SQLException, IOException;

	public Item getItemById(@Range(from = 0, to = Integer.MAX_VALUE) int itemID) throws IOException, SQLException;

	public Item getItemByItemName(@NotNull String itemName) throws IOException, SQLException;

	public ItemStock[] getItemStocks() throws IOException, SQLException;

	public void updateItemStock(@Range(from = 0, to = Integer.MAX_VALUE) final int itemStockId,
			@NotNull final BigDecimal unitPrice, @Range(from = 0, to = Integer.MAX_VALUE) final int minQty)
			throws SQLException, IOException;
}
