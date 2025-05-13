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
package com.github.hanzm_10.murico.swingapp.ui.buttons;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.jetbrains.annotations.NotNull;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatBorder;

public class StyledButtonFactory {
	public static JButton createButton(String text, ButtonStyles buttonStyle) {
		var button = new JButton(text);

		button.setBackground(buttonStyle.getBackgroundColor());
		button.setForeground(buttonStyle.getForegroundColor());

		if (buttonStyle == ButtonStyles.TRANSPARENT) {
			((FlatBorder) button.getBorder()).applyStyleProperty("borderColor", new Color(0x00, true));
			button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
		}

		return button;
	}

	public static JButton createButton(String text, ButtonStyles buttonStyle, int width, int height) {
		var button = createButton(text, buttonStyle);
		button.setPreferredSize(new Dimension(width, height));

		return button;
	}

	public static JButton createButtonButToggleStyle() {
		return createButtonButToggleStyle("");
	}

    public static JButton createButtonButToggleStyle(@NotNull final String text) {
		var btn = new JButton(text);

		btn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);

		return btn;
	}

	public static JToggleButton createJToggleButton() {
		var button = new JToggleButton();
		((FlatBorder) button.getBorder()).applyStyleProperty("borderColor", new Color(0x00, true));

		return button;
	}
}
