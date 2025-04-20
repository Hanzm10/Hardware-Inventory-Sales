package com.github.hanzm_10.murico.swingapp.exceptions;

public class NeedsDeveloperAttentionException extends RuntimeException {
    public NeedsDeveloperAttentionException() {
        super("If you see this, it means that an exception was thrown that needs developer attention. Please report this issue.");
    }

    public NeedsDeveloperAttentionException(String message) {
        super(message);
    }

    public NeedsDeveloperAttentionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeedsDeveloperAttentionException(Throwable cause) {
        super(cause);
    }

}
