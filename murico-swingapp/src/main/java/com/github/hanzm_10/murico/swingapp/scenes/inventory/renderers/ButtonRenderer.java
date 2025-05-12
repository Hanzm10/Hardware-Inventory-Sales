package com.github.hanzm_10.murico.swingapp.scenes.inventory.renderers;

import java.awt.Component;
import java.awt.Dimension;
import java.net.URL;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class ButtonRenderer extends JButton implements TableCellRenderer {
    private final ImageIcon ellipsisIcon;

    public ButtonRenderer() {
        setOpaque(true); // May depend on LookAndFeel
        setBorderPainted(false);
        setContentAreaFilled(false); // Make transparent
        setFocusPainted(false);

        URL iconUrl = getClass().getResource("/icons/edit_button.png"); // ADJUST PATH
        if (iconUrl != null) {
            ellipsisIcon = new ImageIcon(iconUrl);
            setIcon(ellipsisIcon);
        } else {
            System.err.println("Ellipsis icon not found!");
            setText("...");
            ellipsisIcon = null;
        }
        setPreferredSize(new Dimension(30, 30));
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setToolTipText("Edit Item Details");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Don't change background on selection for icon buttons usually
        // if (isSelected) {
        //     setBackground(table.getSelectionBackground());
        // } else {
        //     setBackground(UIManager.getColor("Button.background"));
        // }
        return this;
    }
}