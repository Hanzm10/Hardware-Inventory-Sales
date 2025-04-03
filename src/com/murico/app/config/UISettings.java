package com.murico.app.config;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import com.murico.app.utils.io.FileLoader;

// TODO: Update values in cache when properties are changed
public class UISettings extends AbstractSettings {
  private static UISettings instance;

  private final Properties defaultProperties;

  private final String propertiesFilePath =
      FileLoader.getConfigurationDirectory() + File.separator + "ui.properties";

  private final UIFont uiFont;
  private final UIColors uiColor;
  private final UISpace uiSpace;
  private final UIBorder uiBorder;

  private UISettings() {
    super();

    defaultProperties = new Properties();

    try {
      // Load default properties from the configuration directory
      FileLoader.loadFileFromResourcesToProperties("ui.properties", defaultProperties);
      FileLoader.loadFileFromConfigurationDirectoryToProperties("ui.properties", properties);
    } catch (Exception e) {
      e.printStackTrace();
    }

    uiFont = new UIFont();
    uiColor = new UIColors();
    uiSpace = new UISpace();
    uiBorder = new UIBorder();
  }

  private class UIBorder {
    private int borderRadius;
    private int borderWidth;
    private int borderOffset;

    private UIBorder() {
      this.borderRadius = getIntProperty("border.radius");
      this.borderWidth = getIntProperty("border.width");
      this.borderOffset = getIntProperty("border.offset");
    }

    public int getBorderRadius() {
      return borderRadius;
    }

    public int getBorderWidth() {
      return borderWidth;
    }
    
    public int getBorderOffset() {
      return borderOffset;
    }

    /**
     * Set the border width. The width must be greater than 0.
     * 
     * @param borderRadius
     * @throws AssertionError if the width is less than or equal to 0
     */
    public void setBorderRadius(int borderRadius) throws AssertionError {
      assert borderRadius > 0 : "Border radius must be greater than 0";

      this.borderRadius = borderRadius;
    }
    
    /**
     * Set the border width. The width must be greater than 0.
     * 
     * @param borderWidth
     * @throws AssertionError if the width is less than or equal to 0
     */
    public void setBorderWidth(int borderWidth) throws AssertionError {
      assert borderWidth > 0 : "Border width must be greater than 0";

      this.borderWidth = borderWidth;
    }
    
    /**
     * Set the border offset. The offset must be greater than or equal to 0.
     * 
     * @param borderOffset
     * @throws AssertionError if the offset is less than 0
     */
    public void setBorderOffset(int borderOffset) throws AssertionError {
      assert borderOffset >= 0 : "Border offset must be greater than or equal to 0";

      this.borderOffset = borderOffset;
    }
  }

  private class UISpace {
    private String spaceXXS;
    private String spaceXS;
    private String spaceS;
    private String spaceM;
    private String spaceL;
    private String spaceXL;
    private String spaceXXL;
    private String spaceXXXL;
    private String spaceXXXXL;
    private String spaceXXXXXL;

    public UISpace() {
      this.spaceXXS = getProperty("app.space.scale.xxs");
      this.spaceXS = getProperty("app.space.scale.xs");
      this.spaceS = getProperty("app.space.scale.s");
      this.spaceM = getProperty("app.space.scale.m");
      this.spaceL = getProperty("app.space.scale.l");
      this.spaceXL = getProperty("app.space.scale.xl");
      this.spaceXXL = getProperty("app.space.scale.xxl");
      this.spaceXXXL = getProperty("app.space.scale.xxxl");
      this.spaceXXXXL = getProperty("app.space.scale.xxxxl");
      this.spaceXXXXXL = getProperty("app.space.scale.xxxxxl");

    }

    public String getSpaceXXS() {
      return spaceXXS;
    }

    public String getSpaceXS() {
      return spaceXS;
    }

    public String getSpaceS() {
      return spaceS;
    }

    public String getSpaceM() {
      return spaceM;
    }

