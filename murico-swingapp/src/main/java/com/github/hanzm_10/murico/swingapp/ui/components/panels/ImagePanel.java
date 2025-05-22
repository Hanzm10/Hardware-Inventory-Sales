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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.utils.PaintUtils;

public class ImagePanel extends JPanel {
	protected Image image;
	protected boolean scaleImage = true;

	public ImagePanel(Image image) {
		super();

		this.image = image;

		if (image != null) {
			setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
			setSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		}

		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image != null) {
			var g2 = (Graphics2D) g.create();

			PaintUtils.valueQuality(g2);

			if (!scaleImage) {
				g2.drawImage(image, 0, 0, this); // Draw at original size
			} else {

				Insets insets = getInsets(); // Get the border insets
				var panelX = insets.left;
				var panelY = insets.top;
				var panelWidth = getWidth() - insets.left - insets.right;
				var panelHeight = getHeight() - insets.top - insets.bottom;
				var imgWidth = image.getWidth(null);
				var imgHeight = image.getHeight(null);

				if (imgWidth > 0 && imgHeight > 0) {

					var scaleX = panelWidth / (double) imgWidth;
					var scaleY = panelHeight / (double) imgHeight;
					var scale = Math.min(scaleX, scaleY);

					var drawWidth = (int) (imgWidth * scale);
					var drawHeight = (int) (imgHeight * scale);

					var x = panelX + (panelWidth - drawWidth) / 2;
					var y = panelY + (panelHeight - drawHeight) / 2;

					g2.drawImage(image, x, y, drawWidth, drawHeight, this);
				}
			}
			g2.dispose();
		}
	}

	public void setImage(Image image) {
		this.image = image;

		setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		setSize(new Dimension(image.getWidth(this), image.getHeight(this)));

		repaint();
	}
}
