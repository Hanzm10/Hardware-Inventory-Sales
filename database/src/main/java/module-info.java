module murico.database {
    requires transitive java.sql;
    requires transitive java.logging;

    requires static org.jetbrains.annotations;

    exports com.github.hanzm_10.murico.database;
}
