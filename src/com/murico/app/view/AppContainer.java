package com.murico.app.view;

import java.awt.Dimension;
import com.murico.app.config.UISettings;
import com.murico.app.view.common.containers.ContainerPanel;
import com.murico.app.view.pages.auth.LoginPage;

public class AppContainer extends ContainerPanel {
  /**
   * 
   */
  private static final long serialVersionUID = -2591977387415502564L;

  public AppContainer() {
    super();

    initializeContainerSize();
    add(new LoginPage());

    System.out.println(this.getBackground());
  }

  private void initializeContainerSize() {
    var size = new Dimension(UISettings.getInstance().getUIDisplay().getPreferredWidth(),
        UISettings.getInstance().getUIDisplay().getPreferredHeight()
    );

    setSize(size);
    setPreferredSize(size);
  }
}
