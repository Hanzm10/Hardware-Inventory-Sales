package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalOfSales;

public interface SalesDao {
	public TotalOfSales getTotalOfSales() throws IOException, SQLException;
}
