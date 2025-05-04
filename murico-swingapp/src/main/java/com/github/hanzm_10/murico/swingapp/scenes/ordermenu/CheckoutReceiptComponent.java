package com.github.hanzm_10.murico.swingapp.scenes.ordermenu;

// --- Necessary Imports ---
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDFont;
// --- End Imports ---


/**
 * A component responsible for displaying the receipt preview (using Monospaced font)
 * and handling the generation and saving of the receipt as a PDF (using Courier font).
 */
public class CheckoutReceiptComponent extends JPanel {

    private JTextArea receiptArea;
    private JButton printReceiptButton;
    private final int currentBranchId; // Store branch ID for display

    // Constants for formatting and PDF layout
    // Format WITH symbol for display in JTextArea
    private static final DecimalFormat DISPLAY_CURRENCY_FORMAT = new DecimalFormat("₱ #,##0.00");
    // Format WITHOUT symbol for standard PDF fonts
    private static final DecimalFormat PDF_CURRENCY_FORMAT = new DecimalFormat("#,##0.00");
    private static final SimpleDateFormat RECEIPT_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    // PDF Constants
    private static final float RECEIPT_WIDTH_PT = 216f; // 3 inches
    private static final float SIDE_PADDING = 5f;
    private static final float TOP_MARGIN_PT = 15f;
    private static final float BOTTOM_MARGIN_PT = 20f;
    private static final float CONTENT_WIDTH = RECEIPT_WIDTH_PT - 2 * SIDE_PADDING;
    private static final float LINE_SPACING = 13f; // Adjusted for Monospaced/Courier
    private static final float ITEM_LINE_SPACING = 12f;
    // PDF Font sizes
    private static final float FONT_SIZE_HEADER = 11f;
    private static final float FONT_SIZE_NORMAL = 10f;
    private static final float FONT_SIZE_ITEMS = 9f;
    private static final float FONT_SIZE_TOTAL = 10f;
    // String Format Widths (adjust based on desired columns with Monospaced/Courier)
    private static final int WIDTH_ITEM = 18;  // Chars for item name
    private static final int WIDTH_QTY = 4;    // Chars for Qty
    private static final int WIDTH_PRICE = 9;   // Chars for Price (e.g., ###0.00)
    private static final int WIDTH_AMOUNT = 10; // Chars for Amount (e.g., ####0.00)
    private static final int RECEIPT_LINE_WIDTH_CHARS = WIDTH_ITEM + WIDTH_QTY + WIDTH_PRICE + WIDTH_AMOUNT;
    // Column Indices (matching CheckoutPanel/CheckoutTableComponent)
    private static final int COL_PRODUCT_NAME = 0;
    private static final int COL_QUANTITY = 2;
    private static final int COL_PRICE = 3;
    // PDF Column Alignment Points (relative to CONTENT_WIDTH)
    private static final float QTY_COL_END_X = CONTENT_WIDTH * 0.58f;
    private static final float PRICE_COL_END_X = CONTENT_WIDTH * 0.78f;
    private static final float AMOUNT_COL_END_X = CONTENT_WIDTH;


