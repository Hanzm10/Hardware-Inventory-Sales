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
package com.github.hanzm_10.murico.app.ui.panel;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class RoundedImagePanel extends JComponent {

	protected Icon icon;
	protected int borderSize;
	protected int radii;

	public RoundedImagePanel(Icon icon) {
		this.icon = icon;
		setOpaque(false);
		setBorderSize(0);
	}

	private Rectangle getAutoSize(Icon image, int size) {
		var w = size;
		var h = size;
		var iw = image.getIconWidth();
		var ih = image.getIconHeight();
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

	public Icon getIcon() {
		return icon;
	}

	@Override
	protected void paintComponent(Graphics grphcs) {
		if (icon != null) {
			var width = getWidth();
			var height = getHeight();
			var diameter = Math.min(width, height);
			var x = width / 2 - diameter / 2;
			var y = height / 2 - diameter / 2;
			var border = borderSize * 2;
			diameter -= border;
			var size = getAutoSize(icon, diameter);
			var img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
			var g2_img = img.createGraphics();
			g2_img.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2_img.fillOval(0, 0, diameter, diameter);
			var composite = g2_img.getComposite();
			g2_img.setComposite(AlphaComposite.SrcIn);
			g2_img.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2_img.drawImage(toImage(icon), size.x, size.y, size.width, size.height, null);
			g2_img.setComposite(composite);
			g2_img.dispose();
			var g2 = (Graphics2D) grphcs;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (borderSize > 0) {
				diameter += border;
				g2.setColor(getForeground());
				g2.fillOval(x, y, diameter, diameter);
			}
			if (isOpaque()) {
				g2.setColor(getBackground());
				diameter -= border;
				g2.fillOval(x + borderSize, y + borderSize, diameter, diameter);
			}
			g2.drawImage(img, x + borderSize, y + borderSize, null);
		}
		super.paintComponent(grphcs);
	}

	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	private Image toImage(Icon icon) {
		return ((ImageIcon) icon).getImage();
	}
}
