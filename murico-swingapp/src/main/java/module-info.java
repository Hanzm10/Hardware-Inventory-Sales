module murico.swingapp {
	requires transitive java.desktop;
	requires transitive java.logging;
	requires transitive java.sql;
	requires com.github.weisj.jsvg;
	requires com.miglayout.swing;
	requires static org.jetbrains.annotations;
	requires murico.lookandfeel;
	requires jakarta.mail;
	requires org.apache.pdfbox;
	requires mysql.connector.j;
	requires io.github.classgraph;
	requires org.jfree.jfreechart;
	requires org.apache.commons.text;
	requires com.github.lgooddatepicker;

	exports com.github.hanzm_10.murico.swingapp;
	exports com.github.hanzm_10.murico.swingapp.lib.navigation;
	exports com.github.hanzm_10.murico.swingapp.lib.navigation.manager;
	exports com.github.hanzm_10.murico.swingapp.lib.navigation.scene;
	exports com.github.hanzm_10.murico.swingapp.lib.navigation.factory;
	exports com.github.hanzm_10.murico.swingapp.lib.navigation.guard;
}
