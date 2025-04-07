package com.murico.app.view.pages.auth;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import com.murico.app.view.pages.Page;

public class AuthPage extends Page implements Page.PageInterface {

  /**
   * 
   */
  private static final long serialVersionUID = -1342228009381625226L;

  private CardLayout cardLayout;

  private Page mainPage;
  private Page loginPage;
  private Page registerPage;

  // Shared instance of the navigation listener
  private NavigationListener navigationListener;

  public AuthPage() {
    super();

    navigationListener = new NavigationListener();

    initializeComponents();
    initializeLayout();
    attachComponents();
  }

  public enum Pages {
    MAIN, LOGIN, REGISTER;
  }

  public void setVisiblePanel(Pages panel) {
    var page = switch (panel) {
      case MAIN -> mainPage;
      case LOGIN -> loginPage;
      case REGISTER -> registerPage;
    };

    System.out.println("Switching to page: " + page.getPageName());
    cardLayout.show(this, page.getPageName());
  }

  @Override
  public void initializeComponents() {
    mainPage = new MainAuthPage(navigationListener);
    loginPage = new LoginPage(navigationListener);
    registerPage = new RegisterPage(navigationListener);
  }

  @Override
  public void initializeLayout() {
    cardLayout = new CardLayout();

    setLayout(cardLayout);
  }

  @Override
  public void attachComponents() {
    // TODO Auto-generated method stub
    add(mainPage, mainPage.getPageName());
    add(loginPage, loginPage.getPageName());
    add(registerPage, registerPage.getPageName());

    System.out.println("Adding pages to AuthPage: " + mainPage.getPageName() + ", "
        + loginPage.getPageName() + ", " + registerPage.getPageName());
  }

  @Override
  public void setSize(Dimension d) {
    super.setSize(d);

    mainPage.setSize(d);
    loginPage.setSize(d);
    registerPage.setSize(d);
  }

  @Override
  public void setPreferredSize(Dimension d) {
    super.setPreferredSize(d);

    mainPage.setPreferredSize(d);
    loginPage.setPreferredSize(d);
    registerPage.setPreferredSize(d);
  }

  private class NavigationListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      SwingUtilities.invokeLater(() -> {
        var actionCommand = e.getActionCommand();

        if (actionCommand.equals("login")) {
          setVisiblePanel(Pages.LOGIN);
        } else if (actionCommand.equals("register")) {
          setVisiblePanel(Pages.REGISTER);
        } else if (actionCommand.equals("main")) {
          setVisiblePanel(Pages.MAIN);
        } else {
          throw new IllegalArgumentException("Unknown action command: " + actionCommand);
        }
      });
    }
  }
}