    public String getSpaceL() {
      return spaceL;
    }

    public String getSpaceXL() {
      return spaceXL;
    }

    public String getSpaceXXL() {
      return spaceXXL;
    }

    public String getSpaceXXXL() {
      return spaceXXXL;
    }

    public String getSpaceXXXXL() {
      return spaceXXXXL;
    }

    public String getSpaceXXXXXL() {
      return spaceXXXXXL;
    }
  }

  public class UIFont {
    private String fontFamily;
    private Font h1Font;
    private Font h2Font;
    private Font h3Font;
    private Font h4Font;
    private Font h5Font;
    private Font h6Font;
    private Font bodyFont;
    private Font buttonFont;
    private Font captionFont;

    private UIFont() {
      this.fontFamily = getProperty("font.family");
      this.h1Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h1"));
      this.h2Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h2"));
      this.h3Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h3"));
      this.h4Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h4"));
      this.h5Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h5"));
      this.h6Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h6"));
      this.bodyFont = new Font(fontFamily, Font.PLAIN, getIntProperty("font.size.body"));
      this.buttonFont = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.button"));
      this.captionFont = new Font(fontFamily, Font.ITALIC, getIntProperty("font.size.caption"));
    }

    public Font getH1Font() {
      return h1Font;
    }

    public Font getH2Font() {
      return h2Font;
    }

    public Font getH3Font() {
      return h3Font;
    }

    public Font getH4Font() {
      return h4Font;
    }

    public Font getH5Font() {
      return h5Font;
    }

    public Font getH6Font() {
      return h6Font;
    }

    public Font getBodyFont() {
      return bodyFont;
    }

    public Font getButtonFont() {
      return buttonFont;
    }

    public Font getCaptionFont() {
      return captionFont;
    }

    public String getFontFamily() {
      return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
      this.fontFamily = fontFamily;
      h1Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h1"));
      h2Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h2"));
      h3Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h3"));
      h4Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h4"));
      h5Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h5"));
      h6Font = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.h6"));
      bodyFont = new Font(fontFamily, Font.PLAIN, getIntProperty("font.size.body"));
      buttonFont = new Font(fontFamily, Font.BOLD, getIntProperty("font.size.button"));
      captionFont = new Font(fontFamily, Font.ITALIC, getIntProperty("font.size.caption"));
    }

    public void setH1Font(Font h1Font) {
      this.h1Font = h1Font;
    }

    public void setH2Font(Font h2Font) {
      this.h2Font = h2Font;
    }

    public void setH3Font(Font h3Font) {
      this.h3Font = h3Font;
    }

    public void setH4Font(Font h4Font) {
      this.h4Font = h4Font;
    }

    public void setH5Font(Font h5Font) {
      this.h5Font = h5Font;
    }

    public void setH6Font(Font h6Font) {
      this.h6Font = h6Font;
    }

    public void setBodyFont(Font bodyFont) {
      this.bodyFont = bodyFont;
    }

    public void setButtonFont(Font buttonFont) {
      this.buttonFont = buttonFont;
    }

    public void setCaptionFont(Font captionFont) {
      this.captionFont = captionFont;
    }

    public void setFontSizeH1(int size) {
      this.h1Font = new Font(fontFamily, Font.BOLD, size);
    }

    public void setFontSizeH2(int size) {
      this.h2Font = new Font(fontFamily, Font.BOLD, size);
    }

    public void setFontSizeH3(int size) {
      this.h3Font = new Font(fontFamily, Font.BOLD, size);
    }

    public void setFontSizeH4(int size) {
      this.h4Font = new Font(fontFamily, Font.BOLD, size);
    }

    public void setFontSizeH5(int size) {
      this.h5Font = new Font(fontFamily, Font.BOLD, size);
    }

    public void setFontSizeH6(int size) {
      this.h6Font = new Font(fontFamily, Font.BOLD, size);
    }

    public void setFontSizeBody(int size) {
      this.bodyFont = new Font(fontFamily, Font.PLAIN, size);
    }

