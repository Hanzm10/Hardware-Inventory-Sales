package com.github.hanzm_10.murico.swingapp.service;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class ConnectionManager {
	private static final Logger LOGGER = MuricoLogger.getLogger(ConnectionManager.class);
	private static final ConcurrentHashMap<Thread, Statement> activeStatements = new ConcurrentHashMap<>();

	public static void cancel(Thread thread) {
		Statement stmt = activeStatements.get(thread);
		try {
			if (stmt != null && !stmt.isClosed()) {
				try {
					stmt.cancel(); // non-blocking cancel
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, "Failed to cancel database operation", e);
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to cancel database operation", e);
		}
	}

	public static void register(Thread thread, Statement stmt) {
		activeStatements.put(thread, stmt);
	}

	public static void unregister(Thread thread) {
		activeStatements.remove(thread);
	}
}
