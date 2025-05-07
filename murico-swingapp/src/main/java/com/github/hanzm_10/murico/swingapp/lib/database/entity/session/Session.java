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
package com.github.hanzm_10.murico.swingapp.lib.database.entity.session;

import java.sql.Timestamp;

/**
 * Represents a user session in the system. Immutable data structure typically
 * loaded from or saved to a database.
 *
 * @param _sessionId
 *            Unique identifier for the session (primary key).
 * @param _userId
 *            ID of the user associated with the session.
 * @param _sessionToken
 *            Unique token used to identify or resume the session.
 * @param _createdAt
 *            Timestamp when the session was created.
 * @param expiresAt
 *            Timestamp when the session is set to expire.
 * @param updatedAt
 *            Timestamp of the last update to the session (e.g., refresh).
 * @param ipAddress
 *            IP address from which the session was created.
 * @param userAgent
 *            User agent string from the client.
 * @param status
 *            Current status of the session (e.g., ACTIVE, EXPIRED, REVOKED).
 * @param statusUpdatedAt
 *            Timestamp of the last change in session status.
 */
public record Session(int _sessionId, int _userId, String _sessionToken, Timestamp _createdAt, Timestamp expiresAt,
		Timestamp updatedAt, String ipAddress, String userAgent, SessionStatus status, Timestamp statusUpdatedAt) {

	public Session newStatus(SessionStatus status) {
		return new Session(_sessionId, _userId, _sessionToken, _createdAt, expiresAt, updatedAt, ipAddress, userAgent,
				status, new Timestamp(System.currentTimeMillis()));
	}
}
