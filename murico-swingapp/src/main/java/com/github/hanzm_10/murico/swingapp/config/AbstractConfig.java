package com.github.hanzm_10.murico.swingapp.config;

import java.util.logging.Logger;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public abstract class AbstractConfig {
    protected static final Logger LOGGER = MuricoLogger.getLogger(AbstractConfig.class);

    /**
     * The configuration's file extension. This is used to determine the file type.
     */
    public abstract String getFileExtension();

    /**
     * The configuration's file name. This is used to determine the file name. <br>
     * <br>
     * The returned file name is the class name of the configuration class, followed by a dot and
     * the file extension. For example, if the class name is "GlobalConfig" and the file extension
     * is "json", the returned file name will be "GlobalConfig.json".
     *
     * @return String
     */
    public String getFileName() {
        return getClass().getSimpleName();
    }

    /**
     * The configuration's file path. This is used to determine the file path.
     *
     * @return String
     */
    public abstract String getFilePath();

    public String getFullPath() {
        return getFilePath() + getFileName() + getFileExtension();
    }

    /** The header comment for the configuration file. */
    public abstract String getHeaderComment();
}
