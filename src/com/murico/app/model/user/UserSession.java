package com.murico.app.model.user;

import java.sql.Timestamp;

/**
 * Represents a user session in the application.
 * <p>
 * This class contains information about the user session, including user ID, IP address, user
 * agent, session ID, and timestamps for when the session was created and expires.
 * </p>
 *
 * @param userId The unique identifier for the user.
 * @param ipAddress The IP address of the user.
 * @param userAgent The user agent string of the user's browser or application.
 * @param sessionId The unique identifier for the session.
 * @param sessionCreatedAt The timestamp when the session was created.
 * @param sessionExpiresAt The timestamp when the session expires.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public record UserSession(int _userId, String ipAddress, String userAgent, int _sessionId,
    String _sesionUid, Timestamp _sessionCreatedAt, Timestamp _sessionExpiresAt) {

  public boolean isExpired() throws IllegalStateException {
    if (_sessionExpiresAt != null) {
      return _sessionExpiresAt.before(new Timestamp(System.currentTimeMillis()));
    }

    throw new IllegalStateException("Session expiration time is not set.");
  }

  /**
   * Builder class for creating UserSession objects.
   * <p>
   * This class provides methods to set the properties of the UserSession object before building it.
   * </p>
   * 
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
     * Sets the user ID.
     *
     * @param userId The unique identifier for the user.
     * @return this Builder instance for method chaining
     * @throws AssertionError if userId is less than or equal to 0
     */
    public Builder setUserId(int userId) {
      assert userId > 0 : "User ID must be greater than 0";

      this.userId = userId;
      return this;
    }

    public Builder setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
      return this;
    }

    public Builder setUserAgent(String userAgent) {
      this.userAgent = userAgent;
      return this;
    }

    public Builder setSessionUid(String sessionUid) {
      this.sessionUid = sessionUid;
      return this;
    }

    /**
     * Sets the session ID. This is a unique identifier for the session.
     *
     * @param sessionId The unique identifier for the session.
     * @return this Builder instance for method chaining
     * @throws AssertionError if sessionId is null or empty
     */
    public Builder setSessionId(int sessionId) {
      assert sessionId > 0 : "Session ID must be greater than 0";
      this.sessionId = sessionId;

      return this;
    }

    /**
     * Sets the session creation time. Call this only once when creating a new session to avoid data
     * inconsistency.
     *
     * @param sessionCreatedAt The timestamp when the session was created.
     * @return this Builder instance for method chaining
     * @throws AssertionError if sessionCreatedAt is null or if it is after sessionExpiresAt
     */
    public Builder setSessionCreatedAt(Timestamp sessionCreatedAt) {
      assert sessionCreatedAt != null : "Session creation time cannot be null";

      // will fail if called again and sessionExpiresAt is a timestamp from some older UserSession
      // class
      if (sessionExpiresAt != null) {
        assert sessionCreatedAt.before(
            sessionExpiresAt) : "Session creation time must be before session expiration time";
      }

      this.sessionCreatedAt = sessionCreatedAt;
      return this;
    }

    /**
     * Sets the session expiration time. Call this only once when creating a new session to avoid
     * data inconsistency.
     *
     * @param sessionExpiresAt The timestamp when the session expires.
     * @return this Builder instance for method chaining
     * @throws AssertionError if sessionExpiresAt is null or if it is before sessionCreatedAt
     */
    public Builder setSessionExpiresAt(Timestamp sessionExpiresAt) {
      assert sessionExpiresAt != null : "Session expiration time cannot be null";

      // will fail if called again and sessionCreatedAt is a timestamp from some older UserSession
      // class
      if (sessionCreatedAt != null) {
        assert sessionExpiresAt.after(
            sessionCreatedAt) : "Session expiration time must be after session creation time";
      }

      this.sessionExpiresAt = sessionExpiresAt;
      return this;
    }

    /**
     * Builds a new UserSession object.
     *
     * @return a new UserSession object
     * @throws AssertionError if any required field is null
     */
    public UserSession build() throws AssertionError {
      assert userId > 0 : "User ID must be greater than 0";
      assert sessionId > 0 : "Session ID must be greater than 0";
      assert sessionUid != null : "Session ID cannot be null";
      assert sessionCreatedAt != null : "Session created at cannot be null";
      assert sessionExpiresAt != null : "Session expires at cannot be null";

      return new UserSession(userId, ipAddress, userAgent, sessionId, sessionUid, sessionCreatedAt,
          sessionExpiresAt);
    }
  }
}
