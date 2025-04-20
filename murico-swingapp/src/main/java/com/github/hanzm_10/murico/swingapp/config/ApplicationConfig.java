package com.github.hanzm_10.murico.swingapp.config;

public final class ApplicationConfig extends AbstractResourceConfigProperties {
    private static ApplicationConfig instance;

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            return new ApplicationConfig();
        }

        return instance;
    }

    private ApplicationConfig() {
        super();
    }

    @Override
    public Class<? extends AbstractResourceConfig> getResourceClass() {
        return ApplicationConfig.class;
    }
}
