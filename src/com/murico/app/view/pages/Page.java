package com.murico.app.view.pages;

import javax.swing.JPanel;

public class Page extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 2636344014639010695L;

  public Page() {
    super();
  }

  public String getPageName() {
    return this.getClass().getSimpleName();
  }

  public interface PageInterface {
    public void initializeComponents();

    public void initializeLayout();

    public void attachComponents();
  }

}
