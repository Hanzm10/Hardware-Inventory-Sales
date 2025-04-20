package com.github.hanzm_10.murico.swingapp.lib.utils;

import java.sql.Timestamp;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.Session;

public final class SessionUtils {
    public static final boolean isSessionExpired(final Session session) {
        if (session._sessionExpiresAt() != null) {
            return session._sessionExpiresAt().before(new Timestamp(System.currentTimeMillis()));
        }

        throw new IllegalStateException("Session expiration time is not set.");
    }
}
