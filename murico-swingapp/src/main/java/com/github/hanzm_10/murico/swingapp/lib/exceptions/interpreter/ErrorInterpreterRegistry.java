package com.github.hanzm_10.murico.swingapp.lib.exceptions.interpreter;

import com.github.hanzm_10.murico.swingapp.lib.exceptions.interpreter.impl.SqlCommunicationErrorInterpreter;

public class ErrorInterpreterRegistry {
    private final ErrorInterpreter[] interpreters;

    public ErrorInterpreterRegistry() {
        interpreters = new ErrorInterpreter[] { new SqlCommunicationErrorInterpreter(), };
    }

    public String interpret(Throwable e) {
        for (ErrorInterpreter interpreter : interpreters) {
            if (interpreter.canInterpret(e)) {
                return interpreter.interpret(e);
            }
        }

        return e.getMessage();
    }
}
