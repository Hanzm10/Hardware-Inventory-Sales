package com.github.hanzm_10.murico.swingapp.lib.exceptions.interpreter;

/**
 * Interface for interpreting errors.
 * 
 * <p>
 * Useful for providing user-friendly error messages or logging.
 * </p>
 */
public interface ErrorInterpreter {
    public boolean canInterpret(Throwable e);

    public String interpret(Throwable e);
}
