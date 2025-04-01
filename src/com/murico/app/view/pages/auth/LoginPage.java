package com.murico.app.view.pages.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.murico.app.view.CurrentPage;
import com.murico.app.view.MainWindow;
import com.murico.app.view.common.labels.MLabelCaption;
import com.murico.app.view.common.labels.MLabelTitle;
import com.murico.app.view.components.buttons.variations.SecondaryButton;
import com.murico.app.view.components.buttons.variations.TransparentButton;
import com.murico.app.view.components.inputs.MPasswordFieldToggleable;
import com.murico.app.view.components.inputs.MTextField;

public class LoginPage extends JPanel {

  private static final long serialVersionUID = 1L;
  private final MainWindow mainWindow;
  // private var lblNewLabel;

  /**
   * Create the panel.
   */
  public LoginPage(MainWindow mainWindow) {
    this.mainWindow = mainWindow;

    setBackground(new Color(255, 255, 255));
    setLayout(
        new FormLayout(
            new ColumnSpec[] {new ColumnSpec(ColumnSpec.FILL,
                Sizes.bounded(Sizes.PREFERRED, Sizes.constant("480px", true),
                    Sizes.constant("2139px", true)),
                1),},
            new RowSpec[] {RowSpec.decode("690px"),}));

    var panel_1 = new JPanel();
    panel_1.setBackground(new Color(255, 255, 255));
    add(panel_1, "1, 1, center, center");
    panel_1.setLayout(
        new FormLayout(
            new ColumnSpec[] {ColumnSpec.decode("left:24px"),
                new ColumnSpec(ColumnSpec.FILL,
                    Sizes.bounded(Sizes.PREFERRED, Sizes.constant("480px", true),
                        Sizes.constant("960px", true)),
                    2),
                new ColumnSpec(ColumnSpec.FILL,
                    Sizes.bounded(Sizes.PREFERRED, Sizes.constant("240px", true),
                        Sizes.constant("640px", true)),
                    1),},
            new RowSpec[] {RowSpec.decode("650px"),}));

    var left_view = new JPanel();
    left_view.setPreferredSize(new Dimension(400, 200));
    left_view.setMinimumSize(new Dimension(200, 200));
    left_view.setBackground(Color.WHITE);
    panel_1.add(left_view, "2, 1, left, fill");
    left_view.setLayout(new FormLayout(
        new ColumnSpec[] {ColumnSpec.decode("75px"), ColumnSpec.decode("161px"),
            ColumnSpec.decode("119px"),},
        new RowSpec[] {RowSpec.decode("58px"), RowSpec.decode("470px"),}));

    var trnsprntbtnHello = new TransparentButton((String) null);
    trnsprntbtnHello.addActionListener(new TrnsprntbtnHelloAction());

    var lblNewLabel_2 = new JLabel("");
    lblNewLabel_2.setIcon(new ImageIcon(LoginPage.class.getResource("/assets/logo_icon.png")));
    left_view.add(lblNewLabel_2, "1, 1, fill, fill");
    trnsprntbtnHello
        .setIcon(new ImageIcon(LoginPage.class.getResource("/assets/icons/move-left.png")));
    trnsprntbtnHello.setText("  Back");
    left_view.add(trnsprntbtnHello, "3, 1, fill, top");

    var form = new JPanel();
    form.setBackground(Color.WHITE);
    left_view.add(form, "1, 2, 3, 1, center, center");
    form.setLayout(new FormLayout(new ColumnSpec[] {ColumnSpec.decode("center:355px"),},
        new RowSpec[] {FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, RowSpec.decode("85px"),
            FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("122px"), RowSpec.decode("29px"),
            RowSpec.decode("58px"), FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("67px"),
            FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("60px"),}));

    var lbltlSignIn = new MLabelTitle((String) null);
    lbltlSignIn.setText("Sign in");
    form.add(lbltlSignIn, "1, 2, fill, fill");

    var inputContainer = new JPanel();
    inputContainer.setBackground(Color.WHITE);
    form.add(inputContainer, "1, 4, fill, fill");
    inputContainer.setLayout(new GridLayout(0, 1, 0, 16));

    var txtfldTextField = new MTextField();
    txtfldTextField.setPlaceholderText("Username");
    inputContainer.add(txtfldTextField);

    var passwordFieldToggleable = new MPasswordFieldToggleable();
    var gridBagLayout = (GridBagLayout) passwordFieldToggleable.getLayout();
    gridBagLayout.rowWeights = new double[] {1.0};
    gridBagLayout.rowHeights = new int[] {53};
    gridBagLayout.columnWeights = new double[] {0.0, 0.0};
    gridBagLayout.columnWidths = new int[] {300, 53};
    inputContainer.add(passwordFieldToggleable);

    var scndrbtnSecondaryButton = new SecondaryButton();
    scndrbtnSecondaryButton.setText("Log in");
    form.add(scndrbtnSecondaryButton, "1, 6, fill, fill");

    var panel = new JPanel();
    panel.setLayout(null);
    panel.setBackground(Color.WHITE);
    form.add(panel, "1, 8, fill, fill");

    var separator = new JSeparator();
    separator.setSize(new Dimension(5, 0));
    separator.setPreferredSize(new Dimension(5, 2));
    separator.setMinimumSize(new Dimension(5, 0));
    separator.setBackground(Color.LIGHT_GRAY);
    separator.setBounds(0, 35, 160, 4);
    panel.add(separator);

    var separator_1 = new JSeparator();
    separator_1.setSize(new Dimension(5, 0));
    separator_1.setPreferredSize(new Dimension(5, 2));
    separator_1.setMinimumSize(new Dimension(5, 0));
    separator_1.setBackground(Color.LIGHT_GRAY);
    separator_1.setBounds(195, 35, 160, 4);
    panel.add(separator_1);

    var lblcptnOr = new MLabelCaption((String) null);
    lblcptnOr.setText("or");
    lblcptnOr.setForeground(Color.LIGHT_GRAY);
    lblcptnOr.setFont(new Font("Montserrat", Font.PLAIN, 24));
    lblcptnOr.setBackground(Color.LIGHT_GRAY);
    lblcptnOr.setBounds(165, 20, 26, 29);
    panel.add(lblcptnOr);

    var scndrbtnSecondaryButton_1 = new SecondaryButton();
    scndrbtnSecondaryButton_1.setText("Create an account");
    form.add(scndrbtnSecondaryButton_1, "1, 10, fill, fill");

      var lblNewLabel_1 = new JLabel("");
      lblNewLabel_1.setIcon(new ImageIcon(LoginPage.class.getResource("/assets/logo_login.png")));
      panel_1.add(lblNewLabel_1, "3, 1, right, fill");

  }

  private class TrnsprntbtnHelloAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      CurrentPage.setCurrentPage(CurrentPage.MAIN);
      mainWindow.render();
    }
  }
}
