module murico.database {
	requires transitive java.sql;
	requires transitive java.logging;
	requires transitive murico.core;
	requires murico.utils;
	requires murico.properties;
	requires static org.jetbrains.annotations;

	exports com.github.hanzm_10.murico.database;
	exports com.github.hanzm_10.murico.database.dao;
	exports com.github.hanzm_10.murico.database.mysql;
}
