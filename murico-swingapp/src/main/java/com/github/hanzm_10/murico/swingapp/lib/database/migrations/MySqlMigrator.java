package com.github.hanzm_10.murico.swingapp.lib.database.migrations;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlMigratorFactory;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class MySqlMigrator implements Migrator {
	private static final Logger LOGGER = MuricoLogger.getLogger(MySqlMigrator.class);

	@Override
	public @NotNull String getMigrationsAsAWhole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void migrate() {
		var sqlQueries = MySqlMigratorFactory.getMigrationQueries();

		for (var sqlQuery : sqlQueries) {
		}
	}

	protected boolean migrationExists(@NotNull final int migrationVersionNumber, @NotNull final String migrationName) {
		var query = """
				SELECT EXISTS (
					SELECT FROM migrations
					WHERE version_number = ? AND name = ?
				);
				""";

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			statement.setInt(1, migrationVersionNumber);
			statement.setString(2, migrationName);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt(1) != 0;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to check if migration exists", e);
		}

		return false;
	}
}
