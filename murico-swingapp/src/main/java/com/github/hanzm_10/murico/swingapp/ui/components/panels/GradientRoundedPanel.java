/**
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.ui.components.panels;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.utils.PaintUtils;

public class GradientRoundedPanel extends JPanel {

	private Color startColor;
	private Color endColor;
	private int cornerRadius;

	public GradientRoundedPanel(Color start, Color end, int radius) {
		super();

		this.startColor = start;
		this.endColor = end;
		this.cornerRadius = radius;
		// --- KEY CHANGE: Make the panel non-opaque ---
		// This tells Swing not to paint the default background itself,
		// relying solely on our paintComponent override for the background.
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// DO NOT call super.paintComponent(g) first if setOpaque(false),
		// as we want to draw directly onto the parent's background or whatever is
		// behind.

		Graphics2D g2d = (Graphics2D) g.create();
		PaintUtils.valueQuality(g2d);

		int width = getWidth();
		int height = getHeight();

		// Gradient and Rounded Rectangle setup (same as before)
		GradientPaint gp = new GradientPaint(0, 0, startColor, 0, height, endColor);
		RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius,
				cornerRadius); // Use width-1, height-1 to avoid potential minor border clipping

		// --- Custom Painting ---
		g2d.setPaint(gp);
		g2d.fill(roundedRect); // Fill with gradient

		g2d.dispose();

		// --- IMPORTANT: Call super.paintComponent AFTER custom painting ---
		// This ensures that child components (buttons, labels) are painted
		// ON TOP of the custom background we just drew.
		super.paintComponent(g);
	}

	// Optional: If layout issues occur, uncommenting this might help,
	// but usually GridBagLayout handles it if components are added.
	// @Override
	// public boolean isOpaque() {
	// // Explicitly confirm non-opaque status
	// return false;
	// }
}