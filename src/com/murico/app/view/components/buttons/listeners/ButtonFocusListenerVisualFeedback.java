package com.murico.app.view.components.buttons.listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.SwingUtilities;
import com.murico.app.view.borders.rounded.RoundedCornerBorderComponentInterface;
import com.murico.app.view.components.buttons.MButtonInterface;

public class ButtonFocusListenerVisualFeedback implements FocusListener {
  @Override
  public void focusGained(FocusEvent e) {
    SwingUtilities.invokeLater(() -> {
      var focusedComponent = e.getSource();

      if (focusedComponent instanceof MButtonInterface
          && focusedComponent instanceof RoundedCornerBorderComponentInterface button) {
        button.getRoundedCornerBorder().setDrawBorder(true);
        button.repaintBorder();
      }
    });
  }

  @Override
  public void focusLost(FocusEvent e) {
    SwingUtilities.invokeLater(() -> {
      var focusedComponent = e.getSource();

      if (focusedComponent instanceof MButtonInterface
          && focusedComponent instanceof RoundedCornerBorderComponentInterface button) {
        button.getRoundedCornerBorder().setDrawBorder(false);
        button.repaintBorder();
      }
    });
  }
}
