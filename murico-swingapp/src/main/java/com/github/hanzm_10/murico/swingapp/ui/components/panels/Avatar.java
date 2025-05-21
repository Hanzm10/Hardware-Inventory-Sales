package com.github.hanzm_10.murico.swingapp.ui.components.panels;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import com.github.hanzm_10.murico.swingapp.lib.utils.PaintUtils;

public class Avatar extends ImagePanel {
	public Avatar(final Image image) {
		super(image);
		makeImageCircular();
	}

	protected void makeImageCircular() {
		if (image == null) {
			return;
		}

		int w = image.getWidth(null);
		int h = image.getHeight(null);

		// Make it square by using the smaller of width and height
		int size = Math.min(w, h);

		var output = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = output.createGraphics();

		g2.setComposite(AlphaComposite.Src);
		PaintUtils.valueQuality(g2);
		g2.setColor(Color.WHITE);
		g2.fill(new Ellipse2D.Float(0, 0, size, size));

		g2.setComposite(AlphaComposite.SrcAtop);

		// Center the image if it's not square
		int x = (w > h) ? -(w - h) / 2 : 0;
		int y = (h > w) ? -(h - w) / 2 : 0;

		g2.drawImage(image, x, y, null);
		g2.dispose();

		image = output;
	}

	@Override
	public void setImage(final Image image) {
		super.setImage(image);
		makeImageCircular();
	}
}
