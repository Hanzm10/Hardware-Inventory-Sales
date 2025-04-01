package com.murico.app.view.components.buttons.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import com.murico.app.view.components.buttons.MButton;
import com.murico.app.view.components.buttons.MButtonInterface;
import com.murico.app.view.components.buttons.MToggleButton;
import com.murico.app.view.components.buttons.variations.MButtonColorVariations;
import com.murico.app.view.utilities.RenderingUtilities;

/**
 * This class is used to provide visual feedback for mouse events on buttons. It implements the
 * MouseListener interface
 */
public class ButtonMouseListenerVisualFeedback extends MouseAdapter {
  private void mousePressedMButton(MButton button) {
    button.setPressed(true);

    var alpha = button.getColorVariation() == MButtonColorVariations.TRANSPARENT
        ? MButtonInterface.ALPHA_PRESSED_TRANSPARENT
        : MButtonInterface.ALPHA_PRESSED_SOLID;

    button
        .setBackground(RenderingUtilities.RgbColorToColorWithAlpha(button.getBackground(), alpha));
  }

  private void mousePressedMToggleButton(MToggleButton button) {
    button.setPressed(true);

    var val = button.isHovered() ? MButtonInterface.ALPHA_PRESSED_TOGGLED_HOVERED
        : MButtonInterface.ALPHA_PRESSED_TOGGLED;
    var alpha = button.isSelected() ? val : MButtonInterface.ALPHA_PRESSED_NOT_TOGGLED;

    button
        .setBackground(RenderingUtilities.RgbColorToColorWithAlpha(button.getBackground(), alpha));
  }

  private void mouseReleasedMButton(MButton button) {
    button.setPressed(false);

    int alpha;

    if (!button.isHovered()) {
      RenderingUtilities.setCursorToDefaultCursor(button);

      alpha = button.getColorVariation() == MButtonColorVariations.TRANSPARENT
          ? MButtonInterface.ALPHA_TRANSPARENT
          : MButtonInterface.ALPHA_SOLID;
    } else {
      alpha = button.getColorVariation() == MButtonColorVariations.TRANSPARENT
          ? MButtonInterface.ALPHA_HOVERED_TRANSPARENT
          : MButtonInterface.ALPHA_HOVERED_SOLID;
    }

    button
        .setBackground(RenderingUtilities.RgbColorToColorWithAlpha(button.getBackground(), alpha));
  }

  private void mouseReleasedMToggleButton(MToggleButton button) {
    button.setPressed(false);

    int alpha;

    if (!button.isHovered()) {
      RenderingUtilities.setCursorToDefaultCursor(button);

      alpha =
          button.isSelected() ? MButtonInterface.ALPHA_TOGGLED : MButtonInterface.ALPHA_NOT_TOGGLED;
    } else {
      alpha = button.isSelected() ? MButtonInterface.ALPHA_TOGGLED_HOVERED
          : MButtonInterface.ALPHA_NOT_TOGGLED_HOVERED;
    }

    button
        .setBackground(RenderingUtilities.RgbColorToColorWithAlpha(button.getBackground(), alpha));
  }

  private void mouseEnteredMButton(MButton button) {
    button.setHovered(true);

    if (button.isPressed()) {
      return;
    }

    RenderingUtilities.setCursorToHandCursor(button);

    var alpha = button.getColorVariation() == MButtonColorVariations.TRANSPARENT
        ? MButtonInterface.ALPHA_HOVERED_TRANSPARENT
        : MButtonInterface.ALPHA_HOVERED_SOLID;

    button
        .setBackground(RenderingUtilities.RgbColorToColorWithAlpha(button.getBackground(), alpha));
  }

  private void mouseEnteredMToggleButton(MToggleButton button) {
    button.setHovered(true);

    if (button.isPressed()) {
      return;
    }

    RenderingUtilities.setCursorToHandCursor(button);

    var alpha = button.isSelected() ? MButtonInterface.ALPHA_TOGGLED_HOVERED
        : MButtonInterface.ALPHA_NOT_TOGGLED_HOVERED;

    button
        .setBackground(RenderingUtilities.RgbColorToColorWithAlpha(button.getBackground(), alpha));
  }

  public void mouseExitedMButton(MButton button) {
    button.setHovered(false);

    if (button.isPressed()) {
      return;
    }

    RenderingUtilities.setCursorToDefaultCursor(button);

    var alpha = button.getColorVariation() == MButtonColorVariations.TRANSPARENT
        ? MButtonInterface.ALPHA_TRANSPARENT
        : MButtonInterface.ALPHA_SOLID;

    button
        .setBackground(RenderingUtilities.RgbColorToColorWithAlpha(button.getBackground(), alpha));
  }

  private void mouseExitedMToggleButton(MToggleButton button) {
    button.setHovered(false);

    if (button.isPressed()) {
      return;
    }

    RenderingUtilities.setCursorToDefaultCursor(button);

    var alpha =
        button.isSelected() ? MButtonInterface.ALPHA_TOGGLED : MButtonInterface.ALPHA_NOT_TOGGLED;

    button
        .setBackground(RenderingUtilities.RgbColorToColorWithAlpha(button.getBackground(), alpha));
  }

  @Override
  public void mousePressed(MouseEvent e) {
    SwingUtilities.invokeLater(() -> {
      var clickedComponent = e.getSource();

      if (clickedComponent instanceof MButton button) {
        mousePressedMButton(button);
      } else if (clickedComponent instanceof MToggleButton button) {
        mousePressedMToggleButton(button);
      }
    });
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    SwingUtilities.invokeLater(() -> {
      var clickedComponent = e.getSource();

      if (clickedComponent instanceof MButton button) {
        mouseReleasedMButton(button);
      } else if (clickedComponent instanceof MToggleButton button) {
        mouseReleasedMToggleButton(button);
      }
    });
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    SwingUtilities.invokeLater(() -> {
      var clickedComponent = e.getSource();

      if (clickedComponent instanceof MButton button) {
        mouseEnteredMButton(button);
      } else if (clickedComponent instanceof MToggleButton button) {
        mouseEnteredMToggleButton(button);
      }
    });
  }

  @Override
  public void mouseExited(MouseEvent e) {
    SwingUtilities.invokeLater(() -> {
      var clickedComponent = e.getSource();

      if (clickedComponent instanceof MButton button) {
        mouseExitedMButton(button);
      } else if (clickedComponent instanceof MToggleButton button) {
        mouseExitedMToggleButton(button);
      }
    });
  }

}
