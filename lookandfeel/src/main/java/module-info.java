module murico.lookandfeel {
    requires transitive java.desktop;

    requires transitive murico.utils;
    requires murico.os;

    exports com.github.hanzm_10.murico.lookandfeel;
    exports com.github.hanzm_10.murico.lookandfeel.ui.button;
    exports com.github.hanzm_10.murico.lookandfeel.ui.panel;
}