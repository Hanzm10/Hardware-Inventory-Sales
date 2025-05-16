package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.SupplierDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.supplier.Supplier;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;
import com.github.hanzm_10.murico.swingapp.service.ConnectionManager;

public class MySqlSupplierDao implements SupplierDao {

	@Override
	public Supplier[] getAllSuppliers() throws SQLException, IOException {
		var query = MySqlQueryLoader.getInstance().get("get_all_suppliers", "suppliers", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var stmt = conn.createStatement()) {
			ConnectionManager.register(Thread.currentThread(), stmt);

			try (var resultSet = stmt.executeQuery(query)) {
				var suppliers = new ArrayList<Supplier>();

				while (resultSet.next()) {
					suppliers.add(new Supplier(resultSet.getInt("_supplier_id"), resultSet.getTimestamp("_created_at"),
							resultSet.getTimestamp("updated_at"), resultSet.getString("name"),
							resultSet.getString("email"), resultSet.getString("street"), resultSet.getString("city"),
							resultSet.getString("postal_code"), resultSet.getString("country")));
				}

				return suppliers.toArray(new Supplier[suppliers.size()]);
			} finally {
				ConnectionManager.unregister(Thread.currentThread());
			}
		}
	}

}
