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
package com.github.hanzm_10.murico.swingapp.scenes.ordermenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 * A component responsible for displaying the receipt preview (using Monospaced
 * font) and handling the generation and saving of the receipt as a PDF (using
 * embedded DejaVu Sans Mono font).
 */
public class CheckoutReceiptComponent extends JPanel {

	private JTextArea receiptArea;
	private JButton printReceiptButton;

	// Constants for formatting and PDF layout
	private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₱#,##0.00");
	private static final SimpleDateFormat DATETIME_FORMAT_PDF = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	// PDF Constants
	private static final float RECEIPT_WIDTH_PT = 216f; // 3 inches
	private static final float SIDE_PADDING = 8f;
	private static final float TOP_MARGIN_PT = 10f;
	private static final float BOTTOM_MARGIN_PT = 15f;
	private static final float CONTENT_WIDTH = RECEIPT_WIDTH_PT - 2 * SIDE_PADDING;

	// PDF Line Spacing
	private static final float LINE_SPACING_HEADER = 9f;
	private static final float LINE_SPACING_ITEM_DESC = 9f;
	private static final float LINE_SPACING_ITEM_TOTAL_OFFSET = 8f; // For the second line of item
	private static final float LINE_SPACING_SECTION_BREAK = 6f; // Small break
	private static final float LINE_SPACING_REGULAR = 9f;
	private static final float LINE_SPACING_AFTER_TOTALS_GROUP = 10f;
	private static final float LINE_SPACING_FOOTER_FILL = 9f;

	// PDF Font sizes
	private static final float FONT_SIZE_BUSINESS_NAME = 10f;
	private static final float FONT_SIZE_ADDRESS_TIN_POS = 7f;
	private static final float FONT_SIZE_ITEM_LINE = 7.5f;
	private static final float FONT_SIZE_FINANCIAL_LABEL = 7.5f;
	private static final float FONT_SIZE_FINANCIAL_VALUE = 7.5f;
	private static final float FONT_SIZE_TRANSACTION_DETAILS = 7.5f;
	private static final float FONT_SIZE_CUSTOMER_FILL_LABEL = 7f;
	private static final float FONT_SIZE_FOOTER_MSG = 7.5f;

	private static final String CUSTOMER_LINE_PLACEHOLDER = "------------------------------";

	// Business Details
	private static final String BUSINESS_NAME = "MURICO, INC.";
	private static final String ADDRESS_LINE_1 = "G/F Murico Plaza, 123 Mabini St.";
	private static final String ADDRESS_LINE_2 = "Brgy. Central, Quezon City";
	private static final String ADDRESS_LINE_3 = "Metro Manila, Philippines 1100";
	private static final String TIN_NUMBER = "009-876-543-00000";
	private static final String CASHIER_PLACEHOLDER = "Murico Staff";

	// VAT Rate
	private static final BigDecimal VAT_RATE = new BigDecimal("0.12");

	// Column Indices
	private static final int COL_PRODUCT_NAME = 0;
	private static final int COL_QUANTITY = 2;
	private static final int COL_PRICE = 3;

	public CheckoutReceiptComponent() {
		setLayout(new BorderLayout(5, 5));
		setPreferredSize(new Dimension(320, 0));
		initializeReceiptUI();
		updateReceipt(null, -1, BigDecimal.ZERO); // Initialize with zero cash tendered
	}

