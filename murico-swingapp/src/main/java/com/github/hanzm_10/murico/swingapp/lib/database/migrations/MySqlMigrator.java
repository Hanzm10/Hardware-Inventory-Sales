package com.github.hanzm_10.murico.swingapp.lib.database.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.database.SqlScriptParser;
import com.github.hanzm_10.murico.swingapp.lib.database.migrations.SqlMigration.ParsedSqlMigration;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlMigratorFactory;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class MySqlMigrator implements Migrator {
	private static final Logger LOGGER = MuricoLogger.getLogger(MySqlMigrator.class);

	protected void createMigrationTableIfNotExists() {
		var query = """
				CREATE TABLE IF NOT EXISTS migrations (
					_migration_id INTEGER PRIMARY KEY AUTO_INCREMENT,
					version_number INTEGER NOT NULL CHECK(version_number > 0),
					name VARCHAR(255) NOT NULL
				);
				""";

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			statement.executeUpdate();

			statement.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to check if migration exists", e);
		}
	}

	@Override
	public @NotNull String getMigrationsAsAWhole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void migrate() {
		var sqlQueries = MySqlMigratorFactory.getMigrationQueries();

		createMigrationTableIfNotExists();
		LOGGER.info("Performing migrations...");
		System.out.println("\n=============================================\n");

		try (var conn = MySqlFactoryDao.createConnection()) {
			conn.setAutoCommit(false);

			for (var sqlQuery : sqlQueries) {
				processMigration(conn, sqlQuery);
			}

		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to migrate", e);
		}

		System.out.println("\n=============================================\n");
	}

	protected boolean migrationExists(@NotNull final int migrationVersionNumber, @NotNull final String migrationName) {
		var query = """
				SELECT EXISTS (
					SELECT 1 FROM migrations
					WHERE version_number = ? AND name = ?
				);
				""";

		try (var conn = MySqlFactoryDao.createConnection(); var statement = conn.prepareStatement(query);) {
			statement.setInt(1, migrationVersionNumber);
			statement.setString(2, migrationName);

			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getBoolean(1);
			}

			statement.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to check if migration exists", e);
		}

		return false;
	}

	private void processMigration(Connection conn, ParsedSqlMigration sqlQuery) throws SQLException {
		System.out.print("> Migrating " + sqlQuery.migrationName() + "... ");

		if (migrationExists(sqlQuery.versionNumber(), sqlQuery.migrationName())) {
			System.out.println("already exists, skipping...");
			return;
		}

		var insertMigrationQuery = "INSERT INTO migrations (version_number, name) VALUES (?, ?);";

		try (var insertMigrationStmnt = conn.prepareStatement(insertMigrationQuery)) {
			for (var raw : splitStatements(sqlQuery.query())) {
				String trimmed = raw.trim();

				if (trimmed.isEmpty()) {
					continue;
				}

				processSqlQuery(conn, trimmed);
			}

			insertMigrationStmnt.setInt(1, sqlQuery.versionNumber());
			insertMigrationStmnt.setString(2, sqlQuery.migrationName());
			insertMigrationStmnt.executeUpdate();

			conn.commit();
			System.out.println("\n> Migration " + sqlQuery.migrationName() + " successful\n");
		} catch (SQLException e) {
			conn.rollback();
			System.out.println("\n> Migration " + sqlQuery.migrationName() + " failed\n");
			throw e;
		}
	}

	private void processSqlQuery(@NotNull Connection conn, @NotNull final String sqlQuery) throws SQLException {
		try (var stmt = conn.prepareStatement(sqlQuery)) {
			var parsed = SqlScriptParser.parseSqlStatement(sqlQuery);

			System.out.printf("\n\tType: %-8s | Name: %s", parsed.type(), parsed.name());

			stmt.executeUpdate();
		}
	}

	private List<String> splitStatements(String sqlScript) {
		List<String> statements = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		String currentDelimiter = ";";

		for (String rawLine : sqlScript.split("\\R")) {
			String line = rawLine.trim();

			// Change delimiter if directive is found
			if (line.toUpperCase().startsWith("DELIMITER ")) {
				currentDelimiter = line.substring("DELIMITER ".length()).trim();
				continue;
			}

			current.append(rawLine).append("\n");

			// Check if current buffer ends with the delimiter (ignoring trailing
			// spaces/newlines)
			String trimmed = current.toString().replaceAll("\\s+$", "");
			if (trimmed.endsWith(currentDelimiter)) {
				// Remove the delimiter from the end
				String statement = trimmed.substring(0, trimmed.length() - currentDelimiter.length()).trim();
				if (!statement.isEmpty()) {
					statements.add(statement);
				}
				current.setLength(0); // Reset for next statement
			}
		}

		// Add any remaining statement (without delimiter)
		if (!current.toString().trim().isEmpty()) {
			statements.add(current.toString().trim());
		}

		return statements;
	}
}
