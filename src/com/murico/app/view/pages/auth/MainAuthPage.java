package com.murico.app.view.pages.auth;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.murico.app.view.CurrentPage;
import com.murico.app.view.MainWindow;
import com.murico.app.view.components.buttons.variations.PrimaryButton;

public class MainAuthPage extends JPanel {

  private static final long serialVersionUID = 1L;

  private final MainWindow mainWindow;

  /**
   * Create the panel.
   */
  public MainAuthPage(MainWindow mainWindow) {
    setBackground(new Color(255, 255, 255));
    this.mainWindow = mainWindow;

    setLayout(new FormLayout(new ColumnSpec[] {ColumnSpec.decode("1024px:grow"),},
        new RowSpec[] {RowSpec.decode("max(118dlu;pref)"), RowSpec.decode("32px"),
            RowSpec.decode("max(47px;default):grow"),}));

    var lblNewLabel = new JLabel("");
    lblNewLabel.setIcon(new ImageIcon(MainAuthPage.class.getResource("/assets/logo_freeform.png")));
    add(lblNewLabel, "1, 1, center, center");

    var panel = new JPanel();
    panel.setBackground(new Color(255, 255, 255));
    add(panel, "1, 3, center, top");
    panel.setLayout(new FormLayout(
        new ColumnSpec[] {ColumnSpec.decode("max(280px;default)"),
            new ColumnSpec(ColumnSpec.FILL,
                Sizes.bounded(Sizes.PREFERRED, Sizes.constant("64px", true),
                    Sizes.constant("128px", true)),
                1),
            ColumnSpec.decode("max(280px;default)"),},
        new RowSpec[] {RowSpec.decode("max(48px;default)"),}));

    var prmrbtnLogin = new PrimaryButton();
    prmrbtnLogin.setText("Login");
    prmrbtnLogin.addActionListener(new PrimaryButtonAction());
    panel.add(prmrbtnLogin, "1, 1, fill, fill");

    var prmrbtnPrimaryButton = new PrimaryButton();
    prmrbtnPrimaryButton.setText("Create an account");
    panel.add(prmrbtnPrimaryButton, "3, 1, fill, fill");

  }

  private class PrimaryButtonAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      CurrentPage.setCurrentPage(CurrentPage.LOGIN);
      mainWindow.render();
    }
  }
}
