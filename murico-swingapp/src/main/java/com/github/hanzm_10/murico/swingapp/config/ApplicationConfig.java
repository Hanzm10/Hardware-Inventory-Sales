package com.github.hanzm_10.murico.swingapp.config;

public final class ApplicationConfig {
    public class FileSystemApplicationConfig extends AbstractFileSystemConfigProperties {
        private FileSystemApplicationConfig() {
            super();
        }

        @Override
        public String getFileName() {
            return ApplicationConfig.class.getSimpleName();
        }
    }

    public class ReadOnlyApplicationConfig extends AbstractResourceConfigProperties {
        private ReadOnlyApplicationConfig() {
            super();
        }

        @Override
        public String getFileName() {
            return ApplicationConfig.class.getSimpleName();
        }

        @Override
        public Class<? extends AbstractResourceConfig> getResourceClass() {
            return ReadOnlyApplicationConfig.class;
        }

    }

    private static ApplicationConfig instance;

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            return new ApplicationConfig();
        }

        return instance;
    }

    private final FileSystemApplicationConfig config;

    private final ReadOnlyApplicationConfig defaultConfig;

    private ApplicationConfig() {
        super();

        config = new FileSystemApplicationConfig();
        defaultConfig = new ReadOnlyApplicationConfig();
    }

    public FileSystemApplicationConfig getConfig() {
        return config;
    }

    public ReadOnlyApplicationConfig getDefaultConfig() {
        return defaultConfig;
    }

    public String getProperty(String key) {
        var value = config.getProperty(key);

        if (value == null) {
            value = defaultConfig.getProperty(key);
        }

        return value;
    }
}
