package com.github.hanzm_10.murico.swingapp.lib.exceptions;

public enum MuricoErrorCodes {
    INVALID_CREDENTIALS(1001, "Invalid credentials. Please check your username and password."),
    FORBIDDEN_ACCESS(1002, "You do not have permission to access this resource."),
    DATABASE_CONNECTION_FAILED(2001, "Unable to connect to the database. Please check your configuration."),
    UNKNOWN_ERROR(9999, "An unknown error has occurred.");

    private final int code;
    private final String defaultMessage;

    MuricoErrorCodes(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return code + " - " + defaultMessage;
    }
}
