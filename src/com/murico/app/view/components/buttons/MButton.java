package com.murico.app.view.components.buttons;

import com.murico.app.config.AppSettings;
import com.murico.app.controller.ui.buttons.MButtonMouseListener;

import javax.swing.*;
import java.awt.*;

public class MButton extends JButton implements MButtonInterface {

    private final int borderRadius;

    private final MButtonMouseListener mouseListener;

    private Color bg;
    private Color backgroundWhenHovered;
    private boolean isHovered;

    public MButton(String text) {
        super(text);

        this.borderRadius = 16;

        this.disableDefaultButtonStyle();
        this.setDefaults();

        this.mouseListener = new MButtonMouseListener(this);

        this.addMouseListener(this.mouseListener);
    }

    public boolean getIsHovered() {
        return this.isHovered;
    }

    private void disableDefaultButtonStyle() {
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    private void setDefaults() {
        this.setFont(AppSettings.getInstance().getButtonsFont());

        Dimension size = new Dimension(80, 40);

        this.setPreferredSize(size);
    }

    private void enableSmoothness(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public void mouseClicked() {

    }

    @Override
    public void mousePressed() {

    }

    @Override
    public void mouseReleased() {

    }

    @Override
    public void mouseEntered() {
        this.isHovered = true;
        this.setBackground(this.backgroundWhenHovered);
    }

    @Override
    public void mouseExited() {
        this.isHovered = false;
        this.setBackground(this.bg);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        this.enableSmoothness(g2);

        g2.setColor(this.getBackground());
        g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(),
                         this.borderRadius, this.borderRadius);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        this.enableSmoothness(g2);

        g2.setColor(this.getForeground());
        g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1,
                         this.borderRadius, this.borderRadius);

        g2.dispose();
    }

    // fix this so that we don't create a new Color
    // every time when mouseExited() calls this method
    @Override
    public void setBackground(Color bg) {
        if (isHovered) {
            this.bg = this.getBackground();
        } else {
            this.backgroundWhenHovered = new Color(bg.getRed(),
                                                   bg.getGreen(),
                                                   bg.getBlue(),
                                                   180);
        }

        super.setBackground(bg);
    }
}
