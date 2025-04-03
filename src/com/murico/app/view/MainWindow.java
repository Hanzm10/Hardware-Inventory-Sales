package com.murico.app.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.jgoodies.forms.layout.FormLayout;
import com.murico.app.Murico;
import com.murico.app.config.AppSettings;
import com.murico.app.view.pages.auth.LoginPage;
import com.murico.app.view.pages.auth.MainAuthPage;
import com.murico.app.view.pages.auth.RegisterPage;

public class MainWindow extends JPanel implements ComponentListener {
  /**
   * 
   */
  private static final long serialVersionUID = -2591977387415502564L;
  private final Murico murico;

  private boolean isFirstRender = true;
  private boolean isFirstLoad = true;

  private CurrentPage previouslyRenderedPage;

  public MainWindow(Murico murico) {
    this.murico = murico;


    setLayout(new FormLayout("default:grow", "default:grow"));
    setBackground(new Color(255, 255, 255));

    addComponentListener(this);

    setPanelSize();
    render();
  }

  public Murico getMurico() {
    return murico;
  }

  public void render() {
    var currentPage = CurrentPage.getCurrentPage();

    if (isFirstLoad) {
      isFirstLoad = false;
      return;
    } else {
      if (!isFirstRender && previouslyRenderedPage != null
          && previouslyRenderedPage != currentPage) {
        removeAll();
      } else {
        isFirstRender = false;
      }
    } 

    switch (currentPage) {
      case MAIN:
        if (previouslyRenderedPage == currentPage) {

        } else {
          var mainAuthPage = new MainAuthPage(this);
          add(mainAuthPage, "1, 1, center, center");
        }
        break;
      case LOGIN:
        if (previouslyRenderedPage == currentPage) {

        } else {
          var loginPage = new LoginPage(this);
          add(loginPage, "1, 1, fill, center");
        }
        break;
      case REGISTER:
        if (previouslyRenderedPage == currentPage) {

        } else {
          var registerPage = new RegisterPage(this);
          add(registerPage, "1, 1, fill, center");
        }
        break;
    }
    
    previouslyRenderedPage = currentPage;

    revalidate();
    repaint();
  }

  private void setPanelSize() {
    var size =
        new Dimension(AppSettings.getInstance().getAppDisplaySettings().getAppMainScreenWidth(),
        AppSettings.getInstance().getAppDisplaySettings().getAppMainScreenHeight());

    this.setPreferredSize(size);
  }

  @Override
  public void componentResized(ComponentEvent e) {
    SwingUtilities.invokeLater(() -> {
      render();
    });
  }

  @Override
  public void componentMoved(ComponentEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void componentShown(ComponentEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void componentHidden(ComponentEvent e) {
    // TODO Auto-generated method stub

  }
}
