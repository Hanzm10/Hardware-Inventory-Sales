package com.github.hanzm_10.murico.swingapp.constants;

public final class PropertyKey {
    public static final class Color {
        public static final String FOREGROUND = "color.foreground";
        public static final String FOREGROUND_DARK = "color.dark.foreground";
        public static final String BACKGROUND = "color.background";
        public static final String BACKGROUND_DARK = "color.dark.background";
        public static final String PRIMARY = "color.primary.default";
        public static final String PRIMARY_DARK = "color.dark.primary.default";
        public static final String PRIMARY_FOREGROUND = "color.primary.foreground";
        public static final String PRIMARY_FOREGROUND_DARK = "color.dark.primary.foreground";
        public static final String SECONDARY = "color.secondary.default";
        public static final String SECONDARY_DARK = "color.dark.secondary.default";
        public static final String SECONDARY_FOREGROUND = "color.secondary.foreground";
        public static final String SECONDARY_FOREGROUND_DARK = "color.dark.secondary.foreground";
        public static final String BORDER = "color.border.default";
        public static final String PLACEHOLDER = "color.placeholder.default";
    }

    public static final class Database {
        public static final String DB_URL = "db.url";
        public static final String DB_USER = "db.user";
        public static final String DB_PASSWORD = "db.password";
        public static final String DB_NAME = "db.name";
    }

    public static final class Metadata {
        public static final String APP_TITLE = "app.title";
        public static final String APP_VERSION = "app.version";
    }

    public static final class Session {
        public static final String UID = "session.uid";
    }

    private PropertyKey() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }
}
