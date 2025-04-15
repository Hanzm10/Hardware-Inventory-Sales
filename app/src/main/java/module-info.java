module murico.app {
    requires transitive java.desktop;
    requires transitive murico.lookandfeel;
    requires murico.utils;
    requires murico.core;
    requires murico.io;
    requires murico.database;
    requires static org.jetbrains.annotations;
    requires com.miglayout.swing;
    requires darklaf.core;

    exports com.github.hanzm_10.murico.app;
}
