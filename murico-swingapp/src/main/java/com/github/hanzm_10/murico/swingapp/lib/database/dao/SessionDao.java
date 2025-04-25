/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.Session;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;

public interface SessionDao {
	/**
	 * Creates a session for the given user.
	 *
	 * @param user
	 *            The user for whom the session is created.
	 * @return The session UID.
	 * @throws SQLException
	 *             if a database access error occurs.
	 */
	public @NotNull String createSession(@NotNull final User user) throws SQLException;

	/** Calls {@link SessionDao#createSession(User, String, String, String)} */
	public @NotNull String createSession(@NotNull final User user, final String ipAddress) throws SQLException;

	/**
	 * Creates a session for the given user with the specified IP address and user
	 * agent.
	 *
	 * @param user
	 *            The user for whom the session is created.
	 * @param ipAddress
	 *            The IP address of the user.
	 * @param userAgent
	 *            The user agent of the user's device.
	 * @return The session UID.
	 * @throws SQLException
	 *             if a database access error occurs.
	 */
	public @NotNull String createSession(@NotNull final User user, final String ipAddress, final String userAgent)
			throws SQLException;

	/**
	 * Gets the session for the given session UID.
	 *
	 * @param _sessionUid
	 * @return The session object if it exists, null otherwise.
	 * @throws SQLException
	 *             if a database access error occurs.
	 */
	public Session getSessionByUid(@NotNull final String _sessionUid) throws SQLException;

	/**
	 * Prints the session of the given user in a table format.
	 *
	 * @param user
	 *            The user whose session is to be printed.
	 * @throws SQLException
	 *             if a database access error occurs.
	 */
	public void printSessionTableOfUser(@NotNull final User user) throws SQLException;

	/**
	 * Checks if a session exists for the given session UID.
	 *
	 * @param sessionUid
	 * @return true if the session exists, false otherwise.
	 * @throws SQLException
	 *             if a database access error occurs.
	 */
	public boolean sessionExists(@NotNull final String _sessionUid) throws SQLException;
}
