module murico.swingapp {
	requires transitive java.desktop;
	requires transitive java.logging;
	requires transitive java.sql;
	requires static org.jetbrains.annotations;
	requires murico.lookandfeel;

	exports com.github.hanzm_10.murico.swingapp;
	exports com.github.hanzm_10.murico.swingapp.lib.database;
	exports com.github.hanzm_10.murico.swingapp.lib.database.dao;
	exports com.github.hanzm_10.murico.swingapp.lib.database.mysql;
	exports com.github.hanzm_10.murico.swingapp.lib.database.mysql.dao;
	exports com.github.hanzm_10.murico.swingapp.lib.database.entity;
	exports com.github.hanzm_10.murico.swingapp.lib.logger;
	exports com.github.hanzm_10.murico.swingapp.lib.cache;
	exports com.github.hanzm_10.murico.swingapp.constants;
	exports com.github.hanzm_10.murico.swingapp.config;
	exports com.github.hanzm_10.murico.swingapp.lib.auth;
	exports com.github.hanzm_10.murico.swingapp.lib.io;
	exports com.github.hanzm_10.murico.swingapp.lib.platform;
	exports com.github.hanzm_10.murico.swingapp.lib.utils;
	exports com.github.hanzm_10.murico.swingapp.exceptions;
	exports com.github.hanzm_10.murico.swingapp.exceptions.handlers;
	exports com.github.hanzm_10.murico.swingapp.lib.database.entity.user;
	exports com.github.hanzm_10.murico.swingapp.lib.validation;
}
