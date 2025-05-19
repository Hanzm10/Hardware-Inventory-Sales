package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.sql.SQLException;

import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.supplier.ItemSupplierNameWsp;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.supplier.Supplier;

public interface SupplierDao {
	public Supplier[] getAllSuppliers() throws SQLException, IOException;

	public ItemSupplierNameWsp[] getAllSuppliersForItem(@Range(from = 0, to = Integer.MAX_VALUE) final int _itemId)
			throws SQLException, IOException;
}
