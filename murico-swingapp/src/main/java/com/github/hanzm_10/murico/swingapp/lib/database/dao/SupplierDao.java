package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.supplier.Supplier;

public interface SupplierDao {
	public Supplier[] getAllSuppliers() throws SQLException, IOException;
}
