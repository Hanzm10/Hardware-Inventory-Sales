package Trylang;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Trylang.LoginSystem;
import Trylang.UserService;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.Component;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;

public class MainDashboard {

	private JFrame frame;
	private JLayeredPane layeredPane;
	private LoginSystem last;
	private UserService userService;
	private User user;
	private Profile profile;
	private Format format;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainDashboard window = new MainDashboard();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainDashboard() {
		initialize();
		this.userService = new UserService();
		this.last = new LoginSystem();
		this.profile = new Profile();
		this.format = new Format();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		Profile pfp = new Profile();
		Format format = new Format();
		String username = LoginSystem.username;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		frame.setSize(screenWidth, screenHeight);
		//frame.setBounds(100, 100, 451, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBounds(6, 0, 1680, 1060);
		frame.getContentPane().add(contentPane);
		//panel.setLayout(null);
		
		layeredPane = new JLayeredPane();
		layeredPane.setBackground(Color.WHITE);
		layeredPane.setBounds(293, 36, 1226, 887);
		layeredPane.setLayout(new CardLayout(0, 0));
		contentPane.setLayout(null);
		
		JLabel optPnl = new JLabel("");
		optPnl.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/optionpanel.png")));
		optPnl.setBounds(167, 121, 114, 796);
		
		//button for option panel
		JButton checkoutBtn = new JButton("");
		checkoutBtn.setBorderPainted(false);
		checkoutBtn.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/orderCheckoutLogo.png")));
		checkoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		checkoutBtn.setBounds(187, 488, 76, 72);
		contentPane.add(checkoutBtn);
		
		JButton settingsBtn = new JButton("");
		settingsBtn.setBorderPainted(false);
		settingsBtn.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/settings.png")));
		settingsBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		settingsBtn.setBounds(187, 740, 76, 72);
		contentPane.add(settingsBtn);
		
		JButton strategyBtn = new JButton("");
		strategyBtn.setBorderPainted(false);
		strategyBtn.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/strategyboard.png")));
		strategyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		strategyBtn.setBounds(187, 404, 76, 72);
		contentPane.add(strategyBtn);
	
		
		
		JButton profileBtn = new JButton("");
		profileBtn.setBorderPainted(false);
		profileBtn.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/profilelogo.png")));
		profileBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		profileBtn.setBounds(187, 152, 76, 72);
		contentPane.add(profileBtn);
		
		JButton HomeBtn = new JButton("");
		HomeBtn.setBorderPainted(false);
		HomeBtn.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/home-page.png")));
		HomeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		HomeBtn.setBounds(187, 236, 76, 72);
		contentPane.add(HomeBtn);
		
		JButton MarketingBtn = new JButton("");
		MarketingBtn.setBorderPainted(false);
		MarketingBtn.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/marketing.png")));
		MarketingBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		MarketingBtn.setBounds(187, 320, 76, 72);
		contentPane.add(MarketingBtn);
		
		JButton shutdownBtn = new JButton("");
		shutdownBtn.setBorderPainted(false);
		shutdownBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		shutdownBtn.setBounds(187, 824, 76, 72);
		contentPane.add(shutdownBtn);
		shutdownBtn.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/shutdown.png")));
		
		
		//Home Panel
		JPanel MainPanel1 = new JPanel();
		layeredPane.add(MainPanel1, "name_1092668438958");
		MainPanel1.setBackground(new Color(253, 253, 253));
		//MainPanel1.setPreferredSize(screenSize);
		MainPanel1.setLayout(null);
		
		
		JButton backBtnMain1 = new JButton("");
		backBtnMain1.setBorder(null);
		backBtnMain1.setBounds(192, 848, 43, 42);
		MainPanel1.add(backBtnMain1);
		
		JLabel mainLabel = new JLabel("Main dashboard");
		//mainLabel.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/main#1.png")));
		
		mainLabel.setBounds(120, 268, 1085, 315);
		MainPanel1.add(mainLabel);
		
		//Profile Panel
		JPanel profilePnl = new JPanel();
		profilePnl.setBackground(Color.WHITE);
		//profilePnl.setPreferredSize(screenSize);
		profilePnl.setLayout(null);
		layeredPane.add(profilePnl, "name_265538866079209");
		
		JTextPane displayRole = new JTextPane();
		displayRole.setForeground(Color.WHITE);
		displayRole.setText(pfp.getRole(username));
		displayRole.setFont(new Font("Montserrat", Font.BOLD, 20));
		displayRole.setBounds(510, 439, 233, 30);
		displayRole.setEditable(false);
		displayRole.setOpaque(false);
		format.centerTextPane(displayRole);
		profilePnl.add(displayRole);
		
		JLabel role = new JLabel("");
		role.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/roleBoarderLabel.png")));
		role.setAlignmentX(Component.CENTER_ALIGNMENT);
		role.setBounds(497, 425, 274, 56);
		profilePnl.add(role);
		
		JLabel profilepic = new JLabel("");
		profilepic.setBackground(new Color(33, 64, 107));
		profilepic.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/profilepic.png")));
		profilepic.setBounds(497, 64, 233, 229);
		profilePnl.add(profilepic);
		
		JTextPane displayname = new JTextPane();
		displayname.setAlignmentX(Component.RIGHT_ALIGNMENT);
		displayname.setForeground(Color.WHITE);
		displayname.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		displayname.setText(username.toUpperCase());
		displayname.setFont(new Font("Montserrat", Font.BOLD, 64));
		displayname.setOpaque(false);
		displayname.setEditable(false);
		displayname.setBounds(318, 305, 616, 84);
		format.centerTextPane(displayname);
		profilePnl.add(displayname);
	
		
		
		JLabel profileLbl = new JLabel("");
		profileLbl.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/profileRectangle.png")));
		profileLbl.setBounds(29, 202, 1180, 679);
		profilePnl.add(profileLbl);
		//profilePnl.setLayout(null);
		
		JButton editProfBtn = new JButton("");
		editProfBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		editProfBtn.setBorderPainted(false);
		editProfBtn.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/editProf.png")));
		editProfBtn.setBounds(838, 95, 371, 62);
		profilePnl.add(editProfBtn);
		
		JLabel ProfileTxt = new JLabel();
		//ProfileTxt.setEditable(false);
		ProfileTxt.setFont(new Font("Montserrat", Font.BOLD | Font.ITALIC, 64));
		ProfileTxt.setText("Profile");
		ProfileTxt.setBounds(897, 24, 233, 71);
		ProfileTxt.setForeground(new Color(72, 124, 141));
		profilePnl.add(ProfileTxt);
		
		//edit Profile panel
		JPanel editProfPnl = new JPanel();
		editProfPnl.setBackground(Color.WHITE);
		editProfPnl.setLayout(null);
		layeredPane.add(editProfPnl, "edit Profile Panel");

		
		JTextPane pfpName = new JTextPane();
		pfpName.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pfpName.setForeground(Color.WHITE);
		pfpName.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		pfpName.setText(username.toUpperCase());
		pfpName.setFont(new Font("Montserrat", Font.BOLD, 64));
		pfpName.setOpaque(false);
		pfpName.setEditable(false);
		pfpName.setBounds(318, 305, 616, 84);
		format.centerTextPane(pfpName);
		editProfPnl.add(pfpName);
		
		
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Select a role");          
        for (String r : new String[]{
            "Admin", "Supplier", "Manager",
            "Clerk", "Employee", "Trainee", "To be assigned"
        }) {
            model.addElement(r);
        }

        // 2) Create your combo from that model:
        JComboBox<String> combo = new JComboBox<>(model);
        pfp.editRole(combo);
		editProfPnl.add(combo);
		
		JButton btnSave = new JButton("Save");
		btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		btnSave.setBounds(627, 677, 100, 40);
		editProfPnl.add(btnSave);
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setBackground(Color.WHITE);
		cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cancelBtn.setBounds(493, 677, 100, 40);
		editProfPnl.add(cancelBtn);
		
		JLabel profilepic_1 = new JLabel("");
		profilepic_1.setBounds(497, 64, 233, 229);
		profilepic_1.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/profilepic.png")));
		profilepic_1.setBackground(new Color(33, 64, 107));
		editProfPnl.add(profilepic_1);
		
		JLabel profileLbl_1 = new JLabel("");
		profileLbl_1.setBounds(29, 202, 1180, 679);
		profileLbl_1.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/profileRectangle.png")));
		editProfPnl.add(profileLbl_1);
		
		JLabel ProfileTxt_1 = new JLabel();
		//ProfileTxt_1.setEditable(false);
		ProfileTxt_1.setBounds(897, 24, 229, 79);
		ProfileTxt_1.setText("Profile");
		ProfileTxt_1.setForeground(new Color(40, 62, 104));
		ProfileTxt_1.setFont(new Font("Montserrat", Font.BOLD | Font.ITALIC, 64));
		editProfPnl.add(ProfileTxt_1);
		
		

		
		//Marketing Panel
		JPanel marketingPnl = new JPanel();
		//marketingPnl.setPreferredSize(screenSize);
		layeredPane.add(marketingPnl, "marketing panel");
		
		JLabel marketingLbl = new JLabel("marketing panel");
		marketingLbl.setBounds(120, 268, 1201, 315);
		marketingPnl.add(marketingLbl);
		
		//Strategy Panel
		JPanel strategyPnl = new JPanel();
		//strategyPnl.setPreferredSize(screenSize);
		strategyPnl.setLayout(null);
		layeredPane.add(strategyPnl, "name_265538908912125");
		
		JLabel strategyLbl = new JLabel("strategy panel");
		strategyLbl.setBounds(120, 268, 1201, 315);
		strategyPnl.add(strategyLbl);
		
		//Checkout Panel
		JPanel checkoutPnl = new JPanel();
		checkoutPnl.setLayout(null);
		//checkoutPnl.setPreferredSize(screenSize);
		layeredPane.add(checkoutPnl, "name_265538927088625");
		
		JLabel checkoutLbl = new JLabel("checkout panel");
		checkoutLbl.setBounds(120, 268, 1201, 315);
		checkoutPnl.add(checkoutLbl);
		
		//Settings Panel
		JPanel settingsPnl = new JPanel();
		settingsPnl.setLayout(null);
		//settingsPnl.setPreferredSize(screenSize);
		layeredPane.add(settingsPnl, "name_265538942785167");
		
		JLabel settingsLbl = new JLabel("settings panel");
		settingsLbl.setBounds(120, 268, 1201, 315);
		settingsPnl.add(settingsLbl);
		
		
		
		
		
		//action listener
		shutdownBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoginSystem();
				
			}
		});
		
		MarketingBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				last.buttons(marketingPnl, layeredPane);

			}
		});
		
		profileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				last.buttons(profilePnl, layeredPane);
				

			}
		});
		
		HomeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				last.buttons(MainPanel1, layeredPane);
				

			}
		});
		
		settingsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				last.buttons(settingsPnl, layeredPane);
				

			}
		});
		
		strategyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				last.buttons(strategyPnl, layeredPane);
			

			}
		});
		
		checkoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				last.buttons(checkoutPnl, layeredPane);

			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String displayName = LoginSystem.username;
				String newRole = (String) combo.getSelectedItem();
			    Integer userId = pfp.getUserIdByDisplayName(displayName);
			    if (userId != null) {
			        pfp.selectRole(userId, newRole);
			        JOptionPane.showMessageDialog(editProfPnl, "Role updated to “" + newRole + "”");
			        last.buttons(profilePnl, layeredPane);
			    } else {
			        JOptionPane.showMessageDialog(editProfPnl, "User not found: " + displayName);
			    }
			}
		});
		
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				last.buttons(profilePnl, layeredPane);

			}
		});
		
		editProfBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isAdmin = pfp.isAdmin(username);
				if(isAdmin) {
				last.buttons(editProfPnl, layeredPane);
				}else {
					System.out.print("error");
				}
			}
		});
		
		
		
		JLabel logolbl = new JLabel("");
		logolbl.setIcon(new ImageIcon(MainDashboard.class.getResource("/imageSource/logo.png")));
		logolbl.setBounds(167, 36, 114, 72);
		contentPane.add(logolbl);
		
		
		contentPane.add(optPnl);
		contentPane.add(layeredPane);
		
				
		
	}
}
