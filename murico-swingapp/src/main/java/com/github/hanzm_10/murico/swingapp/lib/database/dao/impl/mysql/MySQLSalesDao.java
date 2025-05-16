package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SalesDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.CustomerPayment;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalItemCategorySoldInYear;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalOfSales;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;

public class MySQLSalesDao implements SalesDao {

	@Override
	public CustomerPayment[] getCustomerPayments() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_customer_payments", "sales", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {
			ConnectionManager.register(Thread.currentThread(), stmt);

			var result = new ArrayList<CustomerPayment>();

			try (var resultSet = stmt.executeQuery(query)) {
				while (resultSet.next()) {
					result.add(new CustomerPayment(resultSet.getInt("_customer_payment_id"),
							resultSet.getInt("_customer_order_id"), resultSet.getTimestamp("_created_at"),
							resultSet.getString("payment_method"), resultSet.getDouble("amount_php")));
				}

				return result.toArray(new CustomerPayment[result.size()]);
			} finally {
				ConnectionManager.unregister(Thread.currentThread());
			}
		}
	}

	@Override
	public TotalItemCategorySoldInYear[] getTotalItemCategorySoldInYear() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_total_item_categories_sold_per_year", "sales",
				SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement();) {
			ConnectionManager.register(Thread.currentThread(), stmt);

			var result = new ArrayList<TotalItemCategorySoldInYear>();

			try (var resultSet = stmt.executeQuery(query)) {
				while (resultSet.next()) {
					result.add(new TotalItemCategorySoldInYear(resultSet.getString("year"),
							resultSet.getString("category_name"), resultSet.getInt("total_quantity")));
				}

				return result.toArray(new TotalItemCategorySoldInYear[result.size()]);
			} finally {
				ConnectionManager.unregister(Thread.currentThread());
			}
		}
	}

	@Override
	public TotalOfSales getTotalOfSales() throws IOException, SQLException {
		TotalOfSales totalOfSales = null;
		var query = MySqlQueryLoader.getInstance().get("get_total_of_sales", "sales", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmnt = conn.createStatement()) {
			ConnectionManager.register(Thread.currentThread(), stmnt);

			try (var resultSet = stmnt.executeQuery(query)) {
				if (resultSet.next()) {
					totalOfSales = new TotalOfSales(resultSet.getDouble("total_revenue"),
							resultSet.getDouble("total_net_sales"), resultSet.getDouble("total_gross_sales"),
							resultSet.getInt("total_items_sold"));
				}

				resultSet.close();
			} finally {
				ConnectionManager.unregister(Thread.currentThread());
			}
		}

		return totalOfSales;
	}

}
