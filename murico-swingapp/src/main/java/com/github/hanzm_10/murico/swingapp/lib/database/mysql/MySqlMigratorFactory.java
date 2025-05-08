package com.github.hanzm_10.murico.swingapp.lib.database.mysql;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractMigratorFactory;
import com.github.hanzm_10.murico.swingapp.lib.database.migrations.Migrator;
import com.github.hanzm_10.murico.swingapp.lib.database.migrations.MySqlMigrator;
import com.github.hanzm_10.murico.swingapp.lib.database.migrations.SqlMigration;
import com.github.hanzm_10.murico.swingapp.lib.database.migrations.SqlMigration.ParsedSqlMigration;
import com.github.hanzm_10.murico.swingapp.lib.database.seeder.MySqlSeeder;
import com.github.hanzm_10.murico.swingapp.lib.database.seeder.Seeder;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class MySqlMigratorFactory extends AbstractMigratorFactory {

	/**
	 * Can be refactored but it doesn't really matter.
	 *
	 * @return A list of migration sql queries arranged by the order of file
	 *         versions.
	 */
	public static @NotNull ArrayList<ParsedSqlMigration> getMigrationQueries() {
		LOGGER.info("Loading sql migrations...");

		try (ScanResult scanResult = new ClassGraph().acceptModules(MySqlMigrator.class.getModule().toString())
				.acceptPackages(MySqlMigrator.class.getPackageName())
				.acceptPaths("META-INF/lib/database/migrations/mysql").scan()) {
			var queries = new ArrayList<SqlMigration>();
			var pattern = Pattern.compile("^V(\\d+)__(.+)\\.(\\w+)$");

			scanResult.getResourcesWithExtension(".sql").forEach((res) -> {
				try {
					queries.add(new SqlMigration(
							Paths.get(res.getPathRelativeToClasspathElement()).getFileName().toString(),
							res.getContentAsString()));
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "Failed to read sql file", e);
				}
			});

			var validQueries = new ArrayList<ParsedSqlMigration>();

			for (var sqlMigration : queries) {
				var fileName = sqlMigration.fileName();
				var matcher = pattern.matcher(fileName);

				if (matcher.matches()) {
					var versionNumber = Integer.parseInt(matcher.group(1));
					var queryName = matcher.group(2);
					var extension = matcher.group(3);

					validQueries.add(
							new ParsedSqlMigration(versionNumber, queryName + "." + extension, sqlMigration.query()));
				} else {
					LOGGER.warning("⚠️ Warning: Invalid migration format: " + fileName + ". skipping...");
				}
			}

			validQueries.sort(Comparator.comparingInt((sqlM) -> sqlM.versionNumber()));

			return validQueries;
		}
	}

	@Override
	public Migrator getMigrator() {
		return new MySqlMigrator();
	}

	@Override
	public Seeder getSeeder() {
		return new MySqlSeeder();
	}

}
