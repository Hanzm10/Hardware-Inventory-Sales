package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SalesDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.CustomerPayment;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.MonthlyGross;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.MonthlyGross.Month;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalItemCategorySoldInYear;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalOfSales;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.Transaction;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.WeeklyGross;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.WeeklyGross.WeekDays;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.YearlyGross;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;

public class MySQLSalesDao implements SalesDao {

	@Override
	public CustomerPayment[] getCustomerPayments() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_customer_payments", "sales", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {
			var result = new ArrayList<CustomerPayment>();
			var resultSet = stmt.executeQuery(query);

			while (resultSet.next()) {
				result.add(new CustomerPayment(resultSet.getInt("_customer_payment_id"),
						resultSet.getInt("_customer_order_id"), resultSet.getTimestamp("_created_at"),
						resultSet.getString("payment_method"), resultSet.getDouble("amount_php")));
			}

			return result.toArray(new CustomerPayment[result.size()]);
		}
	}

	@Override
	public MonthlyGross[] getMonthlyGross() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_monthly_sales", "sales", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {
			var result = new ArrayList<MonthlyGross>();
			var resultSet = stmt.executeQuery(query);

			while (resultSet.next()) {
				result.add(new MonthlyGross(Month.fromString(resultSet.getString("month")),
						resultSet.getBigDecimal("total_gross")));
			}

			return result.toArray(new MonthlyGross[result.size()]);
		}
	}

	@Override
	public TotalItemCategorySoldInYear[] getTotalItemCategorySoldInYear() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_total_item_categories_sold_per_year", "sales",
				SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {
			var result = new ArrayList<TotalItemCategorySoldInYear>();
			var resultSet = stmt.executeQuery(query);

			while (resultSet.next()) {
				result.add(new TotalItemCategorySoldInYear(resultSet.getString("year"),
						resultSet.getString("category_name"), resultSet.getInt("total_quantity")));
			}

			return result.toArray(new TotalItemCategorySoldInYear[result.size()]);
		}
	}

	@Override
	public TotalOfSales getTotalOfSales() throws IOException, SQLException {
		TotalOfSales totalOfSales = null;
		var query = MySqlQueryLoader.getInstance().get("get_total_of_sales", "sales", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmnt = conn.createStatement()) {
			var resultSet = stmnt.executeQuery(query);

			if (resultSet.next()) {
				totalOfSales = new TotalOfSales(resultSet.getDouble("total_revenue"),
						resultSet.getDouble("total_net_sales"), resultSet.getDouble("total_gross_sales"),
						resultSet.getInt("total_items_sold"));
			}

			resultSet.close();
		}

		return totalOfSales;
	}

	@Override
	public Transaction[] getTransactionHistory() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_transactions", "sales", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {
			var result = new ArrayList<Transaction>();
			var resultSet = stmt.executeQuery(query);

			while (resultSet.next()) {
				result.add(new Transaction(resultSet.getTimestamp("transaction_date"),
						resultSet.getString("transaction_type"), resultSet.getInt("ref_id"),
						resultSet.getString("order_number"), resultSet.getBigDecimal("amount"),
						resultSet.getString("employee_handler"), resultSet.getString("party"),
						resultSet.getString("status")));
			}

			return result.toArray(new Transaction[result.size()]);
		}
	}

	@Override
	public WeeklyGross[] getWeeklyGross() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_weekly_sales", "sales", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {
			var result = new ArrayList<WeeklyGross>();
			var resultSet = stmt.executeQuery(query);

			while (resultSet.next()) {
				result.add(new WeeklyGross(WeekDays.fromString(resultSet.getString("weekday")),
						resultSet.getBigDecimal("total_gross")));
			}

			return result.toArray(new WeeklyGross[result.size()]);
		}
	}

	@Override
	public YearlyGross[] getYearlyGross() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_yearly_sales", "sales", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {
			var result = new ArrayList<YearlyGross>();
			var resultSet = stmt.executeQuery(query);

			while (resultSet.next()) {
				result.add(new YearlyGross(resultSet.getString("year"), resultSet.getBigDecimal("total_gross")));
			}

			return result.toArray(new YearlyGross[result.size()]);
		}
	}

}
