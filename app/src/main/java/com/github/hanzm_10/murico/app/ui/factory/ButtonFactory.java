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
package com.github.hanzm_10.murico.app.ui.factory;

import java.awt.Dimension;

import javax.swing.JButton;

/** Creates buttons for the application. */
public class ButtonFactory {
	/**
	 * Creates a button with the specified text and action command.
	 *
	 * @param text
	 *            the text to display on the button
	 * @param actionCommand
	 *            the action command for the button
	 * @return a JButton with the specified text and action command
	 */
	public static JButton createButton(String text, String actionCommand) {
		var button = new JButton(text);
		button.setActionCommand(actionCommand);

		return button;
	}

	/**
	 * Creates a button with the specified text, action command, and size.
	 *
	 * @param text
	 *            the text to display on the button
	 * @param actionCommand
	 *            the action command for the button
	 * @param size
	 *            the preferred size of the button
	 * @return a JButton with the specified text, action command, and size
	 */
	public static JButton createButton(String text, String actionCommand, Dimension size) {
		var button = createButton(text, actionCommand);
		button.setPreferredSize(size);
		button.setSize(size);

		return button;
	}
}
