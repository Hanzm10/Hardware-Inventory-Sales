package com.murico.app.view;

import java.awt.Dimension;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.murico.app.config.UISettings;
import com.murico.app.managers.PageManager;
import com.murico.app.view.pages.auth.AuthPage;
import com.murico.app.view.pages.dashboard.DashboardPage;
import com.murico.app.view.pages.profile.ProfilePage;
import com.murico.app.view.pages.settings.SettingsPage;

public class AppContainer extends JPanel {
  /**
   * 
   */
  private static final long serialVersionUID = -2591977387415502564L;

  public AppContainer() {
    super();

    var size = new Dimension(UISettings.getInstance().getUIDisplay().getPreferredWidth(),
        UISettings.getInstance().getUIDisplay().getPreferredHeight() - 50);

    setLayout(new FormLayout("fill:default:grow", "fill:default:grow"));
    setSize(size);
    setPreferredSize(size);

    render();
  }

  private void render() {
    removeAll();

    var currPage = PageManager.getInstance().getCurrentPage();

    if (currPage.equals(PageManager.AUTH)) {
      add(new AuthPage(), "1, 1, fill, fill");
    } else if (currPage.equals(PageManager.DASHBOARD)) {
      add(new DashboardPage(), "1, 1, fill, fill");
    } else if (currPage.equals(PageManager.SETTINGS)) {
      add(new SettingsPage(), "1, 1, fill, fill");
    } else if (currPage.equals(PageManager.PROFILE)) {
      add(new ProfilePage(), "1, 1, fill, fill");
    } else {
      throw new IllegalStateException("Unknown page: " + currPage);
    }
  }

}