    public void setFontSizeButton(int size) {
      this.buttonFont = new Font(fontFamily, Font.BOLD, size);
    }

    public void setFontSizeCaption(int size) {
      this.captionFont = new Font(fontFamily, Font.ITALIC, size);
    }
  }

  public class UIColors {
    private Color backgroundColor;
    private Color backgroundColorDark;
    private Color primaryColor;
    private Color primaryForegroundColor;
    private Color primaryColorDark;
    private Color primaryForegroundColorDark;
    private Color secondaryColor;
    private Color secondaryForegroundColor;
    private Color secondaryColorDark;
    private Color secondaryForegroundColorDark;
    private Color transparentColor;
    private Color transparentColorDark;
    private Color borderColor;
    private Color borderColorDark;
    private Color placeholderColor;
    private Color placeholderColorDark;

    public UIColors() {
      this.backgroundColor = Color.decode(getProperty("color.background"));
      this.backgroundColorDark = Color.decode(getProperty("color.dark.background"));
      this.primaryColor = Color.decode(getProperty("color.primary.default"));
      this.primaryForegroundColor = Color.decode(getProperty("color.primary.foreground"));
      this.primaryColorDark = Color.decode(getProperty("color.dark.primary.default"));
      this.primaryForegroundColorDark = Color.decode(getProperty("color.dark.primary.foreground"));
      this.secondaryColor = Color.decode(getProperty("color.secondary.default"));
      this.secondaryForegroundColor = Color.decode(getProperty("color.secondary.foreground"));
      this.secondaryColorDark = Color.decode(getProperty("color.dark.secondary.default"));
      this.secondaryForegroundColorDark = Color.decode(getProperty("color.dark.secondary.foreground"));
      this.transparentColor = Color.decode(getProperty("color.transparent.default"));
      this.transparentColorDark = Color.decode(getProperty("color.dark.transparent.default"));
      this.borderColor = Color.decode(getProperty("color.border.default"));
      this.borderColorDark = Color.decode(getProperty("color.dark.border.default"));
      this.placeholderColor = Color.decode(getProperty("color.placeholder.default"));
      this.placeholderColorDark = Color.decode(getProperty("color.dark.placeholder.default"));
    }

    public Color getBackgroundColor() {
      return backgroundColor;
    }

    public Color getBackgroundColorDark() {
      return backgroundColorDark;
    }

    public Color getPrimaryColor() {
      return primaryColor;
    }

    public Color getPrimaryForegroundColor() {
      return primaryForegroundColor;
    }

    public Color getPrimaryColorDark() {
      return primaryColorDark;
    }

    public Color getPrimaryForegroundColorDark() {
      return primaryForegroundColorDark;
    }

    public Color getSecondaryColor() {
      return secondaryColor;
    }

    public Color getSecondaryForegroundColor() {
      return secondaryForegroundColor;
    }

    public Color getSecondaryColorDark() {
      return secondaryColorDark;
    }

    public Color getSecondaryForegroundColorDark() {
      return secondaryForegroundColorDark;
    }

    public Color getTransparentColor() {
      return transparentColor;
    }

    public Color getTransparentColorDark() {
      return transparentColorDark;
    }

    public Color getBorderColor() {
      return borderColor;
    }

    public Color getBorderColorDark() {
      return borderColorDark;
    }

    public Color getPlaceholderColor() {
      return placeholderColor;
    }

    public Color getPlaceholderColorDark() {
      return placeholderColorDark;
    }

