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
package com.github.hanzm_10.murico.lookandfeel.ui.button;

import java.awt.Cursor;
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

        var c = e.getComponent();

        buttonUI.focused = true;
        c.repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        super.focusLost(e);

        var c = e.getComponent();

        buttonUI.focused = false;
        c.repaint();
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        super.mouseEntered(e);

        var c = e.getComponent();

        if (!buttonUI.pressed) {
            c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        buttonUI.hovered = true;
        c.repaint();
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        super.mouseExited(e);

        var c = e.getComponent();

        if (!buttonUI.pressed) {
            c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        buttonUI.hovered = false;
        c.repaint();
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        super.mousePressed(e);

        var c = e.getComponent();

        buttonUI.pressed = true;
        c.repaint();
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        super.mouseReleased(e);

        var c = e.getComponent();

        if (!buttonUI.hovered) {
            c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        buttonUI.pressed = false;
        c.repaint();
    }
}
