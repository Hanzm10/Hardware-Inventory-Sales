package com.github.hanzm_10.murico.swingapp.lib.database.entity.session;

import java.util.Locale;

public enum SessionStatus {
    ACTIVE, INACTIVE, REVOKED;

    /**
     * Returns the SessionStatus enum constant that matches the given string.
     *
     * @param status The string representation of the session status.
     * @return The corresponding SessionStatus enum constant.
     * @throws IllegalArgumentException if the string does not match any enum constant.
     */
    public static SessionStatus fromString(String status) {
        for (SessionStatus sessionStatus : SessionStatus.values()) {
            if (sessionStatus.name().equalsIgnoreCase(status)) {
                return sessionStatus;
            }
        }

        throw new IllegalArgumentException("No constant with text " + status + " found");
    }


    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
