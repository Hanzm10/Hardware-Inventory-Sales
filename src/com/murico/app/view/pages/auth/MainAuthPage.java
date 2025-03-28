package com.murico.app.view.pages.auth;

import com.murico.app.view.components.buttons.variations.PrimaryButton;
import java.util.Objects;
import javax.swing.*;

public class MainAuthPage extends JPanel {
  private PrimaryButton loginButton;
  private PrimaryButton registerButton;
  private ImageIcon logo;

  public MainAuthPage() {
    this.initComponents();
    this.initLayout();
  }

  private void initComponents() {
    this.logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/logo.png")));
    this.loginButton = new PrimaryButton("Login");
    this.registerButton = new PrimaryButton("Register");
  }

  private void initLayout() {
    System.out.println(this.logo.getIconWidth() + " " + this.logo.getIconHeight());

    this.add(new JLabel(this.logo));
    this.add(this.loginButton);
    this.add(this.registerButton);
  }
}
