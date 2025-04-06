package com.murico.app.view.common.containers;

import javax.swing.JPanel;
import com.murico.app.config.UISettings;

/**
 * ContainerPanel is a custom JPanel that sets the default background and foreground colors based on
 * the {@link UISettings}.
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class ContainerPanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 8644518483935220292L;

  public ContainerPanel() {
    super();

    setBackground(UISettings.getInstance().getUIColor().getBackgroundColor());
    setForeground(UISettings.getInstance().getUIColor().getForegroundColor());
  }
}
