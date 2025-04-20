/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.app.ui.image;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class RoundedImageComponent extends JComponent {
	protected int radii;
	protected int borderSize;

	public RoundedImageComponent(public Image image) {
		borderSize = 0;
		radii = 0;
	}

	public RoundedImageComponent(Image image, int borderSize, int radii) {
		this.image = image;

		this.borderSize = borderSize;
		this.radii = radii;
	}

	public RoundedImageComponent(ImageIcon image) {
		this.image = image.getImage();

		borderSize = 0;
		radii = 0;
	}

	private Rectangle getAutoSize(Image image, int size) {
		var w = size;
		var h = size;
		var iw = image.getWidth(null);
		var ih = image.getHeight(null);
		var xScale = (double) w / iw;
		var yScale = (double) h / ih;
		var scale = Math.max(xScale, yScale);
		var width = (int) (scale * iw);
		var height = (int) (scale * ih);
		if (width < 1) {
			width = 1;
		}
		if (height < 1) {
			height = 1;
		}
		var cw = size;
		var ch = size;
		var x = (cw - width) / 2;
		var y = (ch - height) / 2;
		return new Rectangle(new Point(x, y), new Dimension(width, height));
	}

	public int getBorderSize() {
		return borderSize;
	}

	public int getRadii() {
		return radii;
	}

	@Override
	public void paintComponent(Graphics grphcs) {
		super.paintComponent(grphcs);

		var width = getWidth();
		var height = getHeight();
		var diameter = Math.min(width, height);
		var x = width / 2 - diameter / 2;
		var y = height / 2 - diameter / 2;
		var border = borderSize * 2;
		diameter -= border;
		var size = getAutoSize(image, diameter);

		// 🧷 Clamp the radii to a maximum of half the diameter
		var maxRadius = diameter / 2f;
		var arcRadius = Math.min(radii, maxRadius);

		var img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		var g2_img = img.createGraphics();
		g2_img.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 🟦 Rounded rectangle mask
		var roundRect = new RoundRectangle2D.Double(0, 0, diameter, diameter, arcRadius * 2, arcRadius * 2);
		g2_img.fill(roundRect);

		var composite = g2_img.getComposite();
		g2_img.setComposite(AlphaComposite.SrcIn);
		g2_img.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2_img.drawImage(image, size.x, size.y, size.width, size.height, null);
		g2_img.setComposite(composite);
		g2_img.dispose();

		var g2 = (Graphics2D) grphcs;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (borderSize > 0) {
			diameter += border;
			g2.setColor(getForeground());
			g2.fill(new RoundRectangle2D.Double(x, y, diameter, diameter, arcRadius * 2, arcRadius * 2));
		}

		if (isOpaque()) {
			g2.setColor(getBackground());
			diameter -= border;
			g2.fill(new RoundRectangle2D.Double(x + borderSize, y + borderSize, diameter, diameter, arcRadius * 2,
					arcRadius * 2));
		}

		g2.drawImage(img, x + borderSize, y + borderSize, null);
	}

	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
		repaint();
	}

	public void setRadii(int radii) {
		this.radii = radii;
		repaint();
	}
}
