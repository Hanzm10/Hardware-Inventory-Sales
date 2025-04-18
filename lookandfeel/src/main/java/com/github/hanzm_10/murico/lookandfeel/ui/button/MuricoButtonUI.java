package com.github.hanzm_10.murico.lookandfeel.ui.button;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicButtonUI;

public class MuricoButtonUI extends BasicButtonUI {
    public static ComponentUI createUI(final JComponent c) {
        return new MuricoButtonUI();
    }

    protected boolean pressed;

    protected boolean hovered;
    protected boolean focused;
    public MuricoButtonUI() {
        super();
    }

    @Override
    protected BasicButtonListener createButtonListener(AbstractButton b) {
        return new MuricoButtonUIListener<>(b, this);
    }

    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
    }

    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        super.paintButtonPressed(g, b);
    }

    @Override
    protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect,
            Rectangle iconRect) {
        super.paintFocus(g, b, viewRect, textRect, iconRect);
    }
}
