package com.github.hanzm_10.murico.swingapp.lib.database;

import java.util.logging.Logger;

import com.github.hanzm_10.murico.swingapp.lib.database.migrations.Migrator;
import com.github.hanzm_10.murico.swingapp.lib.database.mysql.MySqlMigratorFactory;
import com.github.hanzm_10.murico.swingapp.lib.database.seeder.Seeder;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public abstract class AbstractMigratorFactory {
	protected static final Logger LOGGER = MuricoLogger.getLogger(AbstractMigratorFactory.class);
	public static final int MYSQL = 1;

	public static AbstractMigratorFactory getSqlFactoryDao(int type) {
		return switch (type) {
		case MYSQL -> new MySqlMigratorFactory();
		default -> throw new IllegalArgumentException("Invalid database type: " + type);
		};
	}

	public abstract Migrator getMigrator();

	public abstract Seeder getSeeder();
}
