package com.murico.app.view;

public enum CurrentPage {
  MAIN,
  LOGIN,
  REGISTER;

  private static CurrentPage currentPage = LOGIN;

  public static CurrentPage getCurrentPage() {
    return currentPage;
  }

  public static void setCurrentPage(CurrentPage page) {
    currentPage = page;
  }

  public static CurrentPage fromString(String page) {
    return switch (page) {
      case "LOGIN" -> LOGIN;
      case "REGISTER" -> REGISTER;
      default -> MAIN;
    };
  }
}