    /**
     * Creates the Receipt Component.
     * @param branchId The current branch ID.
     */
    public CheckoutReceiptComponent(int branchId) {
        this.currentBranchId = branchId;
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(320, 0)); // On-screen component width
        initializeReceiptUI();
        updateReceipt(null, -1); // Show initial empty state
    }

    /**
     * Initializes the UI components (TextArea, Button, ScrollPane).
     */
    private void initializeReceiptUI() {
        receiptArea = new JTextArea("--- RECEIPT PREVIEW ---\n");
        receiptArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11)); // Use standard monospaced font
        receiptArea.setEditable(false);
        receiptArea.setLineWrap(false); // Disable line wrap for alignment preview
        receiptArea.setWrapStyleWord(false);
        receiptArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY),
            new EmptyBorder(5, 5, 5, 5)) // Padding inside border
        );
        // Add HORIZONTAL scrollbar
        JScrollPane receiptScrollPane = new JScrollPane(receiptArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(receiptScrollPane, BorderLayout.CENTER); // Add text area to center

        // Button Area
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        printReceiptButton = new JButton("Print Receipt");
        printReceiptButton.setFont(new Font("Montserrat Bold", Font.BOLD, 12)); // Button font can differ
        printReceiptButton.addActionListener(e -> {
            // Get parent CheckoutPanel to ask for current data model
            Component parent = SwingUtilities.getAncestorOfClass(CheckoutPanel.class, this);
            if (parent instanceof CheckoutPanel checkoutParent) {
                 DefaultTableModel model = checkoutParent.getCheckoutTableModel(); // Requires getter in CheckoutPanel
                 // Assuming -1 for orderId means it's a preview before finalization
                 printReceipt(model, -1); // Call public printReceipt with current model
             } else {
                  JOptionPane.showMessageDialog(this, "Error: Cannot access checkout data to print.", "Internal Error", JOptionPane.ERROR_MESSAGE);
                  System.err.println("Could not find CheckoutPanel ancestor for Print action.");
             }
        });
        buttonPanel.add(printReceiptButton);
        add(buttonPanel, BorderLayout.SOUTH); // Add button panel to bottom
    }


    /**
     * Updates the receipt preview text area based on the provided table model.
     * Uses String.format optimized for monospaced fonts and DISPLAY_CURRENCY_FORMAT.
     * @param tableModel The current DefaultTableModel from the checkout table. Can be null.
     * @param orderId The finalized order ID, or -1 if not finalized yet.
     */
    public void updateReceipt(DefaultTableModel tableModel, int orderId) {
        if (receiptArea == null) return; // UI not ready

        StringBuilder receipt = new StringBuilder();

        // Helper function for centering text using character width constant
        java.util.function.Function<String, String> centerText = (text) -> {
            if (text == null) text = "";
            int padding = (RECEIPT_LINE_WIDTH_CHARS - text.length()) / 2;
            padding = Math.max(0, padding); // Ensure padding isn't negative
            return " ".repeat(padding) + text;
        };

        // --- Receipt Header (Centered) ---
        receipt.append(centerText.apply("Murico Branch #" + currentBranchId)).append("\n");
        String receiptNumStr = (orderId > 0) ? String.valueOf(orderId) : "[Pending Finalize]";
        receipt.append(centerText.apply("Receipt #: " + receiptNumStr)).append("\n");
        receipt.append(centerText.apply("Clerk: [Employee Name Placeholder]")).append("\n"); // TODO: Get clerk
        receipt.append(centerText.apply("Date: " + RECEIPT_DATE_FORMAT.format(new Date()))).append("\n");
        receipt.append("\n"); // Blank line

        // --- Separator & Table Header ---
        String separator = "-".repeat(RECEIPT_LINE_WIDTH_CHARS);
        receipt.append(separator).append("\n");
        // Format header using defined widths
        String headerLine = String.format("%-" + WIDTH_ITEM + "s%" + WIDTH_QTY + "s%" + WIDTH_PRICE + "s%" + WIDTH_AMOUNT + "s",
                                         "Item", "Qty", "Price", "Amount");
        receipt.append(headerLine).append("\n");
        receipt.append(separator).append("\n");

        // --- Receipt Items ---
        BigDecimal subtotal = BigDecimal.ZERO;
        if (tableModel != null) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                try {
                    String name = (String) tableModel.getValueAt(i, COL_PRODUCT_NAME);
                    Integer quantity = (Integer) tableModel.getValueAt(i, COL_QUANTITY);
                    BigDecimal price = (BigDecimal) tableModel.getValueAt(i, COL_PRICE);

                    if (name != null && quantity != null && price != null && quantity > 0) {
                        BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));
                        subtotal = subtotal.add(amount);

                        // Truncate long names if necessary
                        String displayName = (name.length() > WIDTH_ITEM) ? name.substring(0, WIDTH_ITEM) : name;

                        // Use DISPLAY_CURRENCY_FORMAT for JTextArea preview
                        String itemLine = String.format("%-" + WIDTH_ITEM + "s%" + WIDTH_QTY + "d%"+WIDTH_PRICE+"s%"+WIDTH_AMOUNT+"s",
                                                       displayName, quantity,
                                                       DISPLAY_CURRENCY_FORMAT.format(price), // Format price with symbol
                                                       DISPLAY_CURRENCY_FORMAT.format(amount)); // Format amount with symbol
                        receipt.append(itemLine).append("\n");
                    } else {
                         receipt.append(" * Invalid row data *\n");
                    }
                } catch (Exception e) {
                     receipt.append(" * Error processing row *\n");
                     System.err.println("Error processing receipt preview row " + i + ": " + e.getMessage());
                }
            }
        }

        // --- Receipt Footer ---
        receipt.append(separator).append("\n");
        String totalText = "Total:";
        String totalValue = DISPLAY_CURRENCY_FORMAT.format(subtotal); // Format total with symbol
        int totalLabelWidth = totalText.length();
        int valueWidth = RECEIPT_LINE_WIDTH_CHARS - totalLabelWidth; // Remaining space
        String totalLine = String.format("%-" + totalLabelWidth + "s%" + valueWidth + "s", totalText, totalValue);
        receipt.append(totalLine).append("\n");
        receipt.append("\n"); // Blank line
        receipt.append(centerText.apply("Thank you!")).append("\n");

        // Update JTextArea
        receiptArea.setText(receipt.toString());
        receiptArea.setCaretPosition(0); // Scroll back to the top
        System.out.println("Receipt preview updated (Monospaced format).");
    }


    /**
     * Calculates the approximate required height for the PDF receipt based on content.
     * @param model The table model containing checkout items. Can be null.
     * @return The calculated height in points.
     */
    private float calculateReceiptHeight(DefaultTableModel model) {
        int headerLines = 6;
        int itemLines = (model != null) ? model.getRowCount() : 0;
        int footerLines = 4;
        int totalLines = headerLines + itemLines + footerLines + 3; // Base lines + spacing
        // Use ITEM_LINE_SPACING for calculation as items are usually the bulk
        float estimatedHeight = (totalLines * ITEM_LINE_SPACING) + TOP_MARGIN_PT + BOTTOM_MARGIN_PT;
        return Math.max(200f, estimatedHeight); // Minimum height
    }


    /**
     * Public method to trigger PDF generation.
     * Called externally by CheckoutPanel or internally by button action listener.
     * @param tableModel The current DefaultTableModel from the checkout table.
     * @param orderId The finalized order ID, or -1 for a preview.
     */
    public void printReceipt(DefaultTableModel tableModel, int orderId) {
         printReceiptInternal(tableModel, orderId); // Calls the private implementation
    }


    /**
     * Internal method to handle PDF generation and saving using Apache PDFBox.
     * Uses Courier font for alignment. Uses PDF_CURRENCY_FORMAT (no symbol).
     * @param tableModel The table model containing the items to print. (Required)
     * @param orderId The finalized order ID, or -1 for a preview.
     */
    private void printReceiptInternal(DefaultTableModel tableModel, int orderId) {
        System.out.println("Print Receipt internal. Order ID: " + orderId);
        if (tableModel == null || tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Nothing to print.", "Empty Order", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // --- Suggest Filename ---
        String defaultFileName = (orderId > 0)
            ? "Receipt-" + orderId + ".pdf"
            : "Receipt-Preview-" + System.currentTimeMillis() + ".pdf";

        // --- File Chooser ---
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Receipt As PDF");
        fileChooser.setSelectedFile(new File(defaultFileName));
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        int userSelection = fileChooser.showSaveDialog(parentWindow);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure .pdf extension
            if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".pdf");
            }
            // Overwrite confirmation
            if (fileToSave.exists()) {
                 int overwriteConfirm = JOptionPane.showConfirmDialog(parentWindow,
                     "File already exists:\n" + fileToSave.getName() + "\nOverwrite?",
                     "Confirm Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                 if (overwriteConfirm != JOptionPane.YES_OPTION) {
                      System.out.println("PDF save cancelled by user (overwrite).");
                      return;
                 }
            }

            System.out.println("Attempting to save receipt PDF to: " + fileToSave.getAbsolutePath());

            // --- PDF Generation ---
            // Use standard Type 1 Courier fonts
            final PDFont fontRegular = PDType1Font.COURIER;       // Use Courier directly
            final PDFont fontBold = PDType1Font.COURIER_BOLD; // Use Courier Bold directly

            try (PDDocument document = new PDDocument()) {
                float calculatedHeight = calculateReceiptHeight(tableModel);
                PDRectangle pageSize = new PDRectangle(RECEIPT_WIDTH_PT, calculatedHeight);
                PDPage currentPage = new PDPage(pageSize);
                document.addPage(currentPage);

                // --- Define Drawing Area & Coordinates ---
                float margin = 5f; // Use SIDE_PADDING directly for clarity
                float contentWidth = RECEIPT_WIDTH_PT - 2 * margin; // Recalculate content width
                float yStart = currentPage.getMediaBox().getHeight() - TOP_MARGIN_PT;
                float yPosition = yStart;
                PDPageContentStream contentStream = null;

                // --- Helper function to get text width using reliable Courier metrics ---
                java.util.function.BiFunction<String, Float, Float> getTextWidth = (text, fontSize) -> {
                    try { return fontRegular.getStringWidth(text) / 1000 * fontSize; } // Use fontRegular (Courier)
                    catch (IOException e) { System.err.println("Err width: "+e); return 10f; }
                };
                java.util.function.BiFunction<String, Float, Float> getBoldTextWidth = (text, fontSize) -> {
                    try { return fontBold.getStringWidth(text) / 1000 * fontSize; } // Use fontBold (Courier Bold)
                    catch (IOException e) { System.err.println("Err bold width: "+e); return 10f; }
                };

                try {
                    contentStream = new PDPageContentStream(document, currentPage);
                    contentStream.beginText();
                    float currentX = margin; // Start X position

                    // --- Write Header (Centered) ---
                    String line1 = "Murico Branch #" + currentBranchId; float line1X = margin + (contentWidth - getBoldTextWidth.apply(line1, FONT_SIZE_HEADER)) / 2; contentStream.setFont(fontBold, FONT_SIZE_HEADER); contentStream.newLineAtOffset(line1X, yPosition); contentStream.showText(line1); yPosition -= LINE_SPACING; currentX = line1X;
                    contentStream.setFont(fontRegular, FONT_SIZE_NORMAL); String receiptNumStr = (orderId > 0) ? String.valueOf(orderId) : "[Preview]"; String line2 = "Receipt #: " + receiptNumStr; float line2X = margin + (contentWidth - getTextWidth.apply(line2, FONT_SIZE_NORMAL)) / 2; contentStream.newLineAtOffset(line2X - currentX, -LINE_SPACING); contentStream.showText(line2); yPosition -= LINE_SPACING; currentX = line2X;
                    String line3 = "Clerk: [Employee Name Placeholder]"; float line3X = margin + (contentWidth - getTextWidth.apply(line3, FONT_SIZE_NORMAL)) / 2; contentStream.newLineAtOffset(line3X - currentX, -LINE_SPACING); contentStream.showText(line3); yPosition -= LINE_SPACING; currentX = line3X;
                    String line4 = "Date: " + RECEIPT_DATE_FORMAT.format(new Date()); float line4X = margin + (contentWidth - getTextWidth.apply(line4, FONT_SIZE_NORMAL)) / 2; contentStream.newLineAtOffset(line4X - currentX, -LINE_SPACING); contentStream.showText(line4); yPosition -= LINE_SPACING; currentX = line4X;
                    contentStream.newLineAtOffset(margin - currentX, -LINE_SPACING * 0.5f); yPosition -= LINE_SPACING * 0.5f; currentX = margin; // Reset X

                    // --- Write Table Header (Aligned using String.format widths) ---
                     // Adjust widths slightly - smaller total width needed?
                     int wItem = 17; int wQty = 3; int wPrice = 8; int wAmount = 9; // Sum = 37
                     int lineWidthChars = wItem + wQty + wPrice + wAmount;
                    String separator = "-".repeat(lineWidthChars);
                    contentStream.setFont(fontRegular, FONT_SIZE_ITEMS);
                    contentStream.showText(separator); yPosition -= ITEM_LINE_SPACING; contentStream.newLineAtOffset(0, -ITEM_LINE_SPACING);
                    String headerLine = String.format("%-" + wItem + "s%" + wQty + "s%" + wPrice + "s%" + wAmount + "s", "Item", "Qty", "Price", "Amount");
                    contentStream.showText(headerLine); yPosition -= ITEM_LINE_SPACING; contentStream.newLineAtOffset(0, -ITEM_LINE_SPACING);
                    contentStream.showText(separator); yPosition -= ITEM_LINE_SPACING; contentStream.newLineAtOffset(0, -ITEM_LINE_SPACING);

                    // --- Write Table Items (Aligned using String.format widths) ---
                    BigDecimal total = BigDecimal.ZERO;
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        // Optional: Page Break Check
                        if (yPosition < BOTTOM_MARGIN_PT + ITEM_LINE_SPACING) { /* ... handle page break ... */ }

                        String name = (String) tableModel.getValueAt(i, COL_PRODUCT_NAME);
                        Integer quantity = (Integer) tableModel.getValueAt(i, COL_QUANTITY);
                        BigDecimal itemPrice = (BigDecimal) tableModel.getValueAt(i, COL_PRICE);
                        if (name != null && quantity != null && itemPrice != null && quantity > 0) {
                            BigDecimal amount = itemPrice.multiply(BigDecimal.valueOf(quantity));
                            total = total.add(amount);
                            String displayName = (name.length() > wItem) ? name.substring(0, wItem) : name;
                            // Use the adjusted widths
                            String itemLine = String.format("%-" + wItem + "s%" + wQty + "d%" + wPrice + ".2f%" + wAmount + ".2f",
                                                           displayName, quantity, itemPrice, amount);
                            contentStream.showText(itemLine);
                            yPosition -= ITEM_LINE_SPACING; contentStream.newLineAtOffset(0, -ITEM_LINE_SPACING);
                        }
                    } // End for loop

                    // --- Write Footer ---
                    contentStream.setFont(fontRegular, FONT_SIZE_NORMAL);
                    contentStream.showText(separator); yPosition -= LINE_SPACING; contentStream.newLineAtOffset(0, -LINE_SPACING);

                    contentStream.setFont(fontBold, FONT_SIZE_TOTAL);
                    String totalText = "Total:";
                    String totalValueStr = PDF_CURRENCY_FORMAT.format(total); // No symbol
                    int totalLabelWidth = totalText.length();
                    // Calculate remaining space based on ADJUSTED line width
                    int valueWidth = lineWidthChars - totalLabelWidth;
                    String totalLine = String.format("%-" + totalLabelWidth + "s%" + valueWidth + "s", totalText, totalValueStr);
                    contentStream.showText(totalLine); yPosition -= LINE_SPACING; contentStream.newLineAtOffset(0, -LINE_SPACING);

                    yPosition -= LINE_SPACING; contentStream.newLineAtOffset(0, -LINE_SPACING); // Extra space

                    contentStream.setFont(fontRegular, FONT_SIZE_NORMAL);
                    String thankYouLine = "Thank you!";
                    // Center within the CONTENT_WIDTH
                    float thankYouX = margin + (contentWidth - getTextWidth.apply(thankYouLine, FONT_SIZE_NORMAL)) / 2;
                    contentStream.newLineAtOffset(thankYouX - margin, 0); // Adjust X relative to start
                    contentStream.showText(thankYouLine);

                    contentStream.endText(); // End the final text block

                } finally { if (contentStream != null) contentStream.close(); } // Close stream

                document.save(fileToSave);
                JOptionPane.showMessageDialog(parentWindow, "Receipt saved.", "PDF Saved", 1);
            } catch (IOException e) { e.printStackTrace(); JOptionPane.showMessageDialog(parentWindow,"PDF IO Error: "+e.getMessage(),"Error",0); }
              catch (Exception e) { e.printStackTrace(); JOptionPane.showMessageDialog(parentWindow,"Unexpected PDF Error: "+e.getMessage(),"Error",0); }
         } else { System.out.println("Save cancelled."); }
    } // --- End printReceiptInternal ---

} // End CheckoutReceiptComponent