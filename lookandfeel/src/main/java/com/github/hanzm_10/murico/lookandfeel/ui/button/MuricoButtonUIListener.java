package com.github.hanzm_10.murico.lookandfeel.ui.button;

import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.plaf.basic.BasicButtonListener;

public class MuricoButtonUIListener<T extends MuricoButtonUI> extends BasicButtonListener {
    private final T buttonUI;

    public MuricoButtonUIListener(final AbstractButton b, final T buttonUI) {
        super(b);
        this.buttonUI = buttonUI;
    }

    @Override
    public void focusGained(FocusEvent e) {
        super.focusGained(e);
        buttonUI.focused = true;
    }

    @Override
    public void focusLost(FocusEvent e) {
        super.focusLost(e);
        buttonUI.focused = false;
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        super.mouseEntered(e);
        buttonUI.hovered = true;
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        super.mouseExited(e);
        buttonUI.hovered = false;
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        super.mousePressed(e);
        buttonUI.pressed = true;
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        super.mouseReleased(e);
        buttonUI.pressed = false;
    }
}