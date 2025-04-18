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
package com.github.hanzm_10.murico.database.model;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a user session in the application.
 *
 * <p>
 * This class contains information about the user session, including user ID, IP
 * address, user agent, session ID, and timestamps for when the session was
 * created and expires.
 *
 * @param userId
 *            The unique identifier for the user.
 * @param ipAddress
 *            The IP address of the user.
 * @param userAgent
 *            The user agent string of the user's browser or application.
 * @param sessionId
 *            The unique identifier for the session.
 * @param sessionCreatedAt
 *            The timestamp when the session was created.
 * @param sessionExpiresAt
 *            The timestamp when the session expires.
 */
public record Session(int _userId, String ipAddress, String userAgent, int _sessionId, String _sesionUid,
		Timestamp _sessionCreatedAt, Timestamp _sessionExpiresAt) {

	public boolean isExpired() throws IllegalStateException {
		if (_sessionExpiresAt != null) {
			return _sessionExpiresAt.before(new Timestamp(System.currentTimeMillis()));
		}

		throw new IllegalStateException("Session expiration time is not set.");
	}

	/**
	 * Builder class for creating UserSession objects.
	 *
	 * <p>
	 * This class provides methods to set the properties of the UserSession object
	 * before building it.
	 */
	public static class Builder {
		private int userId;
		private String ipAddress;
		private String userAgent;
		private String sessionUid;
		private int sessionId;
		private Timestamp sessionCreatedAt;
		private Timestamp sessionExpiresAt;

		/**
		 * Builds a new UserSession object.
		 *
		 * @return a new UserSession object
		 */
		public @NotNull Session build() throws IllegalStateException {
			if (userId <= 0) {
				throw new IllegalStateException("User ID must be greater than 0");
			}

			if (sessionId <= 0) {
				throw new IllegalStateException("Session ID must be greater than 0");
			}

			if (sessionUid == null || sessionUid.isBlank()) {
				throw new IllegalStateException("Session UID cannot be null or empty");
			}

			if (sessionCreatedAt == null) {
				throw new IllegalStateException("Session creation time cannot be null");
			}

			if (sessionExpiresAt == null) {
				throw new IllegalStateException("Session expiration time cannot be null");
			}

			return new Session(userId, ipAddress, userAgent, sessionId, sessionUid, sessionCreatedAt, sessionExpiresAt);
		}

		public Builder setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
			return this;
		}

		/**
		 * Sets the session creation time. Call this only once when creating a new
		 * session to avoid data inconsistency.
		 *
		 * @param sessionCreatedAt
		 *            The timestamp when the session was created.
		 * @return this Builder instance for method chaining
		 * @throws IllegalArgumentException
		 *             if sessionCreatedAt is null or if it is after sessionExpiresAt
		 */
		public Builder setSessionCreatedAt(Timestamp sessionCreatedAt) throws IllegalArgumentException {
			if (sessionCreatedAt == null) {
				throw new IllegalArgumentException("Session creation time cannot be null");
			}

			// will fail if called again and sessionExpiresAt is a timestamp from some older
			// UserSession
			// class

			if (sessionExpiresAt != null) {
				if (sessionCreatedAt.after(sessionExpiresAt)) {
					throw new IllegalArgumentException("Session creation time must be before session expiration time");
				}
			}

			this.sessionCreatedAt = sessionCreatedAt;
			return this;
		}

		/**
		 * Sets the session expiration time. Call this only once when creating a new
		 * session to avoid data inconsistency.
		 *
		 * @param sessionExpiresAt
		 *            The timestamp when the session expires.
		 * @return this Builder instance for method chaining
		 * @throws IllegalArgumentException
		 *             if sessionExpiresAt is null or if it is before sessionCreatedAt
		 */
		public Builder setSessionExpiresAt(Timestamp sessionExpiresAt) throws IllegalArgumentException {
			if (sessionExpiresAt == null) {
				throw new IllegalArgumentException("Session expiration time cannot be null");
			}

			// will fail if called again and sessionCreatedAt is a timestamp from some older
			// UserSession
			// class
			if (sessionCreatedAt != null) {
				if (sessionExpiresAt.before(sessionCreatedAt)) {
					throw new IllegalArgumentException("Session expiration time must be after session creation time");
				}
			}

			this.sessionExpiresAt = sessionExpiresAt;
			return this;
		}

		/**
		 * Sets the session ID. This is a unique identifier for the session.
		 *
		 * @param sessionId
		 *            The unique identifier for the session.
		 * @return this Builder instance for method chaining
		 * @throws IllegalArgumentException
		 *             if sessionId is less than or equal to 0
		 */
		public Builder setSessionId(int sessionId) throws IllegalArgumentException {
			if (sessionId <= 0) {
				throw new IllegalArgumentException("Session ID must be greater than 0");
			}

			this.sessionId = sessionId;

			return this;
		}

		/**
		 * Sets the session UID. This is a unique identifier for the session.
		 *
		 * @param sessionUid
		 *            The unique identifier for the session.
		 * @return this Builder instance for method chaining
		 * @throws IllegalArgumentException
		 *             if sessionUid is null or empty
		 */
		public Builder setSessionUid(String sessionUid) throws IllegalArgumentException {
			if (sessionUid == null || sessionUid.isBlank()) {
				throw new IllegalArgumentException("Session ID cannot be null or empty");
			}

			this.sessionUid = sessionUid;
			return this;
		}

		public Builder setUserAgent(String userAgent) {
			this.userAgent = userAgent;
			return this;
		}

		/**
		 * Sets the user ID.
		 *
		 * @param userId
		 *            The unique identifier for the user.
		 * @return this Builder instance for method chaining
		 * @throws IllegalArgumentException
		 *             if userId is less than or equal to 0
		 */
		public Builder setUserId(int userId) throws IllegalArgumentException {
			if (userId <= 0) {
				throw new IllegalArgumentException("User ID must be greater than 0");
			}

			this.userId = userId;
			return this;
		}
	}
}
