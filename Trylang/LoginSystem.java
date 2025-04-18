package Trylang;
import Trylang.User;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.CardLayout;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Cursor;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import Trylang.UserService;

public class LoginSystem {

	public static String username = null;
	private JFrame frame;
	private JLayeredPane layeredPane;
	private JTextField usnlogin;
	private JPasswordField passFieldLogin;
	private UserService userService;
	private User user;
	private JTextField usernameS;
	private JTextField emailS;
	private JPasswordField passwordS;
	private Register register;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginSystem window = new LoginSystem();
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
	public LoginSystem() {
		initialize();
		this.userService = new UserService();
		this.register = new Register();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		frame.setSize(screenWidth, screenHeight);
		//frame.setBounds(100, 100, 451, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);
		layeredPane.setLayout(new CardLayout(0, 0));
		//Panel for home
		JPanel Home = new JPanel();
		Home.setBackground(Color.WHITE);
		Home.setPreferredSize(screenSize);
		layeredPane.add(Home, "home");
		
		ImageIcon homeIcon = new ImageIcon(LoginSystem.class.getResource("/imageSource/Home.png"));
		Home.setLayout(null);
		
		JButton loginBtn = new JButton("");
		loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginBtn.setBorder(null);
		loginBtn.setBounds(474, 674, 306, 50);
		
		JButton signinBtnHome = new JButton("");
		signinBtnHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		signinBtnHome.setBorder(null);
		signinBtnHome.setBounds(898, 674, 306, 50);
		Home.add(signinBtnHome);
		Home.add(loginBtn);
		
		JLabel homeLab = new JLabel("");
		homeLab.setBounds(120, -1, 1440, 1024);
		homeLab.setIcon(homeIcon);
		Home.add(homeLab);
		
		//Panel for SignIn
		JPanel signIn = new JPanel();
		signIn.setBackground(new Color(255, 255, 255));
		signIn.setPreferredSize(screenSize);
		layeredPane.add(signIn, "signin");
		signIn.setLayout(null);
		
		JTextPane txtrShow = new JTextPane();
		txtrShow.setText("show");
		txtrShow.setFont(new Font("Montserrat", Font.PLAIN, 10));
		txtrShow.setForeground(Color.LIGHT_GRAY);
		txtrShow.setBounds(583, 460, 29, 16);
		signIn.add(txtrShow);
		
		JCheckBox showPassChb = new JCheckBox("");
		showPassChb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		showPassChb.setBounds(583, 475, 29, 23);
		signIn.add(showPassChb);
		
		JButton backBtnS_1 = new JButton("");
		backBtnS_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		backBtnS_1.setRequestFocusEnabled(false);
		backBtnS_1.setBorderPainted(false);
		backBtnS_1.setBorder(null);
		backBtnS_1.setBounds(519, 81, 117, 29);
		signIn.add(backBtnS_1);
		
		JButton signupBtnM = new JButton("");
		signupBtnM.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		signupBtnM.setBorder(null);
		signupBtnM.setBounds(204, 723, 414, 62);
		signIn.add(signupBtnM);
		
		JButton loginBtnM = new JButton("");
		loginBtnM.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginBtnM.setBorder(null);
		loginBtnM.setBounds(204, 540, 409, 62);
		signIn.add(loginBtnM);
		
		usnlogin = new JTextField("Username");
		usnlogin.setForeground(Color.LIGHT_GRAY);
		usnlogin.setFont(new Font("Montserrat", Font.PLAIN, 24));
		usnlogin.setBorder(null);
		usnlogin.setBounds(214, 364, 388, 62);
		signIn.add(usnlogin);
		usnlogin.setColumns(10);
		
		passFieldLogin = new JPasswordField("Password");
		passFieldLogin.setEchoChar((char)0);
		passFieldLogin.setForeground(Color.LIGHT_GRAY);
		passFieldLogin.setFont(new Font("Montserrat", Font.PLAIN, 24));
		passFieldLogin.setBorder(null);
		passFieldLogin.setBounds(214, 455, 388, 56);
		signIn.add(passFieldLogin);
		
		//ImageIcon signInIcon = new ImageIcon(Last.class.getResource("/imgSrc/signIn.png"));
		
		JLabel signInLab = new JLabel("");
		signInLab.setBounds(66, 0, 1547, 1050);
		signInLab.setIcon(new ImageIcon(LoginSystem.class.getResource("/imageSource/login.png")));
		signIn.add(signInLab);
		
		ImageIcon signUpIcon = new ImageIcon(LoginSystem.class.getResource("/imageSource/create.png"));
		
		JPanel signUp = new JPanel();
		signUp.setBackground(new Color(33, 64, 107));
		signUp.setPreferredSize(screenSize);
		layeredPane.add(signUp, "signup");
		signUp.setLayout(null);
		
		JButton backBtnS = new JButton("");
		backBtnS.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		backBtnS.setRequestFocusEnabled(false);
		backBtnS.setBorderPainted(false);
		backBtnS.setBorder(null);
		backBtnS.setBounds(928, 128, 117, 29);
		signUp.add(backBtnS);
		
		JButton loginBtnS = new JButton("");
		loginBtnS.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginBtnS.setBorder(null);
		loginBtnS.setBounds(943, 804, 463, 62);
		signUp.add(loginBtnS);
		
		JButton signupBtnS = new JButton("");
		signupBtnS.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		signupBtnS.setBorder(null);
		signupBtnS.setBounds(943, 571, 463, 62);
		signUp.add(signupBtnS);
		
		JTextPane usernNoticeFilled = new JTextPane();
		usernNoticeFilled.setBounds(954, 324, 1, 16);
		signUp.add(usernNoticeFilled);
		
		JTextPane emailNoticeFilled = new JTextPane();
		emailNoticeFilled.setEditable(false);
		emailNoticeFilled.setBorder(null);
		//emailNoticeFilled.setText("Required: Please enter your Email Address");
		emailNoticeFilled.setForeground(Color.RED);
		emailNoticeFilled.setFont(new Font("SansSerif", Font.PLAIN, 10));
		//emailNoticeFilled.setBounds(945, 451, 443, 16);
		signUp.add(emailNoticeFilled);
		
		JTextPane passNoticefilled = new JTextPane();
		//passNoticefilled.setBounds(950, 468, 1, 16);
		signUp.add(passNoticefilled);
		
		usernameS = new JTextField("Username");
		usernameS.setForeground(Color.LIGHT_GRAY);
		usernameS.setFont(new Font("Montserrat", Font.PLAIN, 24));
		usernameS.setBorder(null);
		usernameS.setBounds(954, 324, 447, 52);
		signUp.add(usernameS);
		usernameS.setColumns(10);
		
		emailS = new JTextField("Email");
		emailS.setForeground(Color.LIGHT_GRAY);
		emailS.setFont(new Font("Montserrat", Font.PLAIN, 24));
		emailS.setBorder(null);
		emailS.setBounds(954, 399, 447, 52);
		signUp.add(emailS);
		emailS.setColumns(10);
		
		JCheckBox cbShowS = new JCheckBox("");
		cbShowS.setBounds(1382, 495, 29, 23);
		signUp.add(cbShowS);
		
		JTextPane txtShowS = new JTextPane();
		txtShowS.setForeground(Color.LIGHT_GRAY);
		txtShowS.setFont(new Font("Montserrat", Font.PLAIN, 10));
		txtShowS.setText("show");
		txtShowS.setBounds(1382, 480, 29, 16);
		signUp.add(txtShowS);
		
		passwordS = new JPasswordField("Password");
		passwordS.setEchoChar((char)0);
		passwordS.setForeground(Color.LIGHT_GRAY);
		passwordS.setFont(new Font("Montserrat", Font.PLAIN, 24));
		passwordS.setBorder(null);
		passwordS.setBounds(954, 475, 447, 52);
		signUp.add(passwordS);
		
		JLabel signUpLab = new JLabel("");
		signUpLab.setBounds(120, -1, 1440, 1024);
		signUpLab.setIcon(signUpIcon);
		signUp.add(signUpLab);
		
		JPanel MainPanel1 = new JPanel();
		MainPanel1.setBackground(new Color(253, 253, 253));
		//MainPanel1.setPreferredSize(screenSize);
		layeredPane.add(MainPanel1, "main1");
		MainPanel1.setLayout(null);
		
		JButton backBtnMain1 = new JButton("");
		backBtnMain1.setBorder(null);
		backBtnMain1.setBounds(192, 848, 43, 42);
		MainPanel1.add(backBtnMain1);
		
		JLabel mainLabel = new JLabel("");
		mainLabel.setIcon(new ImageIcon(LoginSystem.class.getResource("/imageSource/main#1.png")));
		mainLabel.setBounds(120, 0, 1440, 990);
		MainPanel1.add(mainLabel);
		
		
		
		
		//btn
		final boolean[] isPlaceholderActive = {true};
		
		signinBtnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button(signUp);
			}
		});
		
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button(signIn);
				
			}
		});
		loginBtnS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button(signIn);
				
			}
		});
		signupBtnM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button(signUp);
				
			}
		});
		backBtnS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button(Home);
				
			}
		});
		backBtnS_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button(Home);
				
			}
		});
		
		loginBtnM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username = usnlogin.getText();
				char[] passwordChar = passFieldLogin.getPassword();
				String password = new String(passwordChar);
				
				 try {
			        	boolean loginSuccess = userService.login(username, password);
			        	if(loginSuccess) {
			        		System.out.println("Login Successfully");
			        		new MainDashboard();
			        	}
			        	else {System.out.println("Login failed");}
			        	}catch (SQLException e1) {
			                System.out.println("An error occurred during login.");
			                e1.printStackTrace();
			        	}
				
				
			}
		});
		
		signupBtnS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameS.getText();
				String email = emailS.getText();
				char[] passwordChar = passwordS.getPassword();
				String password = new String(passwordChar);
				
							
				if(username.equals("Username") || email.equals("Email") || password.equals("Password")) {
					JOptionPane.showMessageDialog(null, "Please fill out all the field to continue","Notice", JOptionPane.INFORMATION_MESSAGE);
					if(username.equals("Username")) {
						usernameS.setForeground(Color.RED);
						
					}
					if(email.equals("Email")) {
						emailS.setForeground(Color.RED);
						
					}
					if(password.equals("Password")) {
						passwordS.setForeground(Color.RED);
						
					}
				}
				else {
					register.registerAuth(username, password, email);

				}
				
			}
		});
		backBtnMain1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button(Home);
				
			}
		});
		
		
		defText(usnlogin, "Username");
		defText(usernameS,"Username");
		defText(emailS, "Email");
		
		showPassCB(showPassChb, isPlaceholderActive, txtrShow, passFieldLogin);
		showPassCB(cbShowS, isPlaceholderActive, txtShowS, passwordS);
		
		defJPassText(passFieldLogin, "Password", isPlaceholderActive, showPassChb);
		defJPassText(passwordS, "Password", isPlaceholderActive, cbShowS);
		
		

		
	}
	
	//FUNCTION--
	
	public void button(JPanel panel) {
		layeredPane.removeAll();
		layeredPane.add(panel);
		layeredPane.repaint();
		layeredPane.revalidate();
				
	}
	//outside this class
	public void buttons(JPanel panel, JLayeredPane layeredPane) {
		layeredPane.removeAll();
		layeredPane.add(panel);
		layeredPane.repaint();
		layeredPane.revalidate();
				
	}
	
	public static void defText(JTextField textField, String value) {
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				String usn = new String(value);
				if(usn.equals(value)) {
					textField.setText("");
					textField.setForeground(Color.LIGHT_GRAY);
					
				}
			}
			public void focusLost(FocusEvent e) {
                if(textField.getText().length() == 0) {
                    // Reset placeholder text and disable masking
                	textField.setForeground(Color.LIGHT_GRAY);
                	textField.setText(value);
                }
            }
			
		});
				
	}
	
	public static void defJPassText(JPasswordField jpass, String value, boolean[] placeHolder, JCheckBox checkbox) {
		jpass.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String password = new String(jpass.getPassword());
                if(password.equals(value) && placeHolder[0]) {
                	jpass.setText("");
                	jpass.setForeground(Color.LIGHT_GRAY);
                	placeHolder[0] = false;
                    // Set echo char based on checkbox state
                    if(checkbox.isSelected()){
                    	jpass.setEchoChar((char)0);
                    } else {
                    	jpass.setEchoChar('\u25CF');
                    }
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if(jpass.getPassword().length == 0) {
                    // Reset placeholder text and disable masking
                	jpass.setText(value);
                	jpass.setForeground(Color.LIGHT_GRAY);
                	jpass.setEchoChar((char) 0);
                	placeHolder[0] = true;
                }
            }
        });
	}
	
	public static void showPassCB(JCheckBox chb, boolean[] placeHolder, JTextPane textPane, JPasswordField password) {
		chb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // If the placeholder is active, we don't change anything
                if(placeHolder[0]) {
                	textPane.setText(chb.isSelected() ? "hide" : "show");
                    return;
                }
                
                if(chb.isSelected()) {
                    // Show password: disable masking
                	password.setEchoChar((char)0);
                	textPane.setText("hide");
                	textPane.setEditable(false);
                } else {
                    // Hide password: enable masking
                	password.setEchoChar('\u25CF');
                	textPane.setText("show");
                	textPane.setEditable(false);
                }
            }
        });
		
	}

}
