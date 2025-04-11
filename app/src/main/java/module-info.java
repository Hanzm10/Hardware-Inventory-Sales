module murico.app {
	requires transitive java.desktop;
	requires transitive murico.lookandfeel;
	requires murico.utils;
    requires murico.core;
    requires murico.io;

	exports com.github.hanzm_10.murico.app;
}
