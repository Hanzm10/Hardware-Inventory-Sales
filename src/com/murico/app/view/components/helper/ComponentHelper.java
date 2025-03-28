package com.murico.app.view.components.helper;

import java.awt.*;

public record ComponentHelper<C extends Component>(C component) {
  public void setCursorToHand() {
    this.component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public void setCursorToDefault() {
    this.component.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }
}
