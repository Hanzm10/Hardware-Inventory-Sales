import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor; // Import for cursor
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image; // Potentially needed for scaling (optional)


import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import java.net.URL; // Required for loading resources

public class MainFrame {

    private JFrame frame;
    private InventoryPanel inventoryPanel; // Panel for the inventory view
    // Add fields for other view panels here later (e.g., DashboardPanel, OrdersPanel)
    private JPanel currentViewPanel; // <-- ADD THIS FIELD


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> {
            try {
                MainFrame window = new MainFrame();
                window.frame.setTitle("Murico Application"); // More general title
                window.frame.setLocationRelativeTo(null);
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Failed to launch application: " + e.getMessage(),
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Create the application.
     */
    public MainFrame() {
        initialize();
        // Set the initial view (e.g., show inventory panel)
        showInventoryView();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setSize(1215, 787);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0)); // Frame uses BorderLayout

        // --- Create the Left Column Container ---
        JPanel leftColumnPanel = new JPanel(new BorderLayout(0, 0)); // Vertical gap between logo area and sidebar
        leftColumnPanel.setOpaque(false);
        // Overall margins
        leftColumnPanel.setBorder(new EmptyBorder(10, 10, 10, 5)); // top, left, bottom, right

        // --- Create Logo Container Panel with GridBagLayout ---
        JPanel logoContainerPanel = new JPanel(new GridBagLayout());
        logoContainerPanel.setOpaque(false); // Make it transparent
        // Define the total height for the logo area if needed
        // logoContainerPanel.setPreferredSize(new Dimension(100, 100)); // Example explicit size

        // --- Create and Add Logo to NORTH ---
        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        // Optional: Add padding specific to the logo within the NORTH area
        lblLogo.setBorder(new EmptyBorder(5, 0, 5, 0)); // e.g., 5px top, 10px bottom padding for logo
        try {
            URL logoUrl = getClass().getResource("/ImgSrc/blue_transparent_logo.png");
            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                // Scale logo (adjust desiredWidth)
                int desiredWidth = 80; // Adjust logo size as needed
                Image scaledImage = logoIcon.getImage().getScaledInstance(desiredWidth, -1, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(scaledImage));
            } else { /* Error handling */ lblLogo.setText("M"); }
        } catch (Exception e) { /* Error handling */ lblLogo.setText("M"); e.printStackTrace();}
        
        GridBagConstraints gbcLogo = new GridBagConstraints();
        // No need to set gridx/gridy, defaults to 0,0
        // anchor defaults to CENTER which handles vertical/horizontal alignment within the cell
        // gbcLogo.anchor = GridBagConstraints.CENTER; // Explicitly set if needed
        logoContainerPanel.add(lblLogo, gbcLogo);

        leftColumnPanel.add(lblLogo, BorderLayout.NORTH); // Add logo to the top


        // --- Create and Add Sidebar Panel to CENTER ---
        // createSidebarPanel now just returns the GradientRoundedPanel with its internal layout
        JPanel gradientSidebarPanel = createSidebarPanel();
        leftColumnPanel.add(gradientSidebarPanel, BorderLayout.CENTER); // Add sidebar below logo


        // --- Add the combined Left Column to the Frame ---
        frame.getContentPane().add(leftColumnPanel, BorderLayout.WEST);


        // --- Initialize other panels ---
        inventoryPanel = new InventoryPanel(frame);
        // ...

