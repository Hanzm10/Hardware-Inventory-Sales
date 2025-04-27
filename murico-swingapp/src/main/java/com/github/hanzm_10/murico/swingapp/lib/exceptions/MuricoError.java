package com.github.hanzm_10.murico.swingapp.lib.exceptions;

public class MuricoError extends Exception {
    private final MuricoErrorCodes errorCode;

    public MuricoError(MuricoErrorCodes errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public MuricoError(MuricoErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public MuricoErrorCodes getErrorCode() {
        return errorCode;
    }

    public String getUserMessage() {
        return getMessage();
    }
}
