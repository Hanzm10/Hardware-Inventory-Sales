package com.murico.app.view.components.inputs;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.murico.app.view.common.containers.ContainerPanel;
import com.murico.app.view.components.buttons.MToggleButton;

public class MPasswordFieldToggleable extends ContainerPanel {

  private static final long serialVersionUID = 1L;
  private MPasswordField passwordField;
  private MToggleButton tglbtnT;

  /**
   * Create the panel.
   */
  public MPasswordFieldToggleable() {
    super();

    setLayout(new FormLayout(
        new ColumnSpec[] {
            new ColumnSpec(ColumnSpec.FILL,
                Sizes.bounded(Sizes.PREFERRED, Sizes.constant("225px", true),
                    Sizes.constant("300px", true)),
                1),
            new ColumnSpec(ColumnSpec.FILL,
                Sizes.bounded(Sizes.PREFERRED, Sizes.constant("50px", true),
                    Sizes.constant("75px", true)),
                0),},
        new RowSpec[] {new RowSpec(RowSpec.CENTER, Sizes.bounded(Sizes.PREFERRED,
            Sizes.constant("35px", false), Sizes.constant("75px", false)), 1),}));

    passwordField = new MPasswordField();
    add(passwordField, "1, 1, default, fill");

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
    add(tglbtnT, "2, 1, fill, fill");
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
