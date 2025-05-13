package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.CustomerPayment;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalItemCategorySoldInYear;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalOfSales;

public interface SalesDao {
	public CustomerPayment[] getCustomerPayments() throws IOException, SQLException;

	public TotalItemCategorySoldInYear[] getTotalItemCategorySoldInYear() throws IOException, SQLException;

	public TotalOfSales getTotalOfSales() throws IOException, SQLException;
}
