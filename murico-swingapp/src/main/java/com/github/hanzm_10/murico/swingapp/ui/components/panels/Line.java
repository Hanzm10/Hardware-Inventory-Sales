package com.github.hanzm_10.murico.swingapp.ui.components.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;

import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;

public class Line extends JComponent {
	public static class LineBuilder {
		private Color[] colors;
		private Orientation orientation;
		private float strokeSize;
		private float[] fractions;

		public LineBuilder() {
			colors = new Color[] { Color.BLACK };
			orientation = Orientation.HORIZONTAL;
			strokeSize = 1;
			fractions = new float[] { 1f };
		}

		public Line build() {
			return new Line(colors, orientation, strokeSize, fractions);
		}

		public LineBuilder setColors(@NotNull final Color[] colors) {
			this.colors = colors;

			return this;
		}

		public LineBuilder setFractions(@NotNull final float[] fractions) {
			this.fractions = fractions;

			return this;
		}

		public LineBuilder setOrientation(@NotNull final Orientation orientation) {
			this.orientation = orientation;

			return this;
		}

		public LineBuilder setStrokeSize(@NotNull final float strokeSize) {
			this.strokeSize = strokeSize;

			return this;
		}
	}

	public enum Orientation {
		VERTICAL, HORIZONTAL;
	}

	public static LineBuilder builder() {
		return new LineBuilder();
	}

	private Color[] colors;
	private Orientation orientation;
	private float strokeSize;
	private float[] fractions;

	private Line(@NotNull final Color[] colors, @NotNull final Orientation orientation, @NotNull final float strokeSize,
			@NotNull final float[] fractions) {
		this.colors = colors;
		this.orientation = orientation;
		this.strokeSize = strokeSize;
		this.fractions = fractions;
	}

	@Override
	protected void paintComponent(Graphics g) {
		var g2d = (Graphics2D) g.create();
		var dimensions = getSize();
		var gp = new LinearGradientPaint(0f, 0f, dimensions.width, dimensions.height, fractions, colors);

		g2d.setPaint(gp);
		g2d.setStroke(new BasicStroke(strokeSize));

		switch (orientation) {
		case VERTICAL:
			var halfWidth = dimensions.width / 2;
			g2d.drawLine(halfWidth, 0, halfWidth, dimensions.height);
			break;
		case HORIZONTAL:
			var halfHeight = dimensions.height / 2;
			g2d.drawLine(0, halfHeight, dimensions.width, halfHeight);
			break;
		}

		g2d.dispose();
	}
}
