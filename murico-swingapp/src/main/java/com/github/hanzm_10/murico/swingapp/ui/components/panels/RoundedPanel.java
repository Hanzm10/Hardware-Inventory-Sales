package com.github.hanzm_10.murico.swingapp.ui.components.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.utils.PaintUtils;

public class RoundedPanel extends JPanel {

	private int cornerRadius;

	public RoundedPanel(int radius) {
		super();

		this.cornerRadius = radius;

		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// DO NOT call super.paintComponent(g) first if setOpaque(false),
		// as we want to draw directly onto the parent's background or whatever is
		// behind.

		Graphics2D g2d = (Graphics2D) g.create();
		PaintUtils.valueQuality(g2d);

		Insets insets = getInsets(); // Get the border insets
		int x = insets.left;
		int y = insets.top;
		int width = getWidth() - insets.left - insets.right;
		int height = getHeight() - insets.top - insets.bottom;

		RoundRectangle2D roundedRect = new RoundRectangle2D.Float(x, y, width - 1, height - 1, cornerRadius,
				cornerRadius); // Use width-1, height-1 to avoid potential minor border clipping

		g2d.setColor(getBackground());
		g2d.fill(roundedRect);

		g2d.dispose();
		super.paintComponent(g);
	}
}
