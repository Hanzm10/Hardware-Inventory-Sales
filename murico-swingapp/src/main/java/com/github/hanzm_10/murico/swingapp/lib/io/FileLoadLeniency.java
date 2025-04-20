package com.github.hanzm_10.murico.swingapp.lib.io;

public enum FileLoadLeniency {
    /**
     * If the file is not found, an exception will be thrown.
     */
    MANDATORY,
    /**
     * If the file is not found, it will be logged.
     */
    LOG_MISSING,
    /**
     * If the file is not found, it will be created.
     */
    CREATE_FILE_IF_MISSING,

    /**
     * If the file is not found, it will be ignored.
     */
    ALLOW_MISSING;

}
