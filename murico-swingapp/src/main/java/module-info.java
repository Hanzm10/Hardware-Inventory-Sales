module murico.swingapp {
	requires transitive java.desktop;
	requires transitive java.logging;
	requires transitive java.sql;
	requires static org.jetbrains.annotations;

	requires murico.lookandfeel;

	requires com.github.weisj.jsvg;
	requires com.miglayout.swing;
	requires org.apache.pdfbox;
	requires mysql.connector.j;
	requires io.github.classgraph;
	requires org.jfree.jfreechart;
	requires org.apache.commons.text;
	requires com.github.lgooddatepicker;
	requires org.commonmark;

	opens com.github.hanzm_10.murico.swingapp;
}