module murico.core {
    requires transitive java.desktop;
    requires transitive java.sql;

    requires transitive murico.utils;
    requires transitive murico.database;
    requires murico.os;
    requires murico.io;

    requires static org.jetbrains.annotations;

    exports com.github.hanzm_10.murico.core;
    exports com.github.hanzm_10.murico.core.constants;
    exports com.github.hanzm_10.murico.core.config;
    exports com.github.hanzm_10.murico.core.exceptions;
}