        // --- Set initial view ---
        showInventoryView();

    } // --- End of initialize() ---


    /**
     * Creates the sidebar panel containing the buttons.
     * Returns the GradientRoundedPanel itself.
     * @return The configured GradientRoundedPanel for the sidebar content.
     */
    private JPanel createSidebarPanel() {
        // Define Colors and Radius for the gradient panel
        Color startColor = new Color(70, 150, 160);
        Color endColor = new Color(20, 70, 130);
        int cornerRadius = 25;

        // Setup Layout Manager for the buttons INSIDE the gradient panel
        GridBagLayout gbl_sidebarPanel = new GridBagLayout();
        gbl_sidebarPanel.columnWidths = new int[]{0, 0};
        // Adjust rows for buttons + spacer
        gbl_sidebarPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_sidebarPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        // Adjust weights for spacer
        gbl_sidebarPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};

        // Create the Gradient Panel
        GradientRoundedPanel sidebarContentPanel = new GradientRoundedPanel(gbl_sidebarPanel, startColor, endColor, cornerRadius);
        // Set INNER padding for the content (buttons) within the gradient panel
        sidebarContentPanel.setBorder(new EmptyBorder(20, 5, 15, 5)); // e.g., More top padding inside gradient
        // Set preferred width for the gradient panel itself
        sidebarContentPanel.setPreferredSize(new Dimension(90, 0)); // Adjust width

        // GridBagConstraints for adding buttons to the gradient panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0); // Spacing between buttons
        int gridYCounter = 0; // Start button gridy from 0 within this panel

        // --- Add Buttons to the gradient panel ---
        JButton btnProfile = createSidebarButton("Profile", "/ImgSrc/profile_icon.png");
        btnProfile.addActionListener(e -> showPlaceholderView("Profile"));
        gbc.gridy = gridYCounter++;
        sidebarContentPanel.add(btnProfile, gbc);

        JButton btnDashboard = createSidebarButton("Dashboard", "/ImgSrc/dashboard_icon.png");
        btnDashboard.addActionListener(e -> showPlaceholderView("Dashboard"));
        gbc.gridy = gridYCounter++;
        sidebarContentPanel.add(btnDashboard, gbc);

        JButton btnReports = createSidebarButton("Reports", "/ImgSrc/reports_icon.png");
        btnReports.addActionListener(e -> showPlaceholderView("Reports"));
        gbc.gridy = gridYCounter++;
        sidebarContentPanel.add(btnReports, gbc);

        JButton btnOrder = createSidebarButton("Orders", "/ImgSrc/orders_icon.png");
        btnOrder.addActionListener(e -> showPlaceholderView("Orders"));
        gbc.gridy = gridYCounter++;
        sidebarContentPanel.add(btnOrder, gbc);

        JButton btnInventory = createSidebarButton("Inventory", "/ImgSrc/inventory_icon.png");
        btnInventory.addActionListener(e -> showInventoryView());
        gbc.gridy = gridYCounter++;
        sidebarContentPanel.add(btnInventory, gbc);

        JButton btnContacts = createSidebarButton("Contacts", "/ImgSrc/contacts_icon.png");
        btnContacts.addActionListener(e -> showPlaceholderView("Contacts"));
        gbc.gridy = gridYCounter++;
        sidebarContentPanel.add(btnContacts, gbc);

        // Spacer within the gradient panel
        gbc.gridy = gridYCounter++; // This is the row with weight 1.0
        sidebarContentPanel.add(Box.createVerticalGlue(), gbc);

        // Settings Button
        JButton btnSettings = createSidebarButton("Settings", "/ImgSrc/settings_icon.png");
        btnSettings.addActionListener(e -> showPlaceholderView("Settings"));
        gbc.gridy = gridYCounter++;
        sidebarContentPanel.add(btnSettings, gbc);

        // Shutdown Button
        JButton btnShutdown = createSidebarButton("Shutdown", "/ImgSrc/shutdown_icon.png");
        btnShutdown.addActionListener(e -> { /* Shutdown logic */
             int confirm = JOptionPane.showConfirmDialog(frame, "Exit?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) { System.exit(0); }
        });
        gbc.gridy = gridYCounter++;
        gbc.insets = new Insets(8, 0, 0, 0); // No bottom inset for last item
        sidebarContentPanel.add(btnShutdown, gbc);

        // Return the gradient panel containing the buttons
        return sidebarContentPanel;
    }


    // Helper method to create styled sidebar buttons
    private JButton createSidebarButton(String tooltipText, String iconPath) {
        JButton button = new JButton();
        // Removed setting font here as text isn't shown

        boolean isIconButton = iconPath != null && !iconPath.trim().isEmpty();

        if (isIconButton) {
            // --- Configure ICON button ---
            try {
                URL iconUrl = getClass().getResource(iconPath);
                if (iconUrl != null) {
                    ImageIcon icon = new ImageIcon(iconUrl);
                    // Optional: Scale icon if needed
                    int iconSize = 32; // Adjust desired icon size
                    Image img = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(img));
                } else { /* Error handling */ button.setText("?");}
            } catch (Exception e) { /* Error handling */ button.setText("?"); e.printStackTrace();}

            button.setToolTipText(tooltipText);
            button.setText(null);

            // --- Style for gradient background ---
            button.setBorderPainted(false);
            button.setContentAreaFilled(false); // Make button area transparent
            button.setFocusPainted(false);
            button.setOpaque(false);          // Ensure button itself is not opaque
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setPreferredSize(new Dimension(50, 50)); // Adjust size for icons
            button.setMinimumSize(new Dimension(50, 50));
            button.setHorizontalAlignment(SwingConstants.CENTER);

        } else {
             // Fallback for text button if needed, though not used currently for sidebar
            button.setText(tooltipText);
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setMargin(new Insets(2, 10, 2, 10));
        }

        return button;
    }
    


    // --- Methods to Switch Views ---

    /** Removes current view and shows the InventoryPanel */
    private void showInventoryView() {
        System.out.println("Switching to Inventory View");
        // Ensure inventoryPanel is initialized
        if (inventoryPanel == null) {
             inventoryPanel = new InventoryPanel(frame);
        }
        // Consider refreshing data when switching back to the view
        // inventoryPanel.refreshTable(); // Uncomment if refresh on view switch is desired

        switchView(inventoryPanel);
    }

    /** Shows a placeholder message for unimplemented views */
    private void showPlaceholderView(String viewName) {
         System.out.println("Switching to Placeholder View: " + viewName);
         JPanel placeholderPanel = new JPanel(new BorderLayout());
         JLabel label = new JLabel(viewName + " View - Not Implemented Yet", SwingConstants.CENTER);
         label.setFont(new Font("Montserrat SemiBold", Font.BOLD, 24));
         placeholderPanel.add(label, BorderLayout.CENTER);
         switchView(placeholderPanel);
    }

    /** Helper method to switch the central panel */
    private void switchView(JPanel newView) {
        // 1. Remove the previous view IF it exists
        if (currentViewPanel != null) {
            frame.getContentPane().remove(currentViewPanel);
        }

        // 2. Add the new view to the center
        frame.getContentPane().add(newView, BorderLayout.CENTER);
        currentViewPanel = newView; // 3. Update the reference to the current view

        // 4. Refresh the layout and repaint
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        System.out.println("Switched view to: " + newView.getClass().getSimpleName()); // Optional: Log view switch
    }

} // End of MainFrame class