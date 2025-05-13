package com.github.hanzm_10.murico.swingapp.scenes.inventory.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout; // Keep for potential inner panel if needed
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class StockLevelRenderer extends JPanel implements TableCellRenderer {
    private final JLabel statusLabel;
    private final JProgressBar progressBar;
    // private final JPanel progressBarContainer; // Optional: for fine-tuning progress bar padding/alignment

    public StockLevelRenderer() {
        // The StockLevelRenderer itself is a JPanel using BorderLayout
        super(new BorderLayout(5, 0)); // 5px horizontal gap between components (WEST, CENTER, EAST)
        setOpaque(true); // Important for background colors to show correctly
        setBorder(new EmptyBorder(2, 5, 2, 5)); // Padding around the entire cell content

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Montserrat", Font.PLAIN, 11));
        // No fixed preferred size for statusLabel initially, let BorderLayout handle it.
        // If text truncation becomes an issue, or alignment problems persist,
        // you might set a preferred width for it.

        progressBar = new JProgressBar(0, 100); // Max is always 100
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(160, 20)); // Give progress bar a preferred width
                                                            // Adjust this width as needed.
        progressBar.setMinimumSize(new Dimension(60, 12));


        // Add statusLabel to the WEST (left)
        add(statusLabel, BorderLayout.WEST);

        // Add progressBar to the EAST (right)
        // To give the progress bar some padding or ensure it doesn't stretch too much if
        // the center area is large, you can wrap it in another panel.
        // For simple right alignment, adding directly to EAST often works if WEST and CENTER are occupied or well-behaved.
        // If there's nothing in BorderLayout.CENTER, EAST might take more space than desired.
        // Let's try adding directly first. If it stretches too much, we wrap it.
        add(progressBar, BorderLayout.EAST);

        // If progressBar in EAST stretches too much, use this alternative:
        // JPanel progressBarContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); // Aligns bar to its right
        // progressBarContainer.setOpaque(false);
        // progressBarContainer.add(progressBar);
        // add(progressBarContainer, BorderLayout.EAST);

        // You could add a Box.createHorizontalGlue() or an empty JLabel to BorderLayout.CENTER
        // if you want to ensure WEST and EAST components are pushed to the very edges.
        // add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        // Or:
        // JLabel centerSpacer = new JLabel(); // Empty label
        // add(centerSpacer, BorderLayout.CENTER); // Will take up remaining space

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof StockInfo) {
            StockInfo info = (StockInfo) value;
            int qty = info.getQuantity();
            int minQty = info.getMinimumQuantity();

            int progressBarValue = Math.min(qty, 100);
            progressBar.setValue(progressBarValue);

            String statusText = qty + " unit";
            Color barColor;

            if (qty <= minQty && minQty > 0) {
                statusText += " Low";
                barColor = new Color(0xFF6347); // Tomato Red
            } else if (minQty > 0 && qty <= minQty + 10) {
                statusText += " Mild";
                barColor = new Color(0xFFB000); // Amber/Orange
            } else if (qty >= 90) {
                statusText += " High";
                barColor = new Color(0x32CD32); // Lime Green
            } else { // Between (minQty + 10) and 89
                statusText += " Mid";
                barColor = new Color(0x32CD32); // Default Green for Mid, or another color
            }

            statusLabel.setText(statusText);
            progressBar.setForeground(barColor);

        } else {
            statusLabel.setText("N/A");
            progressBar.setValue(0);
            progressBar.setForeground(Color.GRAY);
        }

        // Handle selection highlighting
        if (isSelected) {
            this.setBackground(table.getSelectionBackground());
            statusLabel.setForeground(table.getSelectionForeground());
            // For JProgressBar, selection color is usually not applied to the bar itself,
            // but the panel background will cover it.
        } else {
            this.setBackground(table.getBackground());
            statusLabel.setForeground(table.getForeground());
        }
        return this;
    }
}