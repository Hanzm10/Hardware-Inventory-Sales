module murico.database {
    requires transitive java.sql;

    requires transitive murico.utils;
    requires transitive murico.io;

    requires static org.jetbrains.annotations;

    exports com.github.hanzm_10.murico.database;
    exports com.github.hanzm_10.murico.database.dao;
    exports com.github.hanzm_10.murico.database.mysql;
    exports com.github.hanzm_10.murico.database.model;
    exports com.github.hanzm_10.murico.database.model.user;
}
