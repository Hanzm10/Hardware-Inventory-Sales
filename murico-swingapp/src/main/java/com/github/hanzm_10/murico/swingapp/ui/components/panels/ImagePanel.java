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

import javax.swing.JPanel;

import com.github.hanzm_10.murico.swingapp.lib.utils.PaintUtils;

public class ImagePanel extends JPanel {
	protected Image image;

	public ImagePanel(Image image) {
		super();

		this.image = image;

		var width = image.getWidth(null);
		var height = image.getHeight(null);

		setOpaque(false);
		setPreferredSize(new Dimension(width, height));
		setSize(new Dimension(width, height));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image != null) {
			var panelWidth = getWidth();
			var panelHeight = getHeight();
			var imgWidth = image.getWidth(null);
			var imgHeight = image.getHeight(null);

			if (imgWidth > 0 && imgHeight > 0) {
				var g2 = (Graphics2D) g.create();

				PaintUtils.valueQuality(g2);

				var scaleX = panelWidth / (double) imgWidth;
				var scaleY = panelHeight / (double) imgHeight;
				var scale = Math.min(scaleX, scaleY);

				var drawWidth = (int) (imgWidth * scale);
				var drawHeight = (int) (imgHeight * scale);

				var x = (panelWidth - drawWidth) / 2;
				var y = (panelHeight - drawHeight) / 2;

				g2.drawImage(image, x, y, drawWidth, drawHeight, this);
				g2.dispose();
			}
		}
	}

	public void setImage(Image image) {
		this.image = image;

		setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		setSize(new Dimension(image.getWidth(this), image.getHeight(this)));

		repaint();
	}
}
