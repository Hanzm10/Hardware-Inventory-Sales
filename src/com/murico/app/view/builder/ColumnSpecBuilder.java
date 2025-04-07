package com.murico.app.view.builder;

import com.jgoodies.forms.layout.ColumnSpec;

public class ColumnSpecBuilder {
  private String columnSpec;

  public ColumnSpecBuilder() {
    columnSpec = "";
  }

  public ColumnSpecBuilder addColumn(String column) {
    if (!columnSpec.isEmpty()) {
      columnSpec += ", ";
    }

    columnSpec += column;

    return this;
  }

  public String build() {
    var c = columnSpec.trim();

    columnSpec = "";

    return c;
  }

  public ColumnSpec buildColumnSpec() {
    return ColumnSpec.decode(build());
  }
}
