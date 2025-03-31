package com.murico.app.view.pages.auth;

import com.murico.app.view.MainWindow;
import com.murico.app.view.components.buttons.variations.PrimaryButton;
import com.murico.app.view.components.images.ResizableImageIcon;

import javax.swing.*;
import java.awt.*;

public class MainAuthPage {
  private MainWindow mainWindow;
  private JLabel logoContainer;

  private JPanel buttonsContainer;
  private PrimaryButton loginButton;
  private PrimaryButton registerButton;

  public MainAuthPage(MainWindow mainWindow) {
    this.mainWindow = mainWindow;

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
    this.mainWindow.setLayout(new GridBagLayout());

    GridBagConstraints constraints = new GridBagConstraints();

    constraints.fill = GridBagConstraints.BOTH;
    constraints.weightx = 1.0;
    constraints.weighty = 0.25;
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.CENTER;

    this.mainWindow.add(this.logoContainer, constraints);

    this.buttonsContainer.add(this.loginButton);
    this.buttonsContainer.add(this.registerButton);

    constraints.gridy = 1;
    constraints.anchor = GridBagConstraints.CENTER;
    constraints.fill = GridBagConstraints.NONE;

    this.mainWindow.add(this.buttonsContainer, constraints);
  }
}
