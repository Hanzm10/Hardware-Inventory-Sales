open module murico.swingapp {
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
}
