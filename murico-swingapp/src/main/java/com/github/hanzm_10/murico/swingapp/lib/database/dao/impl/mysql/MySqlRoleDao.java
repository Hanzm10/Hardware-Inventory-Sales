package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.RoleDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.role.Role;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;

public class MySqlRoleDao implements RoleDao {

	@Override
	public Role[] getRolesOfUserById(@Range(from = 0, to = 2147483647) int _userId) throws IOException, SQLException {
		List<Role> roles = new ArrayList<Role>();
		var query = MySqlQueryLoader.getInstance().get("get_user_roles_by_id", "users_roles", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query)) {
			statement.setInt(1, _userId);

			var resultSet = statement.executeQuery();

			while (resultSet.next()) {
				roles.add(new Role(_userId, resultSet.getTimestamp("_createdAt"), resultSet.getTimestamp("updated_at"),
						resultSet.getString("name"), resultSet.getString("description")));
			}

		}

		return (Role[]) roles.toArray();
	}

}