	private void initializeReceiptUI() {
		receiptArea = new JTextArea("--- RECEIPT PREVIEW ---\n(Actual PDF will differ in layout)\n");
		receiptArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		receiptArea.setEditable(false);
		receiptArea.setLineWrap(true);
		receiptArea.setWrapStyleWord(true);
		receiptArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
				new EmptyBorder(5, 5, 5, 5)));
		JScrollPane receiptScrollPane = new JScrollPane(receiptArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(receiptScrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		printReceiptButton = new JButton("Print Receipt (PDF)");
		printReceiptButton.setFont(new Font("Montserrat Bold", Font.BOLD, 12));
		printReceiptButton.addActionListener(e -> {
			Component parent = SwingUtilities.getAncestorOfClass(CheckoutPanel.class, this);
			if (parent instanceof CheckoutPanel checkoutParent) {
				DefaultTableModel model = checkoutParent.getCheckoutTableModel();
				BigDecimal cashTendered = checkoutParent.getCashTenderedAmount();
				int orderId = checkoutParent.getLastFinalizedOrderId();

				if (model == null || model.getRowCount() == 0) {
					JOptionPane.showMessageDialog(this, "Nothing to print in receipt. Add items first.", "Empty Order",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (cashTendered == null) { // Check if cash tendered is valid (e.g. not null from parsing error)
					JOptionPane.showMessageDialog(this, "Please enter a valid cash tendered amount before printing.",
							"Invalid Cash", JOptionPane.WARNING_MESSAGE);
					return;
				}
				// Further check: if cash is required to be >= total for printing
				// BigDecimal totalDue = calculateTotalFromModel(model);
				// if (cashTendered.compareTo(totalDue) < 0) {
				// JOptionPane.showMessageDialog(this, "Cash tendered is less than total due.
				// Cannot
				// print receipt yet.", "Insufficient Payment", JOptionPane.WARNING_MESSAGE);
				// return;
				// }

				printReceipt(model, orderId, cashTendered);
			} else {
				JOptionPane.showMessageDialog(this, "Error: Cannot access checkout data.", "Internal Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		buttonPanel.add(printReceiptButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private BigDecimal calculateTotalFromModel(DefaultTableModel model) {
		BigDecimal total = BigDecimal.ZERO;
		if (model != null) {
			for (int i = 0; i < model.getRowCount(); i++) {
				try {
					Integer quantity = (Integer) model.getValueAt(i, COL_QUANTITY);
					BigDecimal price = (BigDecimal) model.getValueAt(i, COL_PRICE);
					if (quantity != null && price != null && quantity > 0) {
						total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
					}
				} catch (Exception e) {
					/* ignore row errors for this calculation */
				}
			}
		}
		return total;
	}

	public void updateReceipt(DefaultTableModel tableModel, int orderId, BigDecimal cashTendered) {
		if (receiptArea == null)
			return;
		// Ensure cashTendered is not null for preview generation
		BigDecimal validCashTendered = (cashTendered != null) ? cashTendered : BigDecimal.ZERO;
		String receiptContent = generateReceiptTextForPreview(tableModel, orderId, validCashTendered);
		receiptArea.setText(receiptContent);
		receiptArea.setCaretPosition(0);
	}

	private String generateItemShortcut(String itemName) {
		if (itemName == null || itemName.trim().isEmpty()) {
			return "ITEM";
		}
		String[] words = itemName.trim().split("\\s+");
		StringBuilder shortcut = new StringBuilder();
		for (String word : words) {
			if (word.length() >= 3) {
				shortcut.append(word.substring(0, 3).toUpperCase());
			} else if (!word.isEmpty()) {
				shortcut.append(word.toUpperCase());
			}
		}
		return shortcut.toString();
	}

	private String generateReceiptTextForPreview(DefaultTableModel tableModel, int orderId, BigDecimal cashTendered) {
		StringBuilder sb = new StringBuilder();
		String orderIdStr = (orderId > 0) ? String.valueOf(orderId) : "[PENDING]";

		sb.append(centerTextForPreview(BUSINESS_NAME, 40)).append("\n");
		sb.append(centerTextForPreview(ADDRESS_LINE_1, 40)).append("\n");
		sb.append(centerTextForPreview(ADDRESS_LINE_2, 40)).append("\n");
		sb.append(centerTextForPreview(ADDRESS_LINE_3, 40)).append("\n");
		sb.append(centerTextForPreview("VAT Registered TIN: " + TIN_NUMBER, 40)).append("\n");
		sb.append(centerTextForPreview("POS# " + orderIdStr, 40)).append("\n\n");

		sb.append("ITEMS:\n");
		String itemHeader = String.format("%-4s %-15s %-8s %-10s", "Qty", "Item", "Price", "Amount");
		sb.append(itemHeader).append("\n");
		sb.append("-".repeat(itemHeader.length())).append("\n");

		BigDecimal itemsTotal = BigDecimal.ZERO;
		int totalItemsCount = 0;

		if (tableModel != null) {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				try {
					String name = (String) tableModel.getValueAt(i, COL_PRODUCT_NAME);
					Integer quantity = (Integer) tableModel.getValueAt(i, COL_QUANTITY);
					BigDecimal price = (BigDecimal) tableModel.getValueAt(i, COL_PRICE);

					if (name != null && quantity != null && price != null && quantity > 0) {
						BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));
						itemsTotal = itemsTotal.add(amount);
						totalItemsCount += quantity;

						sb.append(String.format("%-4d %-15.15s %-8s %-10s\n", // Truncate item name for preview
								quantity, name, CURRENCY_FORMAT.format(price), // JTextArea can show ₱
								CURRENCY_FORMAT.format(amount)));
					}
				} catch (Exception e) {
					/* ignore row errors for preview */
				}
			}
		}
		sb.append("-".repeat(itemHeader.length())).append("\n");

		sb.append(String.format("%28s %s\n", "TOTAL:", CURRENCY_FORMAT.format(itemsTotal))); // Assuming subtotal is total
		// for preview

		BigDecimal change = cashTendered.subtract(itemsTotal);
		if (change.compareTo(BigDecimal.ZERO) < 0)
			change = BigDecimal.ZERO;
		sb.append(String.format("%28s %s\n", "CASH:", CURRENCY_FORMAT.format(cashTendered)));
		sb.append(String.format("%28s %s\n", "CHANGE:", CURRENCY_FORMAT.format(change)));
		sb.append("\n");

		BigDecimal netOfVat = itemsTotal.divide(BigDecimal.ONE.add(VAT_RATE), 2, RoundingMode.HALF_UP);
		BigDecimal vatPayable = itemsTotal.subtract(netOfVat);
		sb.append(String.format("VAT Sales: %s\n", CURRENCY_FORMAT.format(netOfVat)));
		sb.append(String.format("VAT (%s%%): %s\n",
				VAT_RATE.multiply(BigDecimal.valueOf(100)).stripTrailingZeros().toPlainString(),
				CURRENCY_FORMAT.format(vatPayable)));
		sb.append("\n");

		sb.append("TOTAL ITEM(S) : ").append(totalItemsCount).append("\n");
		sb.append("INV. # : ").append(orderIdStr).append("\n");
		sb.append("CASHIER : ").append(CASHIER_PLACEHOLDER).append("\n");
		sb.append("TIME : ").append(DATETIME_FORMAT_PDF.format(new Date())).append("\n\n");
		sb.append(centerTextForPreview("This serves as your invoice.", 40)).append("\n");

		return sb.toString();
	}

	private String centerTextForPreview(String text, int lineWidth) {
		if (text == null)
			text = "";
		int padding = (lineWidth - text.length()) / 2;
		return " ".repeat(Math.max(0, padding)) + text;
	}

	public void printReceipt(DefaultTableModel tableModel, int orderId, BigDecimal cashTendered) {
		printReceiptInternal(tableModel, orderId, cashTendered);
	}

	private float calculateReceiptHeight(DefaultTableModel model) {
		float currentHeight = TOP_MARGIN_PT + BOTTOM_MARGIN_PT;
		currentHeight += LINE_SPACING_HEADER * 6;
		currentHeight += LINE_SPACING_SECTION_BREAK * 2;

		int itemCount = (model != null) ? model.getRowCount() : 0;
		currentHeight += (LINE_SPACING_ITEM_DESC + LINE_SPACING_ITEM_TOTAL_OFFSET) * itemCount;
		currentHeight += LINE_SPACING_SECTION_BREAK;

		currentHeight += LINE_SPACING_REGULAR * 4;
		currentHeight += LINE_SPACING_SECTION_BREAK;
		currentHeight += LINE_SPACING_AFTER_TOTALS_GROUP;

		currentHeight += LINE_SPACING_REGULAR * 4;
		currentHeight += LINE_SPACING_SECTION_BREAK;
		currentHeight += LINE_SPACING_AFTER_TOTALS_GROUP;

		currentHeight += LINE_SPACING_REGULAR * 4;
		currentHeight += LINE_SPACING_SECTION_BREAK;

		currentHeight += LINE_SPACING_FOOTER_FILL * 3;
		currentHeight += LINE_SPACING_SECTION_BREAK;

		currentHeight += LINE_SPACING_REGULAR;
		return Math.max(350f, currentHeight);
	}

	private void drawTextCentered(PDPageContentStream stream, PDFont font, float fontSize, float y, String text)
			throws IOException {
		stream.setFont(font, fontSize);
		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		float x = SIDE_PADDING + (CONTENT_WIDTH - textWidth) / 2;
		if (x < SIDE_PADDING)
			x = SIDE_PADDING;
		stream.beginText();
		stream.newLineAtOffset(x, y);
		stream.showText(text);
		stream.endText();
	}

	private void drawTextLeft(PDPageContentStream stream, PDFont font, float fontSize, float x, float y, String text)
			throws IOException {
		stream.setFont(font, fontSize);
		stream.beginText();
		stream.newLineAtOffset(x, y);
		stream.showText(text);
		stream.endText();
	}

	private void drawTextRight(PDPageContentStream stream, PDFont font, float fontSize, float y, String text)
			throws IOException {
		stream.setFont(font, fontSize);
		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		float x = SIDE_PADDING + CONTENT_WIDTH - textWidth;
		stream.beginText();
		stream.newLineAtOffset(x, y);
		stream.showText(text);
		stream.endText();
	}

	private void drawLabelValueLine(PDPageContentStream stream, PDFont font, float fontSize, float y, String label,
			String value) throws IOException {
		drawTextLeft(stream, font, fontSize, SIDE_PADDING, y, label);
		drawTextRight(stream, font, fontSize, y, value);
	}

	private String getDynamicDottedLine(PDFont font, float fontSize) throws IOException {
		StringBuilder line = new StringBuilder();
		float dashWidth = font.getStringWidth("-") / 1000 * fontSize;
		if (dashWidth == 0)
			dashWidth = fontSize * 0.4f;
		int numDashes = (int) (CONTENT_WIDTH / dashWidth);
		for (int i = 0; i < numDashes; i++)
			line.append("-");
		return line.toString();
	}

	private void printReceiptInternal(DefaultTableModel tableModel, int orderId, BigDecimal cashTendered) {
		if (tableModel == null || tableModel.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "Nothing to print.", "Empty Order", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// Ensure cashTendered is not null for PDF generation.
		// CheckoutPanel should ideally prevent null from reaching here if it's
		// critical.
		BigDecimal validCashTendered = (cashTendered != null) ? cashTendered : BigDecimal.ZERO;

		String defaultFileName = (orderId > 0)
				? "MuricoReceipt-" + orderId + ".pdf"
				: "MuricoReceipt-Preview-" + System.currentTimeMillis() + ".pdf";
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save Receipt As PDF");
		fileChooser.setSelectedFile(new File(defaultFileName));
		Window parentWindow = SwingUtilities.getWindowAncestor(this);
		int userSelection = fileChooser.showSaveDialog(parentWindow);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
				fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".pdf");
			}
			if (fileToSave.exists()) {
				int overwriteConfirm = JOptionPane.showConfirmDialog(parentWindow, "Overwrite existing file?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (overwriteConfirm != JOptionPane.YES_OPTION) {
					System.out.println("Save cancelled.");
					return;
				}
			}

			PDFont fontRegular = null;
			PDFont fontBold = null;

			try (PDDocument document = new PDDocument()) {
				// --- Load Custom Fonts ---
				try (InputStream fontStream = getClass().getResourceAsStream("/fonts/DejaVuSansMono.ttf");
						InputStream fontBoldStream = getClass().getResourceAsStream("/fonts/DejaVuSansMono-Bold.ttf")) {

					if (fontStream == null) {
						throw new IOException(
								"Regular font (DejaVuSansMono.ttf) not found in classpath resources/fonts/");
					}
					if (fontBoldStream == null) {
						throw new IOException(
								"Bold font (DejaVuSansMono-Bold.ttf) not found in classpath resources/fonts/");
					}
					fontRegular = PDType0Font.load(document, fontStream);
					fontBold = PDType0Font.load(document, fontBoldStream);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(parentWindow,
							"Error loading required fonts for PDF: " + e.getMessage() + "\n"
									+ "Please ensure DejaVuSansMono.ttf and DejaVuSansMono-Bold.ttf are in"
									+ " resources/fonts.",
							"Font Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					return;
				}
				// --- End Font Loading ---

				float calculatedHeight = calculateReceiptHeight(tableModel);
				PDPage currentPage = new PDPage(new PDRectangle(RECEIPT_WIDTH_PT, calculatedHeight));
				document.addPage(currentPage);

				PDPageContentStream contentStream = new PDPageContentStream(document, currentPage);
				float y = currentPage.getMediaBox().getHeight() - TOP_MARGIN_PT;
				String orderIdStr = (orderId > 0) ? String.valueOf(orderId) : "[PENDING]";

				// --- Header ---
				drawTextCentered(contentStream, fontBold, FONT_SIZE_BUSINESS_NAME, y, BUSINESS_NAME);
				y -= LINE_SPACING_HEADER;
				drawTextCentered(contentStream, fontRegular, FONT_SIZE_ADDRESS_TIN_POS, y, ADDRESS_LINE_1);
				y -= LINE_SPACING_HEADER;
				drawTextCentered(contentStream, fontRegular, FONT_SIZE_ADDRESS_TIN_POS, y, ADDRESS_LINE_2);
				y -= LINE_SPACING_HEADER;
				drawTextCentered(contentStream, fontRegular, FONT_SIZE_ADDRESS_TIN_POS, y, ADDRESS_LINE_3);
				y -= LINE_SPACING_HEADER;
				drawTextCentered(contentStream, fontRegular, FONT_SIZE_ADDRESS_TIN_POS, y,
						"VAT Registered TIN: " + TIN_NUMBER);
				y -= LINE_SPACING_HEADER;
				drawTextCentered(contentStream, fontRegular, FONT_SIZE_ADDRESS_TIN_POS, y, "POS# " + orderIdStr);
				y -= LINE_SPACING_HEADER;
				y -= LINE_SPACING_SECTION_BREAK * 2;
				
                drawTextLeft(contentStream, fontBold, FONT_SIZE_TRANSACTION_DETAILS, SIDE_PADDING, y, "ITEMS:");
                y -= LINE_SPACING_REGULAR;

				// --- Items ---
				BigDecimal orderTotalAmount = BigDecimal.ZERO;
				int totalItemsCount = 0;
				if (tableModel != null) {
					for (int i = 0; i < tableModel.getRowCount(); i++) {
						String name = (String) tableModel.getValueAt(i, COL_PRODUCT_NAME);
						Integer quantity = (Integer) tableModel.getValueAt(i, COL_QUANTITY);
						BigDecimal pricePerItem = (BigDecimal) tableModel.getValueAt(i, COL_PRICE);

						if (name == null || quantity == null || pricePerItem == null || quantity <= 0)
							continue; // Skip invalid rows

						BigDecimal itemTotalAmount = pricePerItem.multiply(BigDecimal.valueOf(quantity));
						orderTotalAmount = orderTotalAmount.add(itemTotalAmount);
						totalItemsCount += quantity;

						String itemShortcut = generateItemShortcut(name);
						String itemDescLine = String.format("%d %s @%s", quantity, itemShortcut,
								CURRENCY_FORMAT.format(pricePerItem));

						drawTextLeft(contentStream, fontRegular, FONT_SIZE_ITEM_LINE, SIDE_PADDING, y, itemDescLine);
						y -= LINE_SPACING_ITEM_DESC;
						drawTextRight(contentStream, fontRegular, FONT_SIZE_ITEM_LINE, y,
								CURRENCY_FORMAT.format(itemTotalAmount));
						y -= LINE_SPACING_ITEM_TOTAL_OFFSET;
					}
				}
				y -= LINE_SPACING_SECTION_BREAK;
				
				String dottedLine = getDynamicDottedLine(fontRegular, FONT_SIZE_FINANCIAL_LABEL);
				drawTextLeft(contentStream, fontRegular, FONT_SIZE_FINANCIAL_LABEL, SIDE_PADDING, y, dottedLine);
				y -= LINE_SPACING_REGULAR;

				// --- Financial Summary ---
				BigDecimal totalDue = orderTotalAmount;
				
				drawLabelValueLine(contentStream, fontBold, FONT_SIZE_FINANCIAL_LABEL, y, "TOTAL",
						CURRENCY_FORMAT.format(totalDue));
				y -= LINE_SPACING_REGULAR;

				// Use validCashTendered which ensures it's not null
				BigDecimal actualCashTendered = (validCashTendered.compareTo(totalDue) >= 0)
						? validCashTendered
						: totalDue;
				// If cash tendered < total due, for receipt display, show tendered as total due
				// to avoid
				// negative change on receipt
				// Actual business logic for handling insufficient payment is in CheckoutPanel

				BigDecimal change = actualCashTendered.subtract(totalDue);
				if (change.compareTo(BigDecimal.ZERO) < 0)
					change = BigDecimal.ZERO;

				drawLabelValueLine(contentStream, fontRegular, FONT_SIZE_FINANCIAL_LABEL, y, "CASH",
						CURRENCY_FORMAT.format(validCashTendered));
				y -= LINE_SPACING_REGULAR;
				drawLabelValueLine(contentStream, fontRegular, FONT_SIZE_FINANCIAL_LABEL, y, "CHANGE",
						CURRENCY_FORMAT.format(change));
				y -= LINE_SPACING_REGULAR;

				y -= LINE_SPACING_AFTER_TOTALS_GROUP;
				String dottedLine1 = getDynamicDottedLine(fontRegular, FONT_SIZE_FINANCIAL_LABEL);
				drawTextLeft(contentStream, fontRegular, FONT_SIZE_FINANCIAL_LABEL, SIDE_PADDING, y, dottedLine1);
				y -= LINE_SPACING_REGULAR;

				// --- VAT Breakdown ---
				BigDecimal vatDivisor = BigDecimal.ONE.add(VAT_RATE);
				BigDecimal netOfVat = totalDue.divide(vatDivisor, 2, RoundingMode.HALF_UP);
				BigDecimal vatPayable = totalDue.subtract(netOfVat);

				drawLabelValueLine(contentStream, fontRegular, FONT_SIZE_FINANCIAL_LABEL, y, "VAT Sales",
						CURRENCY_FORMAT.format(netOfVat));
				y -= LINE_SPACING_REGULAR;
				drawLabelValueLine(contentStream, fontRegular, FONT_SIZE_FINANCIAL_LABEL, y,
						"VAT (" + VAT_RATE.multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString() + "%)",
						CURRENCY_FORMAT.format(vatPayable));
				y -= LINE_SPACING_REGULAR;
				drawLabelValueLine(contentStream, fontRegular, FONT_SIZE_FINANCIAL_LABEL, y, "VAT Exempt Sales",
						CURRENCY_FORMAT.format(BigDecimal.ZERO));
				y -= LINE_SPACING_REGULAR;
				drawLabelValueLine(contentStream, fontRegular, FONT_SIZE_FINANCIAL_LABEL, y, "Zero Rated Sales",
						CURRENCY_FORMAT.format(BigDecimal.ZERO));
				y -= LINE_SPACING_REGULAR;

				y -= LINE_SPACING_AFTER_TOTALS_GROUP;
				drawTextLeft(contentStream, fontRegular, FONT_SIZE_FINANCIAL_LABEL, SIDE_PADDING, y, dottedLine1);
				y -= LINE_SPACING_REGULAR;

				// --- Transaction Details ---
				drawTextLeft(contentStream, fontRegular, FONT_SIZE_TRANSACTION_DETAILS, SIDE_PADDING, y,
						"TOTAL ITEM(S) : " + totalItemsCount);
				y -= LINE_SPACING_REGULAR;
				drawTextLeft(contentStream, fontRegular, FONT_SIZE_TRANSACTION_DETAILS, SIDE_PADDING, y,
						"INV. # : " + orderIdStr);
				y -= LINE_SPACING_REGULAR;
				drawTextLeft(contentStream, fontRegular, FONT_SIZE_TRANSACTION_DETAILS, SIDE_PADDING, y,
						"CASHIER : " + CASHIER_PLACEHOLDER);
				y -= LINE_SPACING_REGULAR;
				drawTextLeft(contentStream, fontRegular, FONT_SIZE_TRANSACTION_DETAILS, SIDE_PADDING, y,
						"TIME    : " + DATETIME_FORMAT_PDF.format(new Date()));
				y -= LINE_SPACING_REGULAR;
				y -= LINE_SPACING_SECTION_BREAK;

				// --- Customer Fill-in ---
				drawTextLeft(contentStream, fontRegular, FONT_SIZE_CUSTOMER_FILL_LABEL, SIDE_PADDING, y,
						"Name     : " + CUSTOMER_LINE_PLACEHOLDER);
				y -= LINE_SPACING_FOOTER_FILL;
				drawTextLeft(contentStream, fontRegular, FONT_SIZE_CUSTOMER_FILL_LABEL, SIDE_PADDING, y,
						"Address  : " + CUSTOMER_LINE_PLACEHOLDER);
				y -= LINE_SPACING_FOOTER_FILL;
				drawTextLeft(contentStream, fontRegular, FONT_SIZE_CUSTOMER_FILL_LABEL, SIDE_PADDING, y,
						"TIN No.  : " + CUSTOMER_LINE_PLACEHOLDER);
				y -= LINE_SPACING_FOOTER_FILL;
				y -= LINE_SPACING_SECTION_BREAK;

				// --- Footer Message ---
				drawTextCentered(contentStream, fontRegular, FONT_SIZE_FOOTER_MSG, y, "This serves as your invoice.");

				contentStream.close();
				document.save(fileToSave);
				JOptionPane.showMessageDialog(parentWindow, "Receipt saved successfully as PDF.", "PDF Saved",
						JOptionPane.INFORMATION_MESSAGE);

			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(parentWindow, "Error generating/saving PDF: " + e.getMessage(),
						"PDF I/O Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(parentWindow,
						"An unexpected error occurred during PDF generation: " + e.getMessage(), "PDF Generation Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
