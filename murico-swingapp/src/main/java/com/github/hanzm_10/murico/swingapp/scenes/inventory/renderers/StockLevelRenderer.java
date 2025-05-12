package com.github.hanzm_10.murico.swingapp.scenes.inventory.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class StockLevelRenderer extends JPanel implements TableCellRenderer {
    private final JLabel statusLabel;
    private final JProgressBar progressBar;

    public StockLevelRenderer() {
        super(new BorderLayout(5, 0));
        setOpaque(true);
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Montserrat", Font.PLAIN, 11));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(80, 12));

        add(statusLabel, BorderLayout.WEST);
        add(progressBar, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof StockInfo) {
            StockInfo info = (StockInfo) value;
            int qty = info.getQuantity();
            int minQty = info.getMinimumQuantity();
            double percentage = 100.0;
            String statusText = qty + " unit";

            if (minQty > 0 && qty < minQty ) { // Calculate percentage based on minimum only if below it
                 percentage = Math.max(0, Math.min(100.0, ((double) qty / minQty) * 100.0));
            } else if (minQty <= 0) {
                 percentage = 100.0; // Assume full if no minimum set
            } else {
                percentage = 100.0; // Full if at or above minimum
            }
            // Note: A different scale might be needed if qty can greatly exceed minQty
            // Example: progressBar.setMaximum(minQty * 2); progressBar.setValue(qty);

            progressBar.setValue((int) percentage);

            Color barColor;
            // Logic based on percentage OF minimum (if applicable)
            if (minQty > 0) {
                 if (percentage < 25.0) { // Significantly below minimum
                     statusText += " Low";
                     barColor = new Color(0xFF6347); // Tomato Red
                 } else if (percentage < 75.0) { // Approaching minimum
                     statusText += " Mild"; // Changed from Mid
                     barColor = new Color(0xFFB000); // Amber/Orange
                 } else { // At or slightly above minimum (but maybe we want a higher threshold for "High")
                     statusText += " High"; // Simplification: High if >= 75% of min or min is 0
                     barColor = new Color(0x32CD32); // Lime Green
                 }
            } else { // No minimum defined
                 statusText += " High"; // Default to High if no min qty
                 barColor = new Color(0x32CD32);
            }


            statusLabel.setText(statusText);
            progressBar.setForeground(barColor);

        } else {
            statusLabel.setText("N/A");
            progressBar.setValue(0);
            progressBar.setForeground(Color.GRAY);
        }

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
            statusLabel.setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
            statusLabel.setForeground(table.getForeground());
        }

        return this;
    }
}