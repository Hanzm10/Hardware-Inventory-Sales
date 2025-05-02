package com.github.hanzm_10.murico.swingapp.scenes.auth;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.formdev.flatlaf.ui.FlatBorder;
import com.github.hanzm_10.murico.swingapp.assets.AssetManager;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.navigation.SceneNavigator;
import com.github.hanzm_10.murico.swingapp.lib.navigation.scene.Scene;
import com.github.hanzm_10.murico.swingapp.ui.buttons.StyledButtonFactory;
import com.github.hanzm_10.murico.swingapp.ui.components.panels.ImagePanel;

import net.miginfocom.swing.MigLayout;

import com.github.hanzm_10.murico.swingapp.ui.buttons.ButtonStyles;

public class LoginAuthScene implements Scene {
    private static final Logger LOGGER = MuricoLogger.getLogger(LoginAuthScene.class);

    protected class BtnNavigationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isLoggingIn) {
                // this shouldn't happen anyway since we'll disable
                // the buttons
                return;
            }

            SceneNavigator.navigateTo(e.getActionCommand());
        }
    }

    protected class LoginListener implements ActionListener {
        @Override
            public void actionPerformed(ActionEvent e) {
                errorMessageName.setText("ERROR!");
                errorMessagePassword.setText("ERROR AAAA!");
            }
    }

    /** A flag so that the components are aware whether this scene is busy or not */
    protected boolean isLoggingIn;

    protected BtnNavigationListener btnNavigationListener = new BtnNavigationListener();
    protected LoginListener loginListener = new LoginListener();

	protected JPanel view;
    protected MigLayout viewLayout;

    protected JPanel leftComponent;
    protected MigLayout leftComponentLayout;

    protected ImagePanel rightComponent;

    protected JLabel signInLabel;

    protected JTextField nameInput;
    protected JLabel errorMessageName;
    protected JPasswordField passwordInput;
    protected JLabel errorMessagePassword;

    protected ImagePanel logo;
    protected JButton backBtn;

    protected JButton loginBtn;
    protected JButton registerBtn;

    protected JPanel btnSeparator;

	@Override
	public String getSceneName() {
		return "login";
    }

	@Override
	public JPanel getSceneView() {
		return view == null ? (view = new JPanel()) : view;
	}

    @Override
    public void onCreate() {
        createComponents();
        attachListeners();
    }

    private void attachListeners() {
        // call login controller, loading indicator, etc.
        loginBtn.addActionListener(loginListener);
        registerBtn.addActionListener(btnNavigationListener);
        backBtn.addActionListener(btnNavigationListener);

        registerBtn.setActionCommand("auth/register");
        backBtn.setActionCommand("auth/main");
    }

    private void createComponents() {
        try {
            Image image = AssetManager.getOrLoadImage("images/logo.png");
            logo = new ImagePanel(image);
            logo.setPreferredSize(new Dimension(96, 96));
            Image rightComponentImage = AssetManager.getOrLoadImage("images/auth_login.png");
            rightComponent = new ImagePanel(rightComponentImage);
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading image", e);
        }

        viewLayout = new MigLayout("", "[grow, right][24px][grow, left]", "[grow]");
        view.setLayout(viewLayout);

        leftComponent = new JPanel();
        leftComponent.setPreferredSize(new Dimension(720, 560));
        leftComponentLayout = new MigLayout(
            // insets
            "",
            // columns
            "[170px,left][170px,right]",
            // rows
            "[72px::]48[]16[40px::]2[]12[40px::]2[]20[48px::]24[32px::]12[48px::]"
        );
        leftComponent.setLayout(leftComponentLayout);

        backBtn = StyledButtonFactory.createButton("<- Back", ButtonStyles.TRANSPARENT);
        ((FlatBorder) backBtn.getBorder()).applyStyleProperty("borderColor", new Color(0xff, true));

        signInLabel = new JLabel("Sign in");
        signInLabel.setFont(signInLabel.getFont().deriveFont(Font.BOLD,48));

        nameInput = new JTextField();
        errorMessageName = new JLabel();
        errorMessageName.setFont(errorMessageName.getFont().deriveFont(Font.BOLD,10));
        // TODO: Toggleable password reveal
        passwordInput = new JPasswordField();
        errorMessagePassword = new JLabel();
        errorMessagePassword.setFont(errorMessagePassword.getFont().deriveFont(Font.BOLD,10));
        loginBtn = StyledButtonFactory.createButton("Log In", ButtonStyles.SECONDARY);
        registerBtn = StyledButtonFactory.createButton("Create an account", ButtonStyles.SECONDARY);
        btnSeparator = new JPanel();

        // TODO: fallback image
        if (logo != null) {
            leftComponent.add(logo, "cell 0 0");
        }

        leftComponent.add(backBtn, "cell 1 0");

        // TODO: Placeholder text for labels

        leftComponent.add(signInLabel, "cell 0 1 2, grow");
        leftComponent.add(nameInput, "cell 0 2 2, grow");
        leftComponent.add(errorMessageName, "cell 0 3 2, grow");
        leftComponent.add(passwordInput, "cell 0 4 2, grow");
        leftComponent.add(errorMessagePassword, "cell 0 5 2, grow");
        leftComponent.add(loginBtn, "cell 0 6 2, grow");
        leftComponent.add(btnSeparator, "cell 0 7 2, grow");
        leftComponent.add(registerBtn, "cell 0 8 2, grow");

        view.add(leftComponent);

        if (rightComponent != null) {
            view.add(rightComponent);
        }
    }

}
