/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.database.dao;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.database.model.Session;
import com.github.hanzm_10.murico.database.model.user.User;

public interface SessionDAO {
	/**
	 * Creates a new session for the given user.
	 *
	 * @param user
	 *            The user for whom to create the session.
	 * @return The session UID of the newly created session.
	 */
	String createSession(@NotNull User user);

	/**
	 * Creates a new session for the given user with the specified IP address.
	 *
	 * @param user
	 *            The user for whom to create the session.
	 * @param ipAddress
	 *            The IP address of the user.
	 * @return The session UID of the newly created session.
	 */
	String createSession(@NotNull User user, String ipAddress);

	/**
	 * Creates a new session for the given user with the specified IP address and
	 * user agent.
	 *
	 * @param user
	 *            The user for whom to create the session.
	 * @param ipAddress
	 *            The IP address of the user.
	 * @param userAgent
	 *            The user agent of the user's device.
	 * @return The session UID of the newly created session.
	 */
	String createSession(@NotNull User user, String ipAddress, String userAgent);

	boolean deleteSession();

	Session getSessionByUid(@NotNull String _sessionUid);

	void printSessionTable();

	boolean sessionExists(@NotNull String _sessionUid);

	int updateSession();
}
