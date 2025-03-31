package com.murico.app.view.pages.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import com.murico.app.view.common.labels.MLabelCaption;
import com.murico.app.view.common.labels.MLabelTitle;
import com.murico.app.view.components.buttons.variations.SecondaryButton;
import com.murico.app.view.components.buttons.variations.TransparentButton;
import com.murico.app.view.components.inputs.MPasswordFieldToggleable;
import com.murico.app.view.components.inputs.MTextField;

public class LoginPage extends JPanel {

  private static final long serialVersionUID = 1L;
  //private var lblNewLabel;

  /**
   * Create the panel.
   */
  public LoginPage() {
    setBackground(new Color(255, 255, 255));
    setLayout(null);

    var left_view = new JPanel();
    left_view.setBackground(new Color(255, 255, 255));
    left_view.setBounds(39, 37, 381, 616);
    add(left_view);
    left_view.setLayout(null);

    var lblNewLabel_2 = new JLabel("");
    lblNewLabel_2.setIcon(new ImageIcon(LoginPage.class.getResource("/assets/logo_icon.png")));
    lblNewLabel_2.setBounds(10, 11, 75, 58);
    left_view.add(lblNewLabel_2);

    var trnsprntbtnHello = new TransparentButton((String) null);
    trnsprntbtnHello.addActionListener(new TrnsprntbtnHelloAction());
    trnsprntbtnHello
        .setIcon(new ImageIcon(LoginPage.class.getResource("/assets/icons/move-left.png")));
    trnsprntbtnHello.setText("  Back");
    trnsprntbtnHello.setBounds(246, 12, 119, 40);
    left_view.add(trnsprntbtnHello);

    var form = new JPanel();
    form.setBackground(new Color(255, 255, 255));
    form.setBounds(10, 105, 355, 470);
    left_view.add(form);
    var gbl_form = new GridBagLayout();
    gbl_form.columnWidths = new int[] {361, 0};
    gbl_form.rowHeights = new int[] {90, 50, 57, 79, 35, 0};
    gbl_form.columnWeights = new double[] {1.0, Double.MIN_VALUE};
    gbl_form.rowWeights = new double[] {0.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
    form.setLayout(gbl_form);

    var lbltlSignIn = new MLabelTitle((String) null);
    var gbc_lbltlSignIn = new GridBagConstraints();
    gbc_lbltlSignIn.fill = GridBagConstraints.BOTH;
    gbc_lbltlSignIn.insets = new Insets(0, 0, 5, 0);
    gbc_lbltlSignIn.gridx = 0;
    gbc_lbltlSignIn.gridy = 0;
    form.add(lbltlSignIn, gbc_lbltlSignIn);
    lbltlSignIn.setText("Sign in");

    var inputContainer = new JPanel();
    inputContainer.setBackground(new Color(255, 255, 255));
    var gbc_inputContainer = new GridBagConstraints();
    gbc_inputContainer.insets = new Insets(0, 0, 5, 0);
    gbc_inputContainer.fill = GridBagConstraints.BOTH;
    gbc_inputContainer.gridx = 0;
    gbc_inputContainer.gridy = 1;
    form.add(inputContainer, gbc_inputContainer);
    inputContainer.setLayout(new GridLayout(0, 1, 0, 16));

    var txtfldTextField = new MTextField();
    txtfldTextField.setPlaceholderText("Username");
    inputContainer.add(txtfldTextField);

    var passwordFieldToggleable = new MPasswordFieldToggleable();
    var gridBagLayout = (GridBagLayout) passwordFieldToggleable.getLayout();
    gridBagLayout.rowHeights = new int[] {53};
    inputContainer.add(passwordFieldToggleable);

    var scndrbtnSecondaryButton = new SecondaryButton();
    scndrbtnSecondaryButton.setText("Log in");
    var gbc_scndrbtnSecondaryButton = new GridBagConstraints();
    gbc_scndrbtnSecondaryButton.insets = new Insets(24, 0, 5, 0);
    gbc_scndrbtnSecondaryButton.fill = GridBagConstraints.BOTH;
    gbc_scndrbtnSecondaryButton.gridx = 0;
    gbc_scndrbtnSecondaryButton.gridy = 2;
    form.add(scndrbtnSecondaryButton, gbc_scndrbtnSecondaryButton);

    var panel = new JPanel();
    panel.setBackground(new Color(255, 255, 255));
    panel.setLayout(null);
    var gbc_panel = new GridBagConstraints();
    gbc_panel.insets = new Insets(0, 0, 5, 0);
    gbc_panel.fill = GridBagConstraints.BOTH;
    gbc_panel.gridx = 0;
    gbc_panel.gridy = 3;
    form.add(panel, gbc_panel);

    var separator = new JSeparator();
    separator.setSize(new Dimension(5, 0));
    separator.setMinimumSize(new Dimension(5, 0));
    separator.setPreferredSize(new Dimension(5, 2));
    separator.setBackground(new Color(192, 192, 192));
    separator.setBounds(0, 35, 160, 4);
    panel.add(separator);

    var separator_1 = new JSeparator();
    separator_1.setSize(new Dimension(5, 0));
    separator_1.setPreferredSize(new Dimension(5, 2));
    separator_1.setMinimumSize(new Dimension(5, 0));
    separator_1.setBackground(new Color(192, 192, 192));
    separator_1.setBounds(195, 35, 160, 4);
    panel.add(separator_1);

    var lblcptnOr = new MLabelCaption((String) null);
    lblcptnOr.setBackground(new Color(192, 192, 192));
    lblcptnOr.setForeground(new Color(192, 192, 192));
    lblcptnOr.setFont(new Font("Montserrat", Font.PLAIN, 24));
    lblcptnOr.setText("or");
    lblcptnOr.setBounds(165, 20, 26, 29);
    panel.add(lblcptnOr);

    var scndrbtnSecondaryButton_1 = new SecondaryButton();
    scndrbtnSecondaryButton_1.setText("Create an account");
    var gbc_scndrbtnSecondaryButton_1 = new GridBagConstraints();
    gbc_scndrbtnSecondaryButton_1.fill = GridBagConstraints.BOTH;
    gbc_scndrbtnSecondaryButton_1.gridx = 0;
    gbc_scndrbtnSecondaryButton_1.gridy = 4;
    form.add(scndrbtnSecondaryButton_1, gbc_scndrbtnSecondaryButton_1);

    var lblNewLabel_1 = new JLabel("");
    lblNewLabel_1.setIcon(new ImageIcon(LoginPage.class.getResource("/assets/logo_login.png")));
    lblNewLabel_1.setBounds(430, 35, 484, 616);
    add(lblNewLabel_1);

  }

  private class TrnsprntbtnHelloAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {}
  }
}
