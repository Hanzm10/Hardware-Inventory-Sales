package com.github.hanzm_10.murico.app.ui;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class ResizableImagePanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 8892852953454734499L;
    private Image image;

    public ResizableImagePanel(Image image) {
        this.image = image;
        setOpaque(false); // Optional: let background show through
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            var panelWidth = getWidth();
            var panelHeight = getHeight();

            var imgWidth = image.getWidth(this);
            var imgHeight = image.getHeight(this);

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
        repaint();
    }
}
