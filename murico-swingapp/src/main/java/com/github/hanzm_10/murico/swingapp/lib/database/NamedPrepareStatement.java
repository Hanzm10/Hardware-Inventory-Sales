package com.github.hanzm_10.murico.swingapp.lib.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamedPrepareStatement implements AutoCloseable {
	private final PreparedStatement prepStmt;
	private final Map<String, List<Integer>> fields = new HashMap<>();

	public NamedPrepareStatement(final Connection conn, String sql) throws SQLException {
		StringBuilder parsedSql = new StringBuilder();
		int length = sql.length();
		int index = 1; // JDBC parameter index starts at 1

		for (int i = 0; i < length;) {
			char c = sql.charAt(i);
			if (c == ':') {
				int j = i + 1;
				// Parameter name chars: letters, digits, underscore
				while (j < length && (Character.isLetterOrDigit(sql.charAt(j)) || sql.charAt(j) == '_')) {
					j++;
				}
				String name = sql.substring(i + 1, j);

				// Replace :name with ?
				parsedSql.append('?');

				// Store index for this parameter name
				fields.computeIfAbsent(name, _ -> new ArrayList<>()).add(index++);
				i = j; // advance past the parameter name
			} else {
				parsedSql.append(c);
				i++;
			}
		}

		prepStmt = conn.prepareStatement(parsedSql.toString());
	}

	@Override
	public void close() throws SQLException {
		prepStmt.close();
	}

	public ResultSet executeQuery() throws SQLException {
		return prepStmt.executeQuery();
	}

	public PreparedStatement getPreparedStatement() {
		return prepStmt;
	}

	public void setInt(final String name, final int value) throws SQLException {
		List<Integer> positions = fields.get(name);
		if (positions == null) {
			throw new IllegalArgumentException("Parameter not found: " + name);
		}
		for (int pos : positions) {
			prepStmt.setInt(pos, value);
		}
	}

	public void setTimestamp(final String name, final Timestamp value) throws SQLException {
		List<Integer> positions = fields.get(name);
		if (positions == null) {
			throw new IllegalArgumentException("Parameter not found: " + name);
		}
		for (int pos : positions) {
			prepStmt.setTimestamp(pos, value);
		}
	}

	// Add other setters (setString, setLong, etc.) as needed
}
