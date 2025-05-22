package com.github.hanzm_10.murico.swingapp.lib.database.dao.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.hanzm_10.murico.swingapp.lib.database.NamedPrepareStatement;
import com.github.hanzm_10.murico.swingapp.lib.database.dao.RoleDao;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.role.Role;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlQueryLoader;

public class MySqlRoleDao implements RoleDao {

	@Override
	public Role[] getRoles() throws IOException, SQLException {
		var query = MySqlQueryLoader.getInstance().get("get_roles", "roles", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query)) {
			var resultSet = statement.executeQuery();
			List<Role> roles = new ArrayList<Role>();

			while (resultSet.next()) {
				roles.add(new Role(resultSet.getInt("_role_id"), resultSet.getTimestamp("_created_at"),
						resultSet.getTimestamp("updated_at"), resultSet.getString("name"),
						resultSet.getString("description")));
			}

			return roles.toArray(new Role[roles.size()]);
		}
	}

	@Override
	public Role[] getRolesOfUserById(@Range(from = 0, to = 2147483647) int _userId) throws IOException, SQLException {
		List<Role> roles = new ArrayList<Role>();
		var query = MySqlQueryLoader.getInstance().get("get_user_roles_by_id", "users_roles", SqlQueryType.SELECT);

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query)) {
			statement.setInt(1, _userId);

			var resultSet = statement.executeQuery();

			while (resultSet.next()) {
				roles.add(new Role(_userId, resultSet.getTimestamp("_created_at"), resultSet.getTimestamp("updated_at"),
						resultSet.getString("name"), resultSet.getString("description")));
			}

		}

		return roles.toArray(new Role[roles.size()]);
	}

	@Override
	public void updateUserRoles(@Range(from = 0, to = 2147483647) int _userId, Role[] roles)
			throws IOException, SQLException {
		var insertQuery = MySqlQueryLoader.getInstance().get("insert_user_roles", "roles", SqlQueryType.INSERT);
		var deleteQuery = MySqlQueryLoader.getInstance().get("delete_user_roles", "roles", SqlQueryType.DELETE);

		try (var conn = MySqlFactoryDao.createConnection();
				var deleteStatement = new NamedPrepareStatement(conn, deleteQuery);
				var insertStatement = new NamedPrepareStatement(conn, insertQuery)) {
			conn.setAutoCommit(false);

			try {
				deleteStatement.setInt("user_id", _userId);
				deleteStatement.getPreparedStatement().executeUpdate();

				for (var role : roles) {
					insertStatement.setInt("user_id", _userId);
					insertStatement.setInt("role_id", role._roleId());

					insertStatement.getPreparedStatement().executeUpdate();
				}
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}

			conn.commit();
		}
	}

}
