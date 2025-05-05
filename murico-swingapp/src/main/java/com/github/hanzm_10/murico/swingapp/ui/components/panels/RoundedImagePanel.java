package com.github.hanzm_10.murico.swingapp.ui.components.panels;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import org.jetbrains.annotations.NotNull;

public final class RoundedImagePanel extends ImagePanel {
	protected int arc;

	public RoundedImagePanel(final Image image) {
		this(image, 10);
	}

	public RoundedImagePanel(final Image image, final int arc) {
		super(image);

		this.arc = arc;
		makeImageRounded();
	}

	public int getArc() {
		return arc;
	}

	protected void makeImageRounded() {
		if (image == null) {
			return;
		}

		var w = image.getWidth(null);
		var h = image.getHeight(null);

		var output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = output.createGraphics();

		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, 0, 0, null);
		g2.dispose();

		image = output;
	}

	public void setArc(@NotNull final int arc) {
		this.arc = arc;
		makeImageRounded();
		repaint();
	}
}
