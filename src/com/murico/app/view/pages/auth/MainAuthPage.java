package com.murico.app.view.pages.auth;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.murico.app.config.UISettings;
import com.murico.app.view.builder.ColumnSpecBuilder;
import com.murico.app.view.builder.RowSpecBuilder;
import com.murico.app.view.components.buttons.variations.PrimaryButton;
import com.murico.app.view.pages.Page;

public class MainAuthPage extends Page implements Page.PageInterface {

  /**
   * 
   */
  private static final long serialVersionUID = 7930421087820543267L;

  private JPanel wrapperPanel;

  private JLabel heroImage;

  private JPanel btnsContainer;

  private PrimaryButton loginBtn;
  private PrimaryButton registerBtn;

  ActionListener navigationListener;

  public MainAuthPage(ActionListener navigationListener) {
    super();

    assert navigationListener != null : "Navigation listener cannot be null";

    this.navigationListener = navigationListener;

    initializeComponents();
    initializeLayout();
    attachComponents();
  }

  @Override
  public void initializeComponents() {
    wrapperPanel = new JPanel();
    heroImage =
        new JLabel(new ImageIcon(MainAuthPage.class.getResource("/assets/logo_freeform.png")));
    btnsContainer = new JPanel();
    loginBtn = new PrimaryButton("Login");
    loginBtn.setActionCommand("login");
    loginBtn.addActionListener(navigationListener);
    registerBtn = new PrimaryButton("Create an account");
    registerBtn.setActionCommand("register");
    registerBtn.addActionListener(navigationListener);
  }

  @Override
  public void initializeLayout() {
    setLayout(new FormLayout("center:default:grow", "center:default:grow"));

    var spaceM = UISettings.getInstance().getUISpace().getSpaceM();

    var columnSpec = new ColumnSpecBuilder().addColumn("max(280px;default):grow").build();
    var rowSpec = new RowSpecBuilder().addRow("max(48px;default)")
        .addRow(spaceM).addRow("max(48px;default)")
        .build();

    wrapperPanel.setLayout(new FormLayout(columnSpec, rowSpec));

    var btnsContainerColumnSpec = new ColumnSpecBuilder().addColumn("max(280px;default)")
        .addColumn(spaceM).addColumn("max(280px;default)").build();
    var btnsContainerRowSpec = new RowSpecBuilder().addRow("max(48px;default)").build();
    
    btnsContainer.setLayout(new FormLayout(btnsContainerColumnSpec, btnsContainerRowSpec));
  }

  @Override
  public void attachComponents() {
    btnsContainer.add(loginBtn, "1, 1, fill, fill");
    btnsContainer.add(registerBtn, "3, 1, fill, fill");

    wrapperPanel.add(heroImage, "1, 1, center, center");
    wrapperPanel.add(btnsContainer, "1, 3, center, top");

    add(wrapperPanel, "1, 1, center, center");
  }
}
