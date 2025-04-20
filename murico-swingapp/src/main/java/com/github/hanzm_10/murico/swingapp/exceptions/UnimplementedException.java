package com.github.hanzm_10.murico.swingapp.exceptions;

public class UnimplementedException extends RuntimeException {
    public UnimplementedException() {
        super("This method is not implemented yet.");
    }

    public UnimplementedException(String message) {
        super(message);
    }

    public UnimplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnimplementedException(Throwable cause) {
        super(cause);
    }
}
