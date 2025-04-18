module murico.core {
	requires transitive java.desktop;
	requires transitive java.sql;
	requires static org.jetbrains.annotations;
	requires murico.utils;
	requires murico.io;
	requires darklaf.properties;

	exports com.github.hanzm_10.murico.core;
	exports com.github.hanzm_10.murico.core.model;
	exports com.github.hanzm_10.murico.core.constants;
	exports com.github.hanzm_10.murico.core.config;
	exports com.github.hanzm_10.murico.core.exceptions;
}
