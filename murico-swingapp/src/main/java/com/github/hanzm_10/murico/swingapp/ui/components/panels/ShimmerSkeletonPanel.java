package com.github.hanzm_10.murico.swingapp.ui.components.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class ShimmerSkeletonPanel extends JPanel {
	private int arc;
	private float shimmerX = -1f;
	private final Timer timer;
	private long startTime;
	private final int durationMs = 2000; // 2 seconds for a full loop
	private final Color initBgColor = new Color(245, 245, 245);

	public ShimmerSkeletonPanel(Dimension dimensions, int arc) {
		super();
		this.arc = arc;
		setPreferredSize(dimensions);
		setOpaque(false);

		timer = new Timer(10, this::animateShimmer);
	}

	public ShimmerSkeletonPanel(int arc) {
		this.arc = arc;
		setOpaque(false);

		timer = new Timer(30, this::animateShimmer);
		timer.start();
	}

	private void animateShimmer(ActionEvent ev) {
		shimmerX += 6f;

		if (shimmerX > getWidth()) {
			startTime = System.currentTimeMillis();
			shimmerX = -getWidth();
		}

		repaint();
	}

	private float easeInOutSine(float t) {
		return (float) (-(Math.cos(Math.PI * t) - 1) / 2.0);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = getWidth();
		int h = getHeight();

		// Background
		g2.setColor(initBgColor);
		g2.fillRoundRect(0, 0, w, h, arc, arc);

		// Time-based progress
		long elapsed = System.currentTimeMillis() - startTime;
		float progress = (elapsed % durationMs) / (float) durationMs;
		float eased = easeInOutSine(progress);

		// Position based on eased progress
		float shimmerWidth = w / 3f;
		float startX = -shimmerWidth + eased * (w + shimmerWidth * 2);
		float endX = startX + shimmerWidth;

		// Gradient shimmer
		GradientPaint shimmerGradient = new GradientPaint(startX, 0, new Color(220, 220, 220, 100), endX, 0,
				new Color(200, 200, 200, 0));

		g2.setPaint(shimmerGradient);
		g2.fillRoundRect(0, 0, w, h, arc, arc);

	}

	public void startAnimation() {
		startTime = System.currentTimeMillis();
		timer.start();
	}

	public void stopAnimation() {
		timer.stop();
	}
}
