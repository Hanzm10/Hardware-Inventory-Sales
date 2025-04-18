module murico.database {
	requires transitive java.sql;
	requires transitive java.logging;
	requires transitive murico.core;
	requires static org.jetbrains.annotations;
	requires darklaf.properties;
	requires murico.utils;

	exports com.github.hanzm_10.murico.database;
	exports com.github.hanzm_10.murico.database.dao;
	exports com.github.hanzm_10.murico.database.mysql;
}
