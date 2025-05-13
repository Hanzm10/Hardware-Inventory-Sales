package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SalesDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.sales.TotalOfSales;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;

public class MySQLSalesDao implements SalesDao {

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
