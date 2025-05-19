package com.github.hanzm_10.murico.swingapp.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JComponent;

public class DashedLineSeparator extends JComponent {
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, 10); // Height of the separator
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.LIGHT_GRAY);
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 5, 5 }, 0);
		g2d.setStroke(dashed);
		g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
		g2d.dispose();
	}
}
