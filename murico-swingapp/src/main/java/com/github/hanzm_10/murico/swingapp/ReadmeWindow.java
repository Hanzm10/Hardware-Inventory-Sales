package com.github.hanzm_10.murico.swingapp;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class ReadmeWindow extends JFrame {
	public ReadmeWindow() {
		setTitle("Murico - README");

		var markdown = loadMarkdown();

		var parser = Parser.builder().build();
		var renderer = HtmlRenderer.builder().build();

		var document = parser.parse(markdown);
		var html = renderer.render(document);

		var htmlContent = "<html><body style='font-family:sans-serif; padding:10px;'>" + html + "</body></html>";

		var editor = new JEditorPane("text/html", htmlContent);
		editor.setEditable(false);
		editor.setCaretPosition(0);

		var scrollPane = new JScrollPane(editor);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

		add(scrollPane);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
	}

	private String loadMarkdown() {
		StringBuilder sb = new StringBuilder();
		try (InputStream readmeStream = ReadmeWindow.class.getResourceAsStream("/README.md")) {
			if (readmeStream == null) {
				return "README not found.";
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(readmeStream))) {
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			}
		} catch (Exception e) {
			return "Failed to load README: " + e.getMessage();
		}
		return sb.toString();
	}
}
