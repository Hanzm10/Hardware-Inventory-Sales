module murico.swingapp {
    requires transitive java.desktop;
    requires transitive java.logging;
    requires transitive java.sql;
    requires com.miglayout.core;
    requires static org.jetbrains.annotations;
    requires murico.lookandfeel;

    uses com.github.hanzm_10.murico.swingapp.MuricoSwingApp;
}
