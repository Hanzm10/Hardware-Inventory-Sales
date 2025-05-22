package com.github.hanzm_10.murico.swingapp.lib.database.seeder;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

import com.github.hanzm_10.murico.swingapp.MuricoSwingApp;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlFactoryDao;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlMigratorFactory;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class MySqlSeeder implements Seeder {
	private static final Logger LOGGER = MuricoLogger.getLogger(MySqlSeeder.class);

	@Override
	public void seed() throws SQLException {
		LOGGER.info("Seeding database...");

		var query = MySqlMigratorFactory.getSeederQuery();

		if (query == null) {
			LOGGER.warning("Seeder query not found. Stopping...");
			return;
		}

		if (MuricoSwingApp.IS_DEVELOPMENT) {
			System.out.println("\n===================================================\n");
		}

		try (var conn = MySqlFactoryDao.createConnection();) {
			conn.setAutoCommit(false);

			try (var stmnt = conn.createStatement()) {
				var queries = Arrays.stream(query.split(";")).filter(s -> !s.isBlank()).toArray(String[]::new);

				if (MuricoSwingApp.IS_DEVELOPMENT) {
					System.out.println("> Starting to seed for " + queries.length + " operations...\n");
				}

				for (int i = 0; i < queries.length; ++i) {
					var q = queries[i].trim();

					if (MuricoSwingApp.IS_DEVELOPMENT) {
						System.out.println("[" + (i + 1) + "/" + queries.length + "] " + q);
					}

					try {
						stmnt.execute(q);
						conn.commit();

						if (MuricoSwingApp.IS_DEVELOPMENT) {
							System.out.println("success!");
						}
					} catch (SQLException e) {
						if (MuricoSwingApp.IS_DEVELOPMENT) {
							System.out.println("fail! " + e.getMessage());
						}

						conn.rollback();
					}
				}
			}
		}

		if (MuricoSwingApp.IS_DEVELOPMENT) {
			System.out.println("\n===================================================\n");
		}

	}
}
