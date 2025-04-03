package com.murico.app.view.pages.items;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

class ItemList extends JPanel {
  /**
   * 
   */
  private static final long serialVersionUID = 7050202711977008692L;

  public ItemList() {
    setLayout(new FormLayout(
        new ColumnSpec[] {ColumnSpec.decode("224px"), ColumnSpec.decode("2px"),
            FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),},
        new RowSpec[] {FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("96px"),}));


  }
}
