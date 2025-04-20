module murico.app {
	requires transitive java.desktop;
	requires com.formdev.flatlaf;
	requires murico.utils;
	requires murico.io;
	requires murico.database;
	requires murico.constants;
	requires murico.config;
	requires murico.exceptions;
	requires murico.image;
	requires murico.gui;
	requires static org.jetbrains.annotations;
	requires com.miglayout.swing;

	exports com.github.hanzm_10.murico.app;
}
