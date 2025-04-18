package com.github.hanzm_10.murico.app.scenes.dashboard;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.Format;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import com.github.hanzm_10.murico.app.scenes.auth.LoginSystem;

public class MainDashboard {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                var window = new MainDashboard();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private JFrame frame;
    private JLayeredPane layeredPane;
    // private LoginSystem last;
    // private UserService userService;
    // private User user;
    // private Profile profile;

    private Format format;

    /**
     * Create the application.
     */
    public MainDashboard() {
        initialize();
        /*
         * this.userService = new UserService(); this.last = new LoginSystem(); this.profile = new
         * Profile(); this.format = new Format();
         */
        frame.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        /*
         * Profile pfp = new Profile(); var format = new Format();
         */
        var username = LoginSystem.username;

        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        var screenWidth = screenSize.width;
        var screenHeight = screenSize.height;
        frame.setSize(screenWidth, screenHeight);
        // frame.setBounds(100, 100, 451, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        var contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBounds(6, 0, 1680, 1060);
        frame.getContentPane().add(contentPane);
        // panel.setLayout(null);

        layeredPane = new JLayeredPane();
        layeredPane.setBackground(Color.WHITE);
        layeredPane.setBounds(293, 36, 1226, 887);
        layeredPane.setLayout(new CardLayout(0, 0));
        contentPane.setLayout(null);

        var optPnl = new JLabel("");
        optPnl.setIcon(
                new ImageIcon(MainDashboard.class.getResource("/imageSource/optionpanel.png")));
        optPnl.setBounds(167, 121, 114, 796);

        // button for option panel
        var checkoutBtn = new JButton("");
        checkoutBtn.setBorderPainted(false);
        checkoutBtn.setIcon(new ImageIcon(
                MainDashboard.class.getResource("/imageSource/orderCheckoutLogo.png")));
        checkoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkoutBtn.setBounds(187, 488, 76, 72);
        contentPane.add(checkoutBtn);

        var settingsBtn = new JButton("");
        settingsBtn.setBorderPainted(false);
        settingsBtn.setIcon(
                new ImageIcon(MainDashboard.class.getResource("/imageSource/settings.png")));
        settingsBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        settingsBtn.setBounds(187, 740, 76, 72);
        contentPane.add(settingsBtn);

        var strategyBtn = new JButton("");
        strategyBtn.setBorderPainted(false);
        strategyBtn.setIcon(
                new ImageIcon(MainDashboard.class.getResource("/imageSource/strategyboard.png")));
        strategyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        strategyBtn.setBounds(187, 404, 76, 72);
        contentPane.add(strategyBtn);



        var profileBtn = new JButton("");
        profileBtn.setBorderPainted(false);
        profileBtn.setIcon(
                new ImageIcon(MainDashboard.class.getResource("/imageSource/profilelogo.png")));
        profileBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        profileBtn.setBounds(187, 152, 76, 72);
        contentPane.add(profileBtn);

        var HomeBtn = new JButton("");
        HomeBtn.setBorderPainted(false);
        HomeBtn.setIcon(
                new ImageIcon(MainDashboard.class.getResource("/imageSource/home-page.png")));
        HomeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        HomeBtn.setBounds(187, 236, 76, 72);
        contentPane.add(HomeBtn);

        var MarketingBtn = new JButton("");
        MarketingBtn.setBorderPainted(false);
        MarketingBtn.setIcon(
                new ImageIcon(MainDashboard.class.getResource("/imageSource/marketing.png")));
        MarketingBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        MarketingBtn.setBounds(187, 320, 76, 72);
        contentPane.add(MarketingBtn);

        var shutdownBtn = new JButton("");
        shutdownBtn.setBorderPainted(false);
        shutdownBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        shutdownBtn.setBounds(187, 824, 76, 72);
        contentPane.add(shutdownBtn);
        shutdownBtn.setIcon(
                new ImageIcon(MainDashboard.class.getResource("/imageSource/shutdown.png")));


        // Home Panel
        var MainPanel1 = new JPanel();
        layeredPane.add(MainPanel1, "name_1092668438958");
        MainPanel1.setBackground(new Color(253, 253, 253));
        // MainPanel1.setPreferredSize(screenSize);
        MainPanel1.setLayout(null);


        var backBtnMain1 = new JButton("");
        backBtnMain1.setBorder(null);
        backBtnMain1.setBounds(192, 848, 43, 42);
        MainPanel1.add(backBtnMain1);

        var mainLabel = new JLabel("Main dashboard");
        // mainLabel.setIcon(new
        // ImageIcon(MainDashboard.class.getResource("/imageSource/main#1.png")));

        mainLabel.setBounds(120, 268, 1085, 315);
        MainPanel1.add(mainLabel);

        // Profile Panel
        var profilePnl = new JPanel();
        profilePnl.setBackground(Color.WHITE);
        // profilePnl.setPreferredSize(screenSize);
        profilePnl.setLayout(null);
        layeredPane.add(profilePnl, "name_265538866079209");

        var displayRole = new JTextPane();
        displayRole.setForeground(Color.WHITE);
        // displayRole.setText(pfp.getRole(username));
        displayRole.setFont(new Font("Montserrat", Font.BOLD, 20));
        displayRole.setBounds(510, 439, 233, 30);
        displayRole.setEditable(false);
        displayRole.setOpaque(false);
        // format.centerTextPane(displayRole);
        profilePnl.add(displayRole);

        var role = new JLabel("");
        role.setIcon(new ImageIcon(
                MainDashboard.class.getResource("/imageSource/roleBoarderLabel.png")));
        role.setAlignmentX(Component.CENTER_ALIGNMENT);
        role.setBounds(497, 425, 274, 56);
        profilePnl.add(role);

        var profilepic = new JLabel("");
        profilepic.setBackground(new Color(33, 64, 107));
        profilepic.setIcon(
                new ImageIcon(MainDashboard.class.getResource("/imageSource/profilepic.png")));
        profilepic.setBounds(497, 64, 233, 229);
        profilePnl.add(profilepic);

        var displayname = new JTextPane();
        displayname.setAlignmentX(Component.RIGHT_ALIGNMENT);
        displayname.setForeground(Color.WHITE);
        displayname.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        displayname.setText(username.toUpperCase());
        displayname.setFont(new Font("Montserrat", Font.BOLD, 64));
        displayname.setOpaque(false);
        displayname.setEditable(false);
        displayname.setBounds(318, 305, 616, 84);
        // format.centerTextPane(displayname);
        profilePnl.add(displayname);



        var profileLbl = new JLabel("");
        profileLbl.setIcon(new ImageIcon(
                MainDashboard.class.getResource("/imageSource/profileRectangle.png")));
        profileLbl.setBounds(29, 202, 1180, 679);
        profilePnl.add(profileLbl);
        // profilePnl.setLayout(null);

        var editProfBtn = new JButton("");
        editProfBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editProfBtn.setBorderPainted(false);
        editProfBtn.setIcon(
                new ImageIcon(MainDashboard.class.getResource("/imageSource/editProf.png")));
        editProfBtn.setBounds(838, 95, 371, 62);
        profilePnl.add(editProfBtn);

        var ProfileTxt = new JLabel();
        // ProfileTxt.setEditable(false);
        ProfileTxt.setFont(new Font("Montserrat", Font.BOLD | Font.ITALIC, 64));
        ProfileTxt.setText("Profile");
        ProfileTxt.setBounds(897, 24, 233, 71);
        ProfileTxt.setForeground(new Color(72, 124, 141));
        profilePnl.add(ProfileTxt);

        // edit Profile panel
        var editProfPnl = new JPanel();
        editProfPnl.setBackground(Color.WHITE);
        editProfPnl.setLayout(null);
        layeredPane.add(editProfPnl, "edit Profile Panel");


        var pfpName = new JTextPane();
        pfpName.setAlignmentX(Component.RIGHT_ALIGNMENT);
        pfpName.setForeground(Color.WHITE);
        pfpName.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        pfpName.setText(username.toUpperCase());
        pfpName.setFont(new Font("Montserrat", Font.BOLD, 64));
        pfpName.setOpaque(false);
        pfpName.setEditable(false);
        pfpName.setBounds(318, 305, 616, 84);
        // format.centerTextPane(pfpName);
        editProfPnl.add(pfpName);


        var model = new DefaultComboBoxModel<String>();
        model.addElement("Select a role");
        for (String r : new String[] {"Admin", "Supplier", "Manager", "Clerk", "Employee",
                "Trainee", "To be assigned"}) {
            model.addElement(r);
        }

        // 2) Create your combo from that model:
        var combo = new JComboBox<>(model);
        // pfp.editRole(combo);
        editProfPnl.add(combo);

        var btnSave = new JButton("Save");
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        btnSave.setBounds(627, 677, 100, 40);
        editProfPnl.add(btnSave);

        var cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelBtn.setBounds(493, 677, 100, 40);
        editProfPnl.add(cancelBtn);

        var profilepic_1 = new JLabel("");
        profilepic_1.setBounds(497, 64, 233, 229);
        profilepic_1.setIcon(
                new ImageIcon(MainDashboard.class.getResource("/imageSource/profilepic.png")));
        profilepic_1.setBackground(new Color(33, 64, 107));
        editProfPnl.add(profilepic_1);

        var profileLbl_1 = new JLabel("");
        profileLbl_1.setBounds(29, 202, 1180, 679);
        profileLbl_1.setIcon(new ImageIcon(
                MainDashboard.class.getResource("/imageSource/profileRectangle.png")));
        editProfPnl.add(profileLbl_1);

        var ProfileTxt_1 = new JLabel();
        // ProfileTxt_1.setEditable(false);
        ProfileTxt_1.setBounds(897, 24, 229, 79);
        ProfileTxt_1.setText("Profile");
        ProfileTxt_1.setForeground(new Color(40, 62, 104));
        ProfileTxt_1.setFont(new Font("Montserrat", Font.BOLD | Font.ITALIC, 64));
        editProfPnl.add(ProfileTxt_1);



        // Marketing Panel
        var marketingPnl = new JPanel();
        // marketingPnl.setPreferredSize(screenSize);
        layeredPane.add(marketingPnl, "marketing panel");

        var marketingLbl = new JLabel("marketing panel");
        marketingLbl.setBounds(120, 268, 1201, 315);
        marketingPnl.add(marketingLbl);

        // Strategy Panel
        var strategyPnl = new JPanel();
        // strategyPnl.setPreferredSize(screenSize);
        strategyPnl.setLayout(null);
        layeredPane.add(strategyPnl, "name_265538908912125");

        var strategyLbl = new JLabel("strategy panel");
        strategyLbl.setBounds(120, 268, 1201, 315);
        strategyPnl.add(strategyLbl);

        // Checkout Panel
        var checkoutPnl = new JPanel();
        checkoutPnl.setLayout(null);
        // checkoutPnl.setPreferredSize(screenSize);
        layeredPane.add(checkoutPnl, "name_265538927088625");

        var checkoutLbl = new JLabel("checkout panel");
        checkoutLbl.setBounds(120, 268, 1201, 315);
        checkoutPnl.add(checkoutLbl);

        // Settings Panel
        var settingsPnl = new JPanel();
        settingsPnl.setLayout(null);
        // settingsPnl.setPreferredSize(screenSize);
        layeredPane.add(settingsPnl, "name_265538942785167");

        var settingsLbl = new JLabel("settings panel");
        settingsLbl.setBounds(120, 268, 1201, 315);
        settingsPnl.add(settingsLbl);



        // action listener
        shutdownBtn.addActionListener(e -> new LoginSystem());

        /*
         * MarketingBtn.addActionListener(e -> last.buttons(marketingPnl, layeredPane));
         * 
         * profileBtn.addActionListener(e -> last.buttons(profilePnl, layeredPane));
         * 
         * HomeBtn.addActionListener(e -> last.buttons(MainPanel1, layeredPane));
         * 
         * settingsBtn.addActionListener(e -> last.buttons(settingsPnl, layeredPane));
         * 
         * strategyBtn.addActionListener(e -> last.buttons(strategyPnl, layeredPane));
         * 
         * checkoutBtn.addActionListener(e -> last.buttons(checkoutPnl, layeredPane));
         */

        btnSave.addActionListener(e -> {
            var displayName = LoginSystem.username;
            var newRole = (String) combo.getSelectedItem();
            /*
             * Integer userId = pfp.getUserIdByDisplayName(displayName); if (userId != null) {
             * pfp.selectRole(userId, newRole); JOptionPane.showMessageDialog(editProfPnl,
             * "Role updated to “" + newRole + "”"); // last.buttons(profilePnl, layeredPane); }
             * else { JOptionPane.showMessageDialog(editProfPnl, "User not found: " + displayName);
             * }
             */
        });

        // cancelBtn.addActionListener(e -> last.buttons(profilePnl, layeredPane));

        editProfBtn.addActionListener(e -> {
            // boolean isAdmin = pfp.isAdmin(username);
            /*
             * if (isAdmin) { last.buttons(editProfPnl, layeredPane); } else {
             * System.out.print("error"); }
             */
        });



        var logolbl = new JLabel("");
        logolbl.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/logo.png")));
        logolbl.setBounds(167, 36, 114, 72);
        contentPane.add(logolbl);


        contentPane.add(optPnl);
        contentPane.add(layeredPane);



    }
}
