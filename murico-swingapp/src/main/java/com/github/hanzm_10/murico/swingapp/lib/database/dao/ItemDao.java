package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.inventory.Item;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;

public interface InventoryDao {
	public Item getItemByItemName(@NotNull String itemName)throws IOException, SQLException;
	public Item getItemById(@Range(from = 0, to = Integer.MAX_VALUE) int itemID) throws IOException, SQLException;
}
