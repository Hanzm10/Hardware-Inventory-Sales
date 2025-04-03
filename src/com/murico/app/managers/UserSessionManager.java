package com.murico.app.managers;

import com.murico.app.model.user.UserSession;

/**
 * Singleton class to manage user sessions.
 * <p>
 * This class provides methods to set, get, login, and logout user sessions.
 * </p>
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class UserSessionManager {
  private static UserSessionManager instance;

  private UserSession userSession;

  private UserSessionManager() {
    if (instance != null) {
      throw new IllegalStateException("UserSession instance already exists.");
    }
  }

  public static synchronized UserSessionManager getInstance() {
    if (instance == null) {
      instance = new UserSessionManager();
    }

    return instance;
  }

  public UserSession getUserSession() {
    return userSession;
  }

  /**
   * ONLY USE THIS DURING INITIALIZATION OF THE APPLICATION. Sets the user session. If a session
   * already exists, it will throw an exception.
   * 
   * If you want to set a new session, use the {@link #login()} method instead.
   *
   * @param userSession The user session to set.
   * @throws IllegalArgumentException if the user session is null or expired.
   * @throws IllegalStateException if a user session already exists.
   */
  public void setUserSession(UserSession userSession)
      throws IllegalArgumentException, IllegalStateException {
    if (userSession == null) {
      throw new IllegalArgumentException("User session cannot be null.");
    }

    if (this.userSession != null) {
      throw new IllegalStateException("User session already exists.");
    }

    if (userSession.isExpired()) {
      throw new IllegalArgumentException("User session is expired.");
    }

    this.userSession = userSession;
  }

  public void login() throws IllegalStateException {
    if (userSession != null || !userSession.isExpired()) {
      throw new IllegalStateException("User is already logged in.");
    }

    // TODO: Add session to database
  }

  public void logout() throws IllegalStateException {
    if (userSession == null || userSession.isExpired()) {
      throw new IllegalStateException("User is not logged in.");
    }

    // TODO: Delete session in database
  }

}
