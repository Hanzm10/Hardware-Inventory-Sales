package com.github.hanzm_10.murico.app.ui.factory;

import java.awt.Dimension;
import javax.swing.JButton;

/**
 * Creates buttons for the application.
 */
public class ButtonFactory {
    /**
     * Creates a button with the specified text and action command.
     *
     * @param text the text to display on the button
     * @param actionCommand the action command for the button
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
     * @param text the text to display on the button
     * @param actionCommand the action command for the button
     * @param size the preferred size of the button
     * @return a JButton with the specified text, action command, and size
     */
    public static JButton createButton(String text, String actionCommand, Dimension size) {
        var button = createButton(text, actionCommand);
        button.setPreferredSize(size);
        button.setSize(size);

        return button;
    }

}
