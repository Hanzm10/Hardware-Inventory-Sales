package com.murico.app.view.components.inputs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.murico.app.view.components.buttons.MToggleButton;

public class MPasswordFieldToggleable extends JPanel {

  private static final long serialVersionUID = 1L;
  private MPasswordField passwordField;
  private MToggleButton tglbtnT;

  /**
   * Create the panel.
   */
  public MPasswordFieldToggleable() {
    setBackground(new Color(255, 255, 255));
    var gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {300, 53, 0};
    gridBagLayout.rowHeights = new int[] {83, 0};
    gridBagLayout.columnWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[] {1.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);

    passwordField = new MPasswordField();
    var gbc_passwordField = new GridBagConstraints();
    gbc_passwordField.ipadx = 1;
    gbc_passwordField.fill = GridBagConstraints.BOTH;
    gbc_passwordField.gridx = 0;
    gbc_passwordField.gridy = 0;
    add(passwordField, gbc_passwordField);

    tglbtnT = new MToggleButton();
    tglbtnT.setSelectedIcon(
        new ImageIcon(MPasswordFieldToggleable.class.getResource("/assets/icons/eye-closed.png")));
    tglbtnT.addItemListener(new TglbtnTItem());
    tglbtnT.setBorderTopRightRadius(8);
    tglbtnT.setBorderTopLeftRadius(8);
    tglbtnT.setBorderBottomRightRadius(8);
    tglbtnT.setBorderBottomLeftRadius(8);
    tglbtnT.setMaximumSize(new Dimension(28, 28));
    tglbtnT.setMinimumSize(new Dimension(28, 28));
    tglbtnT.setPreferredSize(new Dimension(28, 28));
    tglbtnT.setIcon(
        new ImageIcon(MPasswordFieldToggleable.class.getResource("/assets/icons/eye.png")));
    tglbtnT.setText("");
    var gbc_tglbtnT = new GridBagConstraints();
    gbc_tglbtnT.fill = GridBagConstraints.BOTH;
    gbc_tglbtnT.gridx = 1;
    gbc_tglbtnT.gridy = 0;
    add(tglbtnT, gbc_tglbtnT);
    
  }

  private class TglbtnTItem implements ItemListener {
    @Override
    public void itemStateChanged(ItemEvent e) {
      SwingUtilities.invokeLater(() -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          passwordField.setEchoChar((char) 0);
        } else {
         passwordField.setEchoChar('*');
        }
      });
    }
  }
}
