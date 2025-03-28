package com.murico.app.controller.ui.buttons;

import com.murico.app.view.components.buttons.MButton;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class MButtonMouseListener implements MouseListener {
  private final MButton btn;

  public MButtonMouseListener(MButton btn) {
    this.btn = btn;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    SwingUtilities.invokeLater(this.btn::mouseClicked);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    SwingUtilities.invokeLater(this.btn::mousePressed);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    SwingUtilities.invokeLater(this.btn::mouseReleased);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    SwingUtilities.invokeLater(this.btn::mouseEntered);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    SwingUtilities.invokeLater(this.btn::mouseExited);
  }
}
