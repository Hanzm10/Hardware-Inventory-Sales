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
package com.github.hanzm_10.murico.app.managers;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.database.model.Session;

/**
 * Singleton class to manage user sessions.
 *
 * <p>
 * This class provides methods to set, get, login, and logout user sessions.
 * TODO: update session occasionally
 */
public class SessionManager {
	private static SessionManager instance;

	public static synchronized SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
		}
		return instance;
	}

	private Session session;

	private SessionManager() {
		// Private constructor to prevent instantiation
	}

	public Session getSession() {
		return session;
	}

	public void removeSession() {
		this.session = null;
	}

	/**
	 * Returns the current session.
	 *
	 * @return The current session.
	 * @throws IllegalArgumentException
	 *             if session is null.
	 * @throws IllegalStateException
	 *             if session already exists.
	 */
	public void setSession(@NotNull Session session) throws IllegalArgumentException, IllegalStateException {
		if (session == null) {
			throw new IllegalArgumentException("Session cannot be null.");
		}

		if (session.isExpired()) {
			throw new IllegalArgumentException("Session is expired.");
		}

		if (this.session != null) {
			throw new IllegalStateException("Session already exists.");
		}

		this.session = session;
	}

	public void updateSession(Session session) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}
}
