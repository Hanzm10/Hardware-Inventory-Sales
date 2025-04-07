package com.murico.app.view.pages.auth;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import com.jgoodies.forms.layout.FormLayout;
import com.murico.app.config.UISettings;
import com.murico.app.view.builder.ColumnSpecBuilder;
import com.murico.app.view.builder.RowSpecBuilder;
import com.murico.app.view.common.labels.MLabelCaption;
import com.murico.app.view.common.labels.MLabelSubtitle;
import com.murico.app.view.components.buttons.variations.SecondaryButton;
import com.murico.app.view.components.buttons.variations.TransparentButton;
import com.murico.app.view.components.inputs.MPasswordFieldToggleable;
import com.murico.app.view.components.inputs.MTextField;
import com.murico.app.view.pages.Page;

public class LoginPage extends Page implements Page.PageInterface {

  /**
   * 
   */
  private static final long serialVersionUID = 3158373793534651652L;

  private JPanel wrapperPanel;

  private JPanel leftPanel;
  private JLabel rightComponent;

  private JLabel logoIcon;
  private TransparentButton backButton;
  private JPanel formPanel;

  private JLabel signInLabel;
  private JPanel inputContainer;
  private MTextField usernameField;
  private MPasswordFieldToggleable passwordField;

  private SecondaryButton loginButton;
  private JPanel separatorPanel;

  private JSeparator separatorLeft;
  private MLabelCaption separatorText;
  private JSeparator separatorRight;

  private SecondaryButton registerButton;

  ActionListener navigationListener;

  public LoginPage(ActionListener navigationListener) {
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

    leftPanel = new JPanel();
    rightComponent =
        new JLabel(new ImageIcon(LoginPage.class.getResource("/assets/logo_login.png")));

    formPanel = new JPanel();
    logoIcon = new JLabel(new ImageIcon(LoginPage.class.getResource("/assets/logo_icon.png")));
    backButton = new TransparentButton("   Back");
    backButton.setFocusable(false);
    backButton.setActionCommand("main");
    backButton.addActionListener(navigationListener);
    backButton.setIcon(new ImageIcon(LoginPage.class.getResource("/assets/icons/move-left.png")));

    signInLabel = new MLabelSubtitle("Sign in");
    inputContainer = new JPanel();

    usernameField = new MTextField("", "Username");
    passwordField = new MPasswordFieldToggleable();

    loginButton = new SecondaryButton("Log in");
    loginButton.setActionCommand("login");

    separatorPanel = new JPanel();
    separatorLeft = new JSeparator();
    separatorText = new MLabelCaption("or");
    separatorRight = new JSeparator();

    registerButton = new SecondaryButton("Create an account");
    registerButton.setActionCommand("register");
    registerButton.addActionListener(navigationListener);
  }

  @Override
  public void initializeLayout() {
    setLayout(new FormLayout("center:default:grow", "center:default:grow"));
    
    var spaceL = UISettings.getInstance().getUISpace().getSpaceL();
    var spaceM = UISettings.getInstance().getUISpace().getSpaceM();
    var spaceXL = UISettings.getInstance().getUISpace().getSpaceXL();

    var wrapperPanelColumnSpec =
        new ColumnSpecBuilder().addColumn(spaceM).addColumn("default:grow").addColumn(spaceM)
            .addColumn("default:grow").addColumn(spaceM).build();
    var wrapperPanelRowSpec =
        new RowSpecBuilder().addRow(spaceM).addRow("default:grow").addRow(spaceM).build();

    wrapperPanel.setLayout(new FormLayout(wrapperPanelColumnSpec, wrapperPanelRowSpec));

    var leftPanelColumnSpec = new ColumnSpecBuilder().addColumn("max(75px;default)")
        .addColumn("default:grow").addColumn("max(119px;default)").build();
    var leftPanelRowSpec = new RowSpecBuilder().addRow("58px").addRow("default:grow").build();

    leftPanel.setLayout(new FormLayout(leftPanelColumnSpec, leftPanelRowSpec));

    var formPanelColumnSpec = new ColumnSpecBuilder().addColumn("center:355px").build();
    var formPanelRowSpec =
        new RowSpecBuilder().addRow("48px").addRow(spaceXL).addRow("122px").addRow(spaceL)
            .addRow("58px").addRow(spaceL).addRow("32px").addRow(spaceM)
        .addRow("60px").build();

    formPanel.setLayout(new FormLayout(formPanelColumnSpec, formPanelRowSpec));

    inputContainer.setLayout(new GridLayout(0, 1, 0, 16));

    var separatorPanelColumnSpec = new ColumnSpecBuilder().addColumn("fill:default:grow(1.0)")
        .addColumn("center:max(48px;default)")
        .addColumn("fill:default:grow(1.0)").build();
    var separatorPanelRowSpec = new RowSpecBuilder().addRow("fill:default:grow(1.0)").build();

    separatorPanel.setLayout(new FormLayout(separatorPanelColumnSpec, separatorPanelRowSpec));
  }

  @Override
  public void attachComponents() {
    inputContainer.add(usernameField);
    inputContainer.add(passwordField);

    separatorPanel.add(separatorLeft, "1, 1, fill, center");
    separatorPanel.add(separatorText, "2, 1, center, center");
    separatorPanel.add(separatorRight, "3, 1, fill, center");

    formPanel.add(signInLabel, "1, 1, fill, top");
    formPanel.add(inputContainer, "1, 3, fill, fill");
    formPanel.add(loginButton, "1, 5, fill, fill");
    formPanel.add(separatorPanel, "1, 7, fill, fill");
    formPanel.add(registerButton, "1, 9, fill, fill");

    leftPanel.add(logoIcon, "1, 1, fill, center");
    leftPanel.add(backButton, "3, 1, fill, center");
    leftPanel.add(formPanel, "1, 2, 3, 1, center, center");

    wrapperPanel.add(leftPanel, "2, 2, left, fill");
    wrapperPanel.add(rightComponent, "4, 2, fill, fill");

    add(wrapperPanel, "1, 1, center, center");
  }

}