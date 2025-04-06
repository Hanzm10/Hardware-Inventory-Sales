package com.murico.app.view.components.inputs.listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.SwingUtilities;
import com.murico.app.config.UISettings;
import com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface;

public class MTextFieldFocusListener implements FocusListener {
  @Override
  public void focusGained(FocusEvent e) {
    SwingUtilities.invokeLater(() -> {
      var focusedComponent = e.getSource();
      
      if (focusedComponent instanceof RoundedCornerBorderComponentInterface component) {
        component.setBorderColor(UISettings.getInstance().getUIColor().getPrimaryColor());
      }
    });
  }

  @Override
  public void focusLost(FocusEvent e) {
    SwingUtilities.invokeLater(() -> {
      var focusedComponent = e.getSource();

      if (focusedComponent instanceof RoundedCornerBorderComponentInterface component) {
        component.setBorderColor(UISettings.getInstance().getUIColor().getBorderColor());
      }
    });
  }
}
