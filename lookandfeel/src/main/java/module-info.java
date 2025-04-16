module murico.lookandfeel {
    requires transitive java.desktop;
    requires transitive java.logging;
    requires murico.platform;
    requires murico.utils;

    exports com.github.hanzm_10.murico.lookandfeel;
    exports com.github.hanzm_10.murico.lookandfeel.ui.panel;
    exports com.github.hanzm_10.murico.lookandfeel.ui.button;

    opens com.github.hanzm_10.murico.lookandfeel to java.desktop;
}
