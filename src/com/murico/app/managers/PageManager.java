package com.murico.app.managers;

import java.util.ArrayList;
import com.murico.app.view.utilities.Observer;

public class PageManager {
  public static final String AUTH = "AUTH";
  public static final String DASHBOARD = "DASHBOARD";
  public static final String SETTINGS = "SETTINGS";
  public static final String PROFILE = "PROFILE";
  private static PageManager instance;

  private final ArrayList<String> protectedPages = new ArrayList<>();
  private final ArrayList<String> neutralPages = new ArrayList<>();
  private final ArrayList<Observer<String>> observers = new ArrayList<>();

  private String currentPage = AUTH;

  private PageManager() {
    // Private constructor to prevent instantiation
    protectedPages.add(DASHBOARD);
    protectedPages.add(PROFILE);
    neutralPages.add(SETTINGS);
  }

  public void addObserver(Observer<String> observer) {
    if (observer == null) {
      throw new IllegalArgumentException("Observer cannot be null");
    }

    observers.add(observer);
  }

  public void removeObserver(Observer<String> observer) {
    if (observer == null) {
      throw new IllegalArgumentException("Observer cannot be null");
    }

    observers.remove(observer);
  }

  public String getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(String page) {
    if (page == null) {
      throw new IllegalArgumentException("CurrentPage cannot be null");
    }

    var isLoggedIn = UserSessionManager.getInstance().isLoggedIn();
    var isProtectedPage = isProtectedPage(page);

    if (isProtectedPage && !isLoggedIn) {
      throw new IllegalStateException("User must be logged in to access this page");
    }

    if (!isProtectedPage && !isNeutralPage(page) && isLoggedIn) {
      throw new IllegalStateException("User must be logged out to access this page");
    }

    for (Observer<String> observer : observers) {
      observer.update(page);
    }

    currentPage = page;
  }

  private boolean isProtectedPage(String page) {
    for (String protectedPage : protectedPages) {
      if (page.equals(protectedPage)) {
        return true;
      }
    }

    return false;
  }

  private boolean isNeutralPage(String page) {
    for (String neutralPage : neutralPages) {
      if (page.equals(neutralPage)) {
        return true;
      }
    }

    return false;
  }

  public static synchronized PageManager getInstance() {
    if (instance == null) {
      instance = new PageManager();
    }

    return instance;
  }
}
