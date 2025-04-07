package com.murico.app.view.builder;

import com.jgoodies.forms.layout.RowSpec;

public class RowSpecBuilder {
  private String rowSpec;

  public RowSpecBuilder() {
    rowSpec = "";
  }

  public RowSpecBuilder addRow(String row) {
    if (!rowSpec.isEmpty()) {
      rowSpec += ", ";
    }

    rowSpec += row;

    return this;
  }

  public String build() {
    var r = rowSpec.trim();

    rowSpec = "";

    return r;
  }

  public RowSpec buildRowSpec() {
    return RowSpec.decode(build());
  }
}
