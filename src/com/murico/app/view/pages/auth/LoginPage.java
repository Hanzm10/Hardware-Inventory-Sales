package com.murico.app.view.pages.auth;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.murico.app.view.common.labels.MLabelTitle;
import com.murico.app.view.components.buttons.variations.TransparentButton;

public class LoginPage extends JPanel {

  private static final long serialVersionUID = 1L;
  //private var lblNewLabel;

  /**
   * Create the panel.
   */
  public LoginPage() {
    setBackground(new Color(255, 255, 255));
    setLayout(null);

    var panel = new JPanel();
    panel.setBackground(new Color(255, 255, 255));
    panel.setBounds(39, 37, 389, 616);
    add(panel);
    panel.setLayout(null);

    var lblNewLabel_2 = new JLabel("");
    lblNewLabel_2.setIcon(new ImageIcon(LoginPage.class.getResource("/assets/logo_icon.png")));
    lblNewLabel_2.setBounds(10, 11, 75, 58);
    panel.add(lblNewLabel_2);

    var trnsprntbtnHello = new TransparentButton((String) null);
    trnsprntbtnHello.setText("← Back");
    trnsprntbtnHello.setBounds(269, 11, 110, 40);
    panel.add(trnsprntbtnHello);

    var lbltlSignIn = new MLabelTitle((String) null);
    lbltlSignIn.setText("Sign in");
    lbltlSignIn.setBounds(10, 137, 190, 70);
    panel.add(lbltlSignIn);

    var lblNewLabel_1 = new JLabel("");
    lblNewLabel_1.setIcon(new ImageIcon(LoginPage.class.getResource("/assets/logo_login.png")));
    lblNewLabel_1.setBounds(459, 37, 448, 616);
    add(lblNewLabel_1);

  }
}
