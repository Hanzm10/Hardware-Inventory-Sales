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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

public class ImagePanel extends JPanel {
	private Image image;
    private Image roundedImage;
    private int arc;

	public ImagePanel(Image image) {
		super();
		this.image = image;

		setOpaque(false);
        var width = image.getWidth(null);
        var height = image.getHeight(null);
		setPreferredSize(new Dimension(width, height));
		setSize(new Dimension(width, height));
	}

    public int getArc() {
        return arc;
    }

    public void setArc(@NotNull final int arc) {
        this.arc = arc;
        makeImageRounded();
    }

    protected void makeImageRounded() {
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

        roundedImage = output;
    }

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image != null) {
            var image = roundedImage == null ? this.image : roundedImage;

			var panelWidth = getWidth();
			var panelHeight = getHeight();

			var imgWidth = image.getWidth(null);
			var imgHeight = image.getHeight(null);

			if (imgWidth > 0 && imgHeight > 0) {
				var scaleX = panelWidth / (double) imgWidth;
				var scaleY = panelHeight / (double) imgHeight;
				var scale = Math.min(scaleX, scaleY);

				var drawWidth = (int) (imgWidth * scale);
				var drawHeight = (int) (imgHeight * scale);

				var x = (panelWidth - drawWidth) / 2;
				var y = (panelHeight - drawHeight) / 2;

				g.drawImage(image, x, y, drawWidth, drawHeight, this);
			}
		}
	}

	public void setImage(Image image) {
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		revalidate();
		repaint();
	}
}
