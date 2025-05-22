package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.CustomerPayment;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.MonthlyGross;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalItemCategorySoldInYear;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalOfSales;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.Transaction;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.WeeklyGross;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.YearlyGross;

public interface SalesDao {
	public CustomerPayment[] getCustomerPayments() throws IOException, SQLException;

	public MonthlyGross[] getMonthlyGross() throws IOException, SQLException;

	public TotalItemCategorySoldInYear[] getTotalItemCategorySoldInYear() throws IOException, SQLException;

	public TotalOfSales getTotalOfSales() throws IOException, SQLException;

	public Transaction[] getTransactionHistory() throws IOException, SQLException;

	public WeeklyGross[] getWeeklyGross() throws IOException, SQLException;

	public YearlyGross[] getYearlyGross() throws IOException, SQLException;
}