    /**
     * Set the background color. The color should be in hex format (e.g., #FFFFFF).
     * 
     * @param backgroundColor the background color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setBackgroundColor(String backgroundColor) throws AssertionError {
      assert backgroundColor != null : "Background color cannot be null";

      var backgroundC = Color.decode(backgroundColor);

      assert backgroundC != null : "Invalid background color format";

      this.backgroundColor = backgroundC;
    }

    /**
     * Set the background color for dark theme. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param backgroundColor the background color in hex formaColor.decode(backgroundColor)t
     * @throws AssertionError if the color is null or invalid
     */
    public void setBackgroundColorDark(String backgroundColorDark) throws AssertionError {
      assert backgroundColorDark != null : "Background color dark cannot be null";

      var backgroundColorD = Color.decode(backgroundColorDark);

      assert backgroundColorD != null : "Invalid background color dark format";

      this.backgroundColorDark = backgroundColorD;
    }

    /**
     * Set the primary color. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param primaryColor the primary color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setPrimaryColor(String primaryColor) throws AssertionError {
      assert primaryColor != null : "Primary color cannot be null";

      var primaryC = Color.decode(primaryColor);

      assert primaryC != null : "Invalid primary color format";

      this.primaryColor = primaryC;
    }

    /**
     * Set the primary foreground color. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param primaryForegroundColor the primary foreground color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setPrimaryForegroundColor(String primaryForegroundColor) throws AssertionError {
      assert primaryForegroundColor != null : "Primary foreground color cannot be null";

      var primaryForegroundC = Color.decode(primaryForegroundColor);

      assert primaryForegroundC != null : "Invalid primary foreground color format";

      this.primaryForegroundColor = primaryForegroundC;
    }

    /**
     * Set the primary color for dark theme. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param primaryColorDark the primary color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setPrimaryColorDark(String primaryColorDark) throws AssertionError {
      assert primaryColorDark != null : "Primary color dark cannot be null";

      var primaryColorD = Color.decode(primaryColorDark);

      assert primaryColorD != null : "Invalid primary color dark format";

      this.primaryColorDark = primaryColorD;
    }

    /**
     * Set the primary foreground color for dark theme. The color should be in hex format (e.g.,
     * #FFFFFF).
     *
     * @param primaryForegroundColorDark the primary foreground color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setPrimaryForegroundColorDark(String primaryForegroundColorDark)
        throws AssertionError {
      assert primaryForegroundColorDark != null : "Primary foreground color dark cannot be null";

      var primaryForegroundColorD = Color.decode(primaryForegroundColorDark);

      assert primaryForegroundColorD != null : "Invalid primary foreground color dark format";

      this.primaryForegroundColorDark = primaryForegroundColorD;
    }

    /**
     * Set the secondary color. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param secondaryColor the secondary color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setSecondaryColor(String secondaryColor) throws AssertionError {
      assert secondaryColor != null : "Secondary color cannot be null";

      var secondaryC = Color.decode(secondaryColor);

      assert secondaryC != null : "Invalid secondary color format";

      this.secondaryColor = secondaryC;
    }

    /**
     * Set the secondary foreground color. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param secondaryForegroundColor the secondary foreground color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setSecondaryForegroundColor(String secondaryForegroundColor) throws AssertionError {
      assert secondaryForegroundColor != null : "Secondary foreground color cannot be null";

      var secondaryForegroundC = Color.decode(secondaryForegroundColor);

      assert secondaryForegroundC != null : "Invalid secondary foreground color format";

      this.secondaryForegroundColor = secondaryForegroundC;
    }

    /**
     * Set the secondary color for dark theme. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param secondaryColorDark the secondary color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setSecondaryColorDark(String secondaryColorDark) throws AssertionError {
      assert secondaryColorDark != null : "Secondary color dark cannot be null";

      var secondaryColorD = Color.decode(secondaryColorDark);

      assert secondaryColorD != null : "Invalid secondary color dark format";

      this.secondaryColorDark = secondaryColorD;
    }

    /**
     * Set the secondary foreground color for dark theme. The color should be in hex format (e.g.,
     * #FFFFFF).
     *
     * @param secondaryForegroundColorDark the secondary foreground color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setSecondaryForegroundColorDark(String secondaryForegroundColorDark)
        throws AssertionError {
      assert secondaryForegroundColorDark != null : "Secondary foreground color dark cannot be null";

      var secondaryForegroundColorD = Color.decode(secondaryForegroundColorDark);

      assert secondaryForegroundColorD != null : "Invalid secondary foreground color dark format";

      this.secondaryForegroundColorDark = secondaryForegroundColorD;
    }

    /**
     * Set the transparent color. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param transparentColor the transparent color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setTransparentColor(String transparentColor) throws AssertionError {
      assert transparentColor != null : "Transparent color cannot be null";

      var transparentC = Color.decode(transparentColor);

      assert transparentC != null : "Invalid transparent color format";

      this.transparentColor = transparentC;
    }

    /**
     * Set the transparent color for dark theme. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param transparentColorDark the transparent color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setTransparentColorDark(String transparentColorDark) throws AssertionError {
      assert transparentColorDark != null : "Transparent color dark cannot be null";

      var transparentColorD = Color.decode(transparentColorDark);

      assert transparentColorD != null : "Invalid transparent color dark format";

      this.transparentColorDark = transparentColorD;
    }

    /**
     * Set the border color. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param borderColor the border color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setBorderColor(String borderColor) throws AssertionError {
      assert borderColor != null : "Border color cannot be null";

      var borderC = Color.decode(borderColor);

      assert borderC != null : "Invalid border color format";

      this.borderColor = borderC;
    }

    /**
     * Set the border color for dark theme. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param borderColorDark the border color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setBorderColorDark(String borderColorDark) throws AssertionError {
      assert borderColorDark != null : "Border color dark cannot be null";

      var borderColorD = Color.decode(borderColorDark);

      assert borderColorD != null : "Invalid border color dark format";

      this.borderColorDark = borderColorD;
    }

    /**
     * Set the placeholder color. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param placeholderColor the placeholder color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setPlaceholderColor(String placeholderColor) throws AssertionError {
      assert placeholderColor != null : "Placeholder color cannot be null";

      var placeholderC = Color.decode(placeholderColor);

      assert placeholderC != null : "Invalid placeholder color format";

      this.placeholderColor = placeholderC;

    }

    /**
     * Set the placeholder color for dark theme. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param placeholderColorDark the placeholder color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setPlaceholderColorDark(String placeholderColorDark) throws AssertionError {
      assert placeholderColorDark != null : "Placeholder color dark cannot be null";

      var placeholderColorD = Color.decode(placeholderColorDark);

      assert placeholderColorD != null : "Invalid placeholder color dark format";

      this.placeholderColorDark = placeholderColorD;
    }

  }

  public UIFont getUIFont() {
    return uiFont;
  }

  public UIColors getUIColor() {
    return uiColor;
  }

  public static synchronized UISettings getInstance() {
    if (instance == null) {
      instance = new UISettings();
    }
    return instance;
  }

  @Override
  public String getProperty(String key) {
    var value = properties.getProperty(key);

    if (value == null) {
      value = defaultProperties.getProperty(key);
    }

    return value;
  }

  @Override
  public String getProperty(String key, String defaultValue) {
    var value = properties.getProperty(key, defaultValue);

    if (value == null) {
      value = defaultProperties.getProperty(key, defaultValue);
    }

    return value;
  }

  @Override
  public int getIntProperty(String key) {
    return Integer.parseInt(getProperty(key));
  }

  @Override
  public int getIntProperty(String key, int defaultValue) {
    var value = getProperty(key);

    return (value != null) ? Integer.parseInt(value) : defaultValue;
  }

  @Override
  public void setProperty(String key, String value) throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "UISettings: To change UI settings, please use the set methods for each respective property, then call save() to save the changes.");
  }

  @Override
  public void save() {
    // Save properties to the configuration directory
    try (var outputStream = new FileOutputStream(propertiesFilePath)) {
      properties.store(outputStream, "DO NOT EDIT THOSE PREFIXED WITH '_'. Auto-generated file");
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }}
