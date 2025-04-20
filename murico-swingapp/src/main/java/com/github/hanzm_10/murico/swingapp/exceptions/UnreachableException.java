package com.github.hanzm_10.murico.swingapp.exceptions;

public class UnreachableException extends RuntimeException {
    public UnreachableException() {
        super("This code should not be reachable.");
    }

    public UnreachableException(String message) {
        super(message);
    }

    public UnreachableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnreachableException(Throwable cause) {
        super(cause);
    }
}
