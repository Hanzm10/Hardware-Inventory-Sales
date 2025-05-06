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
package com.github.hanzm_10.murico.swingapp.ui.inputs;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.utils.ColorUtils;

public class TextPlaceholder extends JLabel implements FocusListener, DocumentListener {
	public enum Show {
		ALWAYS, FOCUS_GAINED, FOCUS_LOST
	}

	private JTextComponent textComponent;
	private Document document;
	private Show show;

	protected boolean isDestroyed;

	public TextPlaceholder(@NotNull final String placeholderText, @NotNull final JTextComponent textComponent) {
		this(placeholderText, textComponent, Show.ALWAYS);
	}

	public TextPlaceholder(@NotNull final String placeholderText, @NotNull final JTextComponent textComponent,
			@NotNull final Show show) {
		this.show = show;
		this.textComponent = textComponent;
		document = textComponent.getDocument();

		setText(placeholderText);
		setFont(textComponent.getFont());
		setForeground(ColorUtils.changeAlpha(textComponent.getForeground(), 0.5f));
		// setBorder(new EmptyBorder(textComponent.getInsets()));
		setHorizontalAlignment(JLabel.LEADING);

		textComponent.addFocusListener(this);
		document.addDocumentListener(this);

		textComponent.setLayout(new BorderLayout());
		textComponent.add(this);
		displayIfPossible();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	public void changeFontStyle(int fontStyle) {
		setFont(getFont().deriveFont(fontStyle));
	}

	public void destroy() {
		if (isDestroyed) {
			return;
		}

		textComponent.removeFocusListener(this);
		document.removeDocumentListener(this);
		document = null;
		textComponent = null;
		isDestroyed = true;
	}

	public void displayIfPossible() {
		if (document.getLength() != 0) {
			setVisible(false);
			return;
		}

		if (show == Show.FOCUS_GAINED) {
			if (!textComponent.hasFocus()) {
				setVisible(false);
			} else {
				setVisible(true);
			}

			return;
		}

		if (show == Show.FOCUS_LOST) {
			if (textComponent.hasFocus()) {
				setVisible(false);
			} else {
				setVisible(true);
			}

			return;
		}

		setVisible(true);;
	}

	@Override
	public void focusGained(FocusEvent e) {
		displayIfPossible();
	}

	@Override
	public void focusLost(FocusEvent e) {
		displayIfPossible();
	}

	public boolean getIsDestroyed() {
		return isDestroyed;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		displayIfPossible();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		displayIfPossible();
	}
}
