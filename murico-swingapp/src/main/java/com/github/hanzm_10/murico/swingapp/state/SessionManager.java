package com.github.hanzm_10.murico.swingapp.state;

import org.jetbrains.annotations.NotNull;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.Session;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;
import com.github.hanzm_10.murico.swingapp.lib.utils.SessionUtils;

public final class SessionManager {
    private static SessionManager instance;

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    private Session session;
    private User loggedInUser;

    private SessionManager() {
        // Private constructor to prevent instantiation
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Session getSession() {
        return session;
    }

    public void removeSession() {
        session = null;
        loggedInUser = null;
    }

    /**
     * Returns the current session.
     *
     * @return The current session.
     * @throws IllegalArgumentException if session is null.
     * @throws IllegalStateException if session already exists.
     */
    public void setSession(@NotNull Session session, @NotNull User user)
            throws IllegalArgumentException, IllegalStateException {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }

        if (SessionUtils.isSessionExpired(session)) {
            throw new IllegalArgumentException("Session is expired.");
        }

        if (this.session != null) {
            throw new IllegalStateException("Session already exists.");
        }

        this.session = session;
        this.loggedInUser = user;
    }

    public void updateSession(Session session) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
