module murico.swingapp {
	requires transitive java.desktop;
	requires transitive java.logging;
	requires transitive java.sql;
	requires static org.jetbrains.annotations;
	requires murico.lookandfeel;

	exports com.github.hanzm_10.murico.swingapp;
	exports com.github.hanzm_10.murico.swingapp.lib.cache;
}
