module murico.core {
    requires transitive java.desktop;
    requires transitive java.sql;
    requires static org.jetbrains.annotations;
    requires murico.utils;
    requires murico.io;

    exports com.github.hanzm_10.murico.core;
    exports com.github.hanzm_10.murico.core.model;
}
