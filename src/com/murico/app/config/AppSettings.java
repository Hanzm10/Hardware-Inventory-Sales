package com.murico.app.config;

import java.awt.Color;
import java.awt.Font;
import java.util.Properties;

public class AppSettings {

  private static AppSettings instance;
  private final Properties properties;
  private String appTitle;
  private String appFontFamily;
  private int appMainScreenWidth;
  private int appMainScreenHeight;
  private Color primaryColor;
  private Color primaryForegroundColor;
  private Color secondaryColor;
  private Color secondaryForegroundColor;
  private Color transparentColor;
  private Color borderColor;
  private Color placeholderColor;
  private Font mainFont;
  private Font mainFontTitle;
  private Font mainFontSubtitle;
  private Font mainFontHeader;
  private Font mainFontBody;
  private Font mainFontCaption;
  private Font mainFontFootnote;
  private Font mainFontButton;
  private Font mainFontLink;
  private int baseBorderRadius;
  private int baseBorderWidth;
  private int baseBorderOffset;

  private AppSettings() {
    this.properties = new Properties();

    this.loadConfiguration();
    this.setConfigDefaults();
  }

  public static synchronized AppSettings getInstance() {
    if (instance == null) {
      instance = new AppSettings();
    }

    return instance;
  }

  public String getAppTitle() {
    return this.appTitle;
  }

  public int getAppMainScreenWidth() {
    return this.appMainScreenWidth;
  }

  public int getAppMainScreenHeight() {
    return this.appMainScreenHeight;
  }

  public Color getPrimaryColor() {
    return this.primaryColor;
  }

  public Color getPrimaryForegroundColor() {
    return this.primaryForegroundColor;
  }

  public Color getSecondaryColor() {
    return this.secondaryColor;
  }

  public Color getSecondaryForegroundColor() {
    return this.secondaryForegroundColor;
  }

  public Color getTransparentColor() {
    return this.transparentColor;
  }

  public Color getBorderColor() {
    return this.borderColor;
  }

  public Color getPlaceholderColor() {
    return this.placeholderColor;
  }

  public String getAppFontFamily() {
    return this.appFontFamily;
  }

  public Font getMainFont() {
    return this.mainFont;
  }

  public Font getMainFontTitle() {
    return this.mainFontTitle;
  }

  public Font getMainFontSubtitle() {
    return this.mainFontSubtitle;
  }

  public Font getMainFontHeader() {
    return this.mainFontHeader;
  }

  public Font getMainFontBody() {
    return this.mainFontBody;
  }

  public Font getMainFontCaption() {
    return this.mainFontCaption;
  }

  public Font getMainFontFootnote() {
    return this.mainFontFootnote;
  }

  public Font getMainFontLink() {
    return this.mainFontLink;
  }

  public Font getMainFontButton() {
    return this.mainFontButton;
  }

  public int getBaseBorderRadius() {
    return baseBorderRadius;
  }

  public int getBaseBorderWidth() {
    return baseBorderWidth;
  }

  public int getBaseBorderOffset() {
    return baseBorderOffset;
  }

  public String getProperty(String key) {
    return this.properties.getProperty(key);
  }

  public int getIntProperty(String key) throws NumberFormatException {
    return Integer.parseInt(this.getProperty(key));
  }

  private void setConfigDefaults() {
    this.appTitle = this.getProperty("app.title");

    this.appMainScreenWidth = this.getIntProperty("app.screen.width");
    this.appMainScreenHeight = this.getIntProperty("app.screen.height");
    this.appFontFamily = this.getProperty("app.font.family");

    this.primaryColor = Color.decode(this.getProperty("color.primary.default"));
    this.primaryForegroundColor = Color.decode(this.getProperty("color.primary.foreground"));

    this.secondaryColor = Color.decode(this.getProperty("color.secondary.default"));
    this.secondaryForegroundColor = Color.decode(this.getProperty("color.secondary.foreground"));

    var transparentColor = Color.decode(this.getProperty("color.transparent.default"));

    this.transparentColor = new Color(transparentColor.getRed(), transparentColor.getGreen(),
        transparentColor.getBlue(), 0);

    this.borderColor = Color.decode(this.getProperty("color.border.default"));
    this.placeholderColor = Color.decode(this.getProperty("color.placeholder.default"));

    var defaultFontSize = this.getIntProperty("app.font.size.default");
    var defaultFontSizeTitle = this.getIntProperty("app.font.size.title");
    var defaultFontSizeSubtitle = this.getIntProperty("app.font.size.subtitle");
    var defaultFontSizeHeader = this.getIntProperty("app.font.size.header");
    var defaultFontSizeBody = this.getIntProperty("app.font.size.body");
    var defaultFontSizeCaption = this.getIntProperty("app.font.size.caption");
    var defaultFontSizeFootnote = this.getIntProperty("app.font.size.footnote");
    var defaultFontSizeLink = this.getIntProperty("app.font.size.link");
    var defaultFontSizeButton = this.getIntProperty("app.font.size.button");

    this.mainFont = new Font(this.appFontFamily, Font.PLAIN, defaultFontSize);
    this.mainFontTitle = new Font(this.appFontFamily, Font.BOLD, defaultFontSizeTitle);
    this.mainFontSubtitle = new Font(this.appFontFamily, Font.BOLD, defaultFontSizeSubtitle);
    this.mainFontHeader = new Font(this.appFontFamily, Font.BOLD, defaultFontSizeHeader);
    this.mainFontBody = new Font(this.appFontFamily, Font.PLAIN, defaultFontSizeBody);
    this.mainFontCaption = new Font(this.appFontFamily, Font.PLAIN, defaultFontSizeCaption);
    this.mainFontFootnote = new Font(this.appFontFamily, Font.PLAIN, defaultFontSizeFootnote);
    this.mainFontLink = new Font(this.appFontFamily, Font.ITALIC, defaultFontSizeLink);
    this.mainFontButton = new Font(this.appFontFamily, Font.BOLD, defaultFontSizeButton);

    this.baseBorderRadius = this.getIntProperty("app.border.radius");
    this.baseBorderWidth = this.getIntProperty("app.border.width");
    this.baseBorderOffset = this.getIntProperty("app.border.offset");
  }

  private void loadConfiguration() {
    var configuration =
        AppSettings.class.getClassLoader().getResourceAsStream("config.properties");

    assert configuration != null : "Configuration file not found";

    try {
      this.properties.load(configuration);
    } catch (Exception e) {
      System.err.println("Error loading configuration file: " + e.getMessage());
      System.exit(1);
    }
  }
}
