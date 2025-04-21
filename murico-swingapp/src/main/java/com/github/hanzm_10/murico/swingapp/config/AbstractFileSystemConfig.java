package com.github.hanzm_10.murico.swingapp.config;

import com.github.hanzm_10.murico.swingapp.constants.Directories;

public abstract class AbstractFileSystemConfig extends AbstractConfig {
    @Override
    public String getFilePath() {
        return Directories.CONFIG_DIRECTORY;
    }

    @Override
    public String getHeaderComment() {
        return "A configuration file for Murico. Refer to the documentation for more information.";
    }
}
