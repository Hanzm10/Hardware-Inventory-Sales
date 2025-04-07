package com.murico.app.view.pages.auth;

import java.awt.event.ActionListener;
import com.murico.app.view.pages.Page;

public class RegisterPage extends Page implements Page.PageInterface {


  /**
   * 
   */
  private static final long serialVersionUID = 5843435349454356660L;

  ActionListener navigationListener;

  /**
   * Create the panel.
   * 
   * @param authPage
   */
  public RegisterPage(ActionListener navigationListener) {
    super();

    assert navigationListener != null : "Navigation listener cannot be null";

    this.navigationListener = navigationListener;

    initializeComponents();
    initializeLayout();
    attachComponents();
  }

  @Override
  public void initializeComponents() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void initializeLayout() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void attachComponents() {
    // TODO Auto-generated method stub
    
  }

}
