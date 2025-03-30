package com.murico.app.view.pages.auth;

import com.murico.app.view.components.buttons.variations.PrimaryButton;
import com.murico.app.view.components.images.ResizableImageIcon;

import javax.swing.*;
import java.awt.*;

public class MainAuthPage extends JPanel {
  private JLabel logoContainer;

  private JPanel buttonsContainer;
  private PrimaryButton loginButton;
  private PrimaryButton registerButton;

  public MainAuthPage() {
    this.initComponents();
    this.initLayout();
  }

  private void initComponents() {
    ResizableImageIcon logoResizer = new ResizableImageIcon("/assets/logo_freeform.png");

    this.logoContainer = new JLabel(logoResizer.getImageIcon());

    this.buttonsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 32, 0));
    this.loginButton = new PrimaryButton("Login");
    this.registerButton = new PrimaryButton("Create an account");

    this.loginButton.setPreferredSize(new Dimension(250, 50));
    this.registerButton.setPreferredSize(new Dimension(250, 50));
  }

  private void initLayout() {
    this.setLayout(new GridLayout(0, 1, 0, 48));

    this.add(this.logoContainer);

    this.buttonsContainer.add(this.loginButton);
    this.buttonsContainer.add(this.registerButton);

    this.add(this.buttonsContainer);
  }
}
