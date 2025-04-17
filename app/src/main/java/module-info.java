module murico.app {
	requires transitive java.desktop;
	requires darklaf.core;
	requires murico.utils;
	requires murico.core;
	requires murico.io;
	requires murico.database;
	requires static org.jetbrains.annotations;
	requires com.miglayout.swing;

	exports com.github.hanzm_10.murico.app;
}
