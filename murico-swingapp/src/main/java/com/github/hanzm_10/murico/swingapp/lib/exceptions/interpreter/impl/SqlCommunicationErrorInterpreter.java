package com.github.hanzm_10.murico.swingapp.lib.exceptions.interpreter.impl;

import com.github.hanzm_10.murico.swingapp.lib.exceptions.interpreter.ErrorInterpreter;

public class SqlCommunicationErrorInterpreter implements ErrorInterpreter {

    @Override
    public boolean canInterpret(Throwable e) {
        return e instanceof java.sql.SQLException && e.getMessage() != null
                && e.getMessage().contains("Communications link failure");
    }

    @Override
    public String interpret(Throwable e) {
        return "Cannot connect to the database.\nThere might be invalid database credentials in the configuration file.\n Please check your connection settings in (src/main/resources/com/github/hanzm_10/murico/swingapp/lib/database).";
    }

}
