module murico.core {
    requires transitive java.desktop;
    requires transitive java.sql;

    requires static org.jetbrains.annotations;

    exports com.github.hanzm_10.murico.core;
    exports com.github.hanzm_10.murico.core.model;
}
