/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.app.scenes.auth;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import com.github.hanzm_10.murico.database.model.user.User;

public class LoginSystem extends JLayeredPane {

    public static String username = "habadu";

    public static void defJPassText(JPasswordField jpass, String value, boolean[] placeHolder, JCheckBox checkbox) {
        jpass.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                var password = new String(jpass.getPassword());
                if (password.equals(value) && placeHolder[0]) {
                    jpass.setText("");
                    jpass.setForeground(Color.LIGHT_GRAY);
                    placeHolder[0] = false;
                    // Set echo char based on checkbox state
                    if (checkbox.isSelected()) {
                        jpass.setEchoChar((char) 0);
                    } else {
                        jpass.setEchoChar('\u25CF');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (jpass.getPassword().length == 0) {
                    // Reset placeholder text and disable masking
                    jpass.setText(value);
                    jpass.setForeground(Color.LIGHT_GRAY);
                    jpass.setEchoChar((char) 0);
                    placeHolder[0] = true;
                }
            }
        });
    }

    public static void defText(JTextField textField, String value) {
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                var usn = new String(value);
                if (usn.equals(value)) {
                    textField.setText("");
                    textField.setForeground(Color.LIGHT_GRAY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    // Reset placeholder text and disable masking
                    textField.setForeground(Color.LIGHT_GRAY);
                    textField.setText(value);
                }
            }
        });
    }

    /** Launch the application. */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                var window = new LoginSystem();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void showPassCB(JCheckBox chb, boolean[] placeHolder, JTextPane textPane, JPasswordField password) {
        chb.addActionListener(e -> {
            // If the placeholder is active, we don't change anything
            if (placeHolder[0]) {
                textPane.setText(chb.isSelected() ? "hide" : "show");
                return;
            }

            if (chb.isSelected()) {
                // Show password: disable masking
                password.setEchoChar((char) 0);
                textPane.setText("hide");
            } else {
                // Hide password: enable masking
                password.setEchoChar('\u25CF');
                textPane.setText("show");
            }
            textPane.setEditable(false);
        });
    }

    private JTextField usnlogin;
    private JPasswordField passFieldLogin;
    // private UserService userService;
    private User user;

    private JTextField usernameS;

    private JTextField emailS;

    private JPasswordField passwordS;

    // private Register register;

    public LoginSystem() {
        initialize();
    }

    public void button(JPanel panel) {
        removeAll();
        add(panel);
        repaint();
        revalidate();
    }

    // outside this class
    public void buttons(JPanel panel) {
        removeAll();
        add(panel);
        repaint();
        revalidate();
    }

    /** Initialize the contents of the frame. */
    private void initialize() {
        setLayout(new CardLayout(0, 0));
        // Panel for home
        var Home = new JPanel();
        Home.setBackground(Color.WHITE);
        add(Home, "home");

        var homeIcon = new ImageIcon(LoginSystem.class.getResource("/imageSource/Home.png"));
        Home.setLayout(null);

        var loginBtn = new JButton("");
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setBorder(null);
        loginBtn.setBounds(474, 674, 306, 50);

        var signinBtnHome = new JButton("");
        signinBtnHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signinBtnHome.setBorder(null);
        signinBtnHome.setBounds(898, 674, 306, 50);
        Home.add(signinBtnHome);
        Home.add(loginBtn);

        var homeLab = new JLabel("");
        homeLab.setBounds(120, -1, 1440, 1024);
        homeLab.setIcon(homeIcon);
        Home.add(homeLab);

        // Panel for SignIn
        var signIn = new JPanel();
        signIn.setBackground(new Color(255, 255, 255));
        add(signIn, "signin");
        signIn.setLayout(null);

        var txtrShow = new JTextPane();
        txtrShow.setText("show");
        txtrShow.setFont(new Font("Montserrat", Font.PLAIN, 10));
        txtrShow.setForeground(Color.LIGHT_GRAY);
        txtrShow.setBounds(583, 460, 29, 16);
        signIn.add(txtrShow);

        var showPassChb = new JCheckBox("");
        showPassChb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showPassChb.setBounds(583, 475, 29, 23);
        signIn.add(showPassChb);

        var backBtnS_1 = new JButton("");
        backBtnS_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtnS_1.setRequestFocusEnabled(false);
        backBtnS_1.setBorderPainted(false);
        backBtnS_1.setBorder(null);
        backBtnS_1.setBounds(519, 81, 117, 29);
        signIn.add(backBtnS_1);

        var signupBtnM = new JButton("");
        signupBtnM.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupBtnM.setBorder(null);
        signupBtnM.setBounds(204, 723, 414, 62);
        signIn.add(signupBtnM);

        var loginBtnM = new JButton("");
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
        passFieldLogin.setEchoChar((char) 0);
        passFieldLogin.setForeground(Color.LIGHT_GRAY);
        passFieldLogin.setFont(new Font("Montserrat", Font.PLAIN, 24));
        passFieldLogin.setBorder(null);
        passFieldLogin.setBounds(214, 455, 388, 56);
        signIn.add(passFieldLogin);

        // ImageIcon signInIcon = new
        // ImageIcon(Last.class.getResource("/imgSrc/signIn.png"));

        var signInLab = new JLabel("");
        signInLab.setBounds(66, 0, 1547, 1050);
        signInLab.setIcon(new ImageIcon(LoginSystem.class.getResource("/imageSource/login.png")));
        signIn.add(signInLab);

        var signUpIcon = new ImageIcon(LoginSystem.class.getResource("/imageSource/create.png"));

        var signUp = new JPanel();
        signUp.setBackground(new Color(33, 64, 107));
        add(signUp, "signup");
        signUp.setLayout(null);

        var backBtnS = new JButton("");
        backBtnS.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtnS.setRequestFocusEnabled(false);
        backBtnS.setBorderPainted(false);
        backBtnS.setBorder(null);
        backBtnS.setBounds(928, 128, 117, 29);
        signUp.add(backBtnS);

        var loginBtnS = new JButton("");
        loginBtnS.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtnS.setBorder(null);
        loginBtnS.setBounds(943, 804, 463, 62);
        signUp.add(loginBtnS);

        var signupBtnS = new JButton("");
        signupBtnS.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupBtnS.setBorder(null);
        signupBtnS.setBounds(943, 571, 463, 62);
        signUp.add(signupBtnS);

        var usernNoticeFilled = new JTextPane();
        usernNoticeFilled.setBounds(954, 324, 1, 16);
        signUp.add(usernNoticeFilled);

        var emailNoticeFilled = new JTextPane();
        emailNoticeFilled.setEditable(false);
        emailNoticeFilled.setBorder(null);
        // emailNoticeFilled.setText("Required: Please enter your Email Address");
        emailNoticeFilled.setForeground(Color.RED);
        emailNoticeFilled.setFont(new Font("SansSerif", Font.PLAIN, 10));
        // emailNoticeFilled.setBounds(945, 451, 443, 16);
        signUp.add(emailNoticeFilled);

        var passNoticefilled = new JTextPane();
        // passNoticefilled.setBounds(950, 468, 1, 16);
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

        var cbShowS = new JCheckBox("");
        cbShowS.setBounds(1382, 495, 29, 23);
        signUp.add(cbShowS);

        var txtShowS = new JTextPane();
        txtShowS.setForeground(Color.LIGHT_GRAY);
        txtShowS.setFont(new Font("Montserrat", Font.PLAIN, 10));
        txtShowS.setText("show");
        txtShowS.setBounds(1382, 480, 29, 16);
        signUp.add(txtShowS);

        passwordS = new JPasswordField("Password");
        passwordS.setEchoChar((char) 0);
        passwordS.setForeground(Color.LIGHT_GRAY);
        passwordS.setFont(new Font("Montserrat", Font.PLAIN, 24));
        passwordS.setBorder(null);
        passwordS.setBounds(954, 475, 447, 52);
        signUp.add(passwordS);

        var signUpLab = new JLabel("");
        signUpLab.setBounds(120, -1, 1440, 1024);
        signUpLab.setIcon(signUpIcon);
        signUp.add(signUpLab);

        var MainPanel1 = new JPanel();
        MainPanel1.setBackground(new Color(253, 253, 253));
        // MainPanel1.setPreferredSize(screenSize);
        add(MainPanel1, "main1");
        MainPanel1.setLayout(null);

        var backBtnMain1 = new JButton("");
        backBtnMain1.setBorder(null);
        backBtnMain1.setBounds(192, 848, 43, 42);
        MainPanel1.add(backBtnMain1);

        var mainLabel = new JLabel("");
        mainLabel.setIcon(new ImageIcon(LoginSystem.class.getResource("/imageSource/main#1.png")));
        mainLabel.setBounds(120, 0, 1440, 990);
        MainPanel1.add(mainLabel);

        // btn
        final boolean[] isPlaceholderActive = {true};

        signinBtnHome.addActionListener(e -> button(signUp));

        loginBtn.addActionListener(e -> button(signIn));
        loginBtnS.addActionListener(e -> button(signIn));
        signupBtnM.addActionListener(e -> button(signUp));
        backBtnS.addActionListener(e -> button(Home));
        backBtnS_1.addActionListener(e -> button(Home));

        loginBtnM.addActionListener(e -> {

            /*
             * boolean loginSuccess = userService.login(username, password); if
             * (loginSuccess) { System.out.println("Login Successfully"); new
             * MainDashboard(); } else { System.out.println("Login failed"); }
             */

        });

        signupBtnS.addActionListener(e -> {
            var username = usernameS.getText();
            var email = emailS.getText();
            var passwordChar = passwordS.getPassword();
            var password = new String(passwordChar);

            if (username.equals("Username") || email.equals("Email") || password.equals("Password")) {
                JOptionPane.showMessageDialog(null, "Please fill out all the field to continue", "Notice",
                        JOptionPane.INFORMATION_MESSAGE);
                if (username.equals("Username")) {
                    usernameS.setForeground(Color.RED);
                }
                if (email.equals("Email")) {
                    emailS.setForeground(Color.RED);
                }
                if (password.equals("Password")) {
                    passwordS.setForeground(Color.RED);
                }
            } else {
                // register.registerAuth(username, password, email);

            }
        });
        backBtnMain1.addActionListener(e -> button(Home));

        defText(usnlogin, "Username");
        defText(usernameS, "Username");
        defText(emailS, "Email");

        showPassCB(showPassChb, isPlaceholderActive, txtrShow, passFieldLogin);
        showPassCB(cbShowS, isPlaceholderActive, txtShowS, passwordS);

        defJPassText(passFieldLogin, "Password", isPlaceholderActive, showPassChb);
        defJPassText(passwordS, "Password", isPlaceholderActive, cbShowS);
    }
}
