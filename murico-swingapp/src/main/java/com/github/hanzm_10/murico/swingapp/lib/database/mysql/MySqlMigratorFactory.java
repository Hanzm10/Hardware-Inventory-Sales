package com.github.hanzm_10.murico.swingapp.lib.database.mysql;

import com.github.hanzm_10.murico.swingapp.lib.database.AbstractMigratorFactory;
import com.github.hanzm_10.murico.swingapp.lib.database.migrations.Migrator;
import com.github.hanzm_10.murico.swingapp.lib.database.migrations.MySqlMigrator;
import com.github.hanzm_10.murico.swingapp.lib.database.seeder.MySqlSeeder;
import com.github.hanzm_10.murico.swingapp.lib.database.seeder.Seeder;

public class MySqlMigratorFactory extends AbstractMigratorFactory {

	@Override
	public Migrator getMigrator() {
		return new MySqlMigrator();
	}

	@Override
	public Seeder getSeeder() {
		return new MySqlSeeder();
	}

}
