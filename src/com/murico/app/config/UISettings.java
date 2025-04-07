package com.murico.app.config;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import com.murico.app.utils.io.FileLoader;
import com.murico.app.view.utilities.RenderingUtilities;

// TODO: Update values in cache when properties are changed
public class UISettings extends AbstractSettings {
  private static UISettings instance;

  private static final String KEY_DISPLAY_PREFERRED_WIDTH = "screen.preferredWidth";
  private static final String KEY_DISPLAY_PREFERRED_HEIGHT = "screen.preferredHeight";

  private static final String KEY_BORDER_RADIUS = "border.radius";
  private static final String KEY_BORDER_WIDTH = "border.width";
  private static final String KEY_BORDER_OFFSET = "border.offset";

  private static final String KEY_FONT_FAMILY = "font.family";
  private static final String KEY_FONT_SIZE_H1 = "font.size.h1";
  private static final String KEY_FONT_SIZE_H2 = "font.size.h2";
  private static final String KEY_FONT_SIZE_H3 = "font.size.h3";
  private static final String KEY_FONT_SIZE_H4 = "font.size.h4";
  private static final String KEY_FONT_SIZE_H5 = "font.size.h5";
  private static final String KEY_FONT_SIZE_H6 = "font.size.h6";
  private static final String KEY_FONT_SIZE_BODY = "font.size.body";
  private static final String KEY_FONT_SIZE_BUTTON = "font.size.button";
  private static final String KEY_FONT_SIZE_CAPTION = "font.size.caption";

  private static final String KEY_SPACE_XXS = "space.scale.xxs";
  private static final String KEY_SPACE_XS = "space.scale.xs";
  private static final String KEY_SPACE_S = "space.scale.s";
  private static final String KEY_SPACE_M = "space.scale.m";
  private static final String KEY_SPACE_L = "space.scale.l";
  private static final String KEY_SPACE_XL = "space.scale.xl";
  private static final String KEY_SPACE_XXL = "space.scale.xxl";
  private static final String KEY_SPACE_XXXL = "space.scale.xxxl";
  private static final String KEY_SPACE_XXXXL = "space.scale.xxxxl";
  private static final String KEY_SPACE_XXXXXL = "space.scale.xxxxxl";

  private static final String KEY_COLOR_BACKGROUND = "color.background";
  private static final String KEY_COLOR_DARK_BACKGROUND = "color.dark.background";
  private static final String KEY_COLOR_FOREGROUND = "color.foreground";
  private static final String KEY_COLOR_DARK_FOREGROUND = "color.dark.foreground";
  private static final String KEY_COLOR_PRIMARY_DEFAULT = "color.primary.default";
  private static final String KEY_COLOR_PRIMARY_FOREGROUND = "color.primary.foreground";
  private static final String KEY_COLOR_DARK_PRIMARY_DEFAULT = "color.dark.primary.default";
  private static final String KEY_COLOR_DARK_PRIMARY_FOREGROUND = "color.dark.primary.foreground";
  private static final String KEY_COLOR_SECONDARY_DEFAULT = "color.secondary.default";
  private static final String KEY_COLOR_SECONDARY_FOREGROUND = "color.secondary.foreground";
  private static final String KEY_COLOR_DARK_SECONDARY_DEFAULT = "color.dark.secondary.default";

  private static final String KEY_COLOR_DARK_SECONDARY_FOREGROUND =
      "color.dark.secondary.foreground";
  private static final String KEY_COLOR_TRANSPARENT_DEFAULT = "color.transparent.default";
  private static final String KEY_COLOR_DARK_TRANSPARENT_DEFAULT = "color.dark.transparent.default";
  private static final String KEY_COLOR_BORDER_DEFAULT = "color.border.default";
  private static final String KEY_COLOR_DARK_BORDER_DEFAULT = "color.dark.border.default";
  private static final String KEY_COLOR_PLACEHOLDER_DEFAULT = "color.placeholder.default";
  private static final String KEY_COLOR_DARK_PLACEHOLDER_DEFAULT = "color.dark.placeholder.default";

  private final UIFont uiFont;
  private final UIDisplay uiDisplay;
  private final UIColors uiColor;
  private final UISpace uiSpace;
  private final UIBorder uiBorder;

  private UISettings() {
    super();

    uiFont = new UIFont();
    uiDisplay = new UIDisplay();
    uiColor = new UIColors();
    uiSpace = new UISpace();
    uiBorder = new UIBorder();
  }

  public UIFont getUIFont() {
    return uiFont;
  }

  public UIDisplay getUIDisplay() {
    return uiDisplay;
  }

  public UIColors getUIColor() {
    return uiColor;
  }

  public UISpace getUISpace() {
    return uiSpace;
  }

  public UIBorder getUIBorder() {
    return uiBorder;
  }

  public static synchronized UISettings getInstance() {
    if (instance == null) {
      instance = new UISettings();
    }
    return instance;
  }

  @Override
  protected String getFileName() {
    return "ui.properties";
  }

  @Override
  protected String getFilePath() {
    return FileLoader.getConfigurationDirectory() + File.separator + getFileName();
  }

  @Override
  protected boolean isFileReadOnly() {
    return false;
  }

  @Override
  protected boolean editableByUser() {
    return false;
  }

  /**
   * Used by inline classes to set properties. This method is not intended to be used outside of
   * this class.
   * 
   * @param key
   * @param value
   */
  private void privateSetProperty(String key, String value) {
    properties.setProperty(key, value);
  }

  public class UIDisplay {
    private int preferredWidth;
    private int preferredHeight;

    private UIDisplay() {
      this.preferredWidth = getIntProperty(KEY_DISPLAY_PREFERRED_WIDTH);
      this.preferredHeight = getIntProperty(KEY_DISPLAY_PREFERRED_HEIGHT);
    }

    public int getPreferredWidth() {
      return preferredWidth;
    }

    public int getPreferredHeight() {
      return preferredHeight;
    }
  }

  public class UIBorder {
    private int borderRadius;
    private int borderWidth;
    private int borderOffset;

    private UIBorder() {
      this.borderRadius = getIntProperty(KEY_BORDER_RADIUS);
      this.borderWidth = getIntProperty(KEY_BORDER_WIDTH);
      this.borderOffset = getIntProperty(KEY_BORDER_OFFSET);
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
      
      privateSetProperty(KEY_BORDER_RADIUS, String.valueOf(borderRadius));
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
      
      privateSetProperty(KEY_BORDER_WIDTH, String.valueOf(borderWidth));
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

      privateSetProperty(KEY_BORDER_OFFSET, String.valueOf(borderOffset));
    }
  }

  public class UISpace {
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

    private UISpace() {
      this.spaceXXS = getProperty(KEY_SPACE_XXS);
      this.spaceXS = getProperty(KEY_SPACE_XS);
      this.spaceS = getProperty(KEY_SPACE_S);
      this.spaceM = getProperty(KEY_SPACE_M);
      this.spaceL = getProperty(KEY_SPACE_L);
      this.spaceXL = getProperty(KEY_SPACE_XL);
      this.spaceXXL = getProperty(KEY_SPACE_XXL);
      this.spaceXXXL = getProperty(KEY_SPACE_XXXL);
      this.spaceXXXXL = getProperty(KEY_SPACE_XXXXL);
      this.spaceXXXXXL = getProperty(KEY_SPACE_XXXXXL);

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

    /**
     * Convert a value to pixels. If the value is already in pixels, it will be returned as is.
     * 
     * @param spaceXXS
     * @throws AssertionError if the value is null
     */
    public String toPx(String value) {
      assert value != null : "Value cannot be null";

      if (value.endsWith("px")) {
        return value;
      } else {
        return value + "px";
      }
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
      this.fontFamily = getProperty(KEY_FONT_FAMILY);
      this.h1Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H1));
      this.h2Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H2));
      this.h3Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H3));
      this.h4Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H4));
      this.h5Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H5));
      this.h6Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H6));
      this.bodyFont = new Font(fontFamily, Font.PLAIN, getIntProperty(KEY_FONT_SIZE_BODY));
      this.buttonFont = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_BUTTON));
      this.captionFont = new Font(fontFamily, Font.ITALIC, getIntProperty(KEY_FONT_SIZE_CAPTION));
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
      h1Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H1));
      h2Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H2));
      h3Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H3));
      h4Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H4));
      h5Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H5));
      h6Font = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_H6));
      bodyFont = new Font(fontFamily, Font.PLAIN, getIntProperty(KEY_FONT_SIZE_BODY));
      buttonFont = new Font(fontFamily, Font.BOLD, getIntProperty(KEY_FONT_SIZE_BUTTON));
      captionFont = new Font(fontFamily, Font.ITALIC, getIntProperty(KEY_FONT_SIZE_CAPTION));

      privateSetProperty(KEY_FONT_FAMILY, fontFamily);
    }

    public void setH1Font(Font h1Font) {
      this.h1Font = h1Font;

      privateSetProperty(KEY_FONT_SIZE_H1, String.valueOf(h1Font.getSize()));
    }

    public void setH2Font(Font h2Font) {
      this.h2Font = h2Font;

      privateSetProperty(KEY_FONT_SIZE_H2, String.valueOf(h2Font.getSize()));
    }

    public void setH3Font(Font h3Font) {
      this.h3Font = h3Font;

      privateSetProperty(KEY_FONT_SIZE_H3, String.valueOf(h3Font.getSize()));
    }

    public void setH4Font(Font h4Font) {
      this.h4Font = h4Font;

      privateSetProperty(KEY_FONT_SIZE_H4, String.valueOf(h4Font.getSize()));
    }

    public void setH5Font(Font h5Font) {
      this.h5Font = h5Font;

      privateSetProperty(KEY_FONT_SIZE_H5, String.valueOf(h5Font.getSize()));
    }

    public void setH6Font(Font h6Font) {
      this.h6Font = h6Font;

      privateSetProperty(KEY_FONT_SIZE_H6, String.valueOf(h6Font.getSize()));
    }

    public void setBodyFont(Font bodyFont) {
      this.bodyFont = bodyFont;

      privateSetProperty(KEY_FONT_SIZE_BODY, String.valueOf(bodyFont.getSize()));
    }

    public void setButtonFont(Font buttonFont) {
      this.buttonFont = buttonFont;

      privateSetProperty(KEY_FONT_SIZE_BUTTON, String.valueOf(buttonFont.getSize()));
    }

    public void setCaptionFont(Font captionFont) {
      this.captionFont = captionFont;

      privateSetProperty(KEY_FONT_SIZE_CAPTION, String.valueOf(captionFont.getSize()));
    }

    public void setFontSizeH1(int size) {
      this.h1Font = new Font(fontFamily, Font.BOLD, size);

      privateSetProperty(KEY_FONT_SIZE_H1, String.valueOf(size));
    }

    public void setFontSizeH2(int size) {
      this.h2Font = new Font(fontFamily, Font.BOLD, size);

      privateSetProperty(KEY_FONT_SIZE_H2, String.valueOf(size));
    }

    public void setFontSizeH3(int size) {
      this.h3Font = new Font(fontFamily, Font.BOLD, size);

      privateSetProperty(KEY_FONT_SIZE_H3, String.valueOf(size));
    }

    public void setFontSizeH4(int size) {
      this.h4Font = new Font(fontFamily, Font.BOLD, size);

      privateSetProperty(KEY_FONT_SIZE_H4, String.valueOf(size));
    }

    public void setFontSizeH5(int size) {
      this.h5Font = new Font(fontFamily, Font.BOLD, size);

      privateSetProperty(KEY_FONT_SIZE_H5, String.valueOf(size));
    }

    public void setFontSizeH6(int size) {
      this.h6Font = new Font(fontFamily, Font.BOLD, size);

      privateSetProperty(KEY_FONT_SIZE_H6, String.valueOf(size));
    }

    public void setFontSizeBody(int size) {
      this.bodyFont = new Font(fontFamily, Font.PLAIN, size);

      privateSetProperty(KEY_FONT_SIZE_BODY, String.valueOf(size));
    }

    public void setFontSizeButton(int size) {
      this.buttonFont = new Font(fontFamily, Font.BOLD, size);

      privateSetProperty(KEY_FONT_SIZE_BUTTON, String.valueOf(size));
    }

    public void setFontSizeCaption(int size) {
      this.captionFont = new Font(fontFamily, Font.ITALIC, size);

      privateSetProperty(KEY_FONT_SIZE_CAPTION, String.valueOf(size));
    }
  }

  public class UIColors {
    private Color backgroundColor;
    private Color backgroundColorDark;
    private Color foregroundColor;
    private Color foregroundColorDark;
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

    private UIColors() {
      this.backgroundColor = Color.decode(getProperty(KEY_COLOR_BACKGROUND));
      this.backgroundColorDark = Color.decode(getProperty(KEY_COLOR_DARK_BACKGROUND));
      this.foregroundColor = Color.decode(getProperty(KEY_COLOR_FOREGROUND));
      this.foregroundColorDark = Color.decode(getProperty(KEY_COLOR_DARK_FOREGROUND));
      this.primaryColor = Color.decode(getProperty(KEY_COLOR_PRIMARY_DEFAULT));
      this.primaryForegroundColor = Color.decode(getProperty(KEY_COLOR_PRIMARY_FOREGROUND));
      this.primaryColorDark = Color.decode(getProperty(KEY_COLOR_DARK_PRIMARY_DEFAULT));
      this.primaryForegroundColorDark =
          Color.decode(getProperty(KEY_COLOR_DARK_PRIMARY_FOREGROUND));
      this.secondaryColor = Color.decode(getProperty(KEY_COLOR_SECONDARY_DEFAULT));
      this.secondaryForegroundColor = Color.decode(getProperty(KEY_COLOR_SECONDARY_FOREGROUND));
      this.secondaryColorDark = Color.decode(getProperty(KEY_COLOR_DARK_SECONDARY_DEFAULT));
      this.secondaryForegroundColorDark =
          Color.decode(getProperty(KEY_COLOR_DARK_SECONDARY_FOREGROUND));
      this.transparentColor = RenderingUtilities
          .RgbColorToColorWithAlpha(Color.decode(getProperty(KEY_COLOR_TRANSPARENT_DEFAULT)), 0);
      this.transparentColorDark = RenderingUtilities.RgbColorToColorWithAlpha(
          Color.decode(getProperty(KEY_COLOR_DARK_TRANSPARENT_DEFAULT)), 0);
      this.borderColor = Color.decode(getProperty(KEY_COLOR_BORDER_DEFAULT));
      this.borderColorDark = Color.decode(getProperty(KEY_COLOR_DARK_BORDER_DEFAULT));
      this.placeholderColor = Color.decode(getProperty(KEY_COLOR_PLACEHOLDER_DEFAULT));
      this.placeholderColorDark = Color.decode(getProperty(KEY_COLOR_DARK_PLACEHOLDER_DEFAULT));
    }

    public Color getBackgroundColor() {
      return backgroundColor;
    }

    public Color getBackgroundColorDark() {
      return backgroundColorDark;
    }

    public Color getForegroundColor() {
      return foregroundColor;
    }

    public Color getForegroundColorDark() {
      return foregroundColorDark;
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

      privateSetProperty(KEY_COLOR_BACKGROUND, backgroundColor);
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

      privateSetProperty(KEY_COLOR_DARK_BACKGROUND, backgroundColorDark);
    }

    /**
     * Set the foreground color. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param foregroundColor the foreground color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setForegroundColor(String foregroundColor) throws AssertionError {
      assert foregroundColor != null : "Foreground color cannot be null";

      var foregroundC = Color.decode(foregroundColor);

      assert foregroundC != null : "Invalid foreground color format";

      this.foregroundColor = foregroundC;

      privateSetProperty(KEY_COLOR_FOREGROUND, foregroundColor);
    }

    /**
     * Set the foreground color for dark theme. The color should be in hex format (e.g., #FFFFFF).
     *
     * @param foregroundColorDark the foreground color in hex format
     * @throws AssertionError if the color is null or invalid
     */
    public void setForegroundColorDark(String foregroundColorDark) throws AssertionError {
      assert foregroundColorDark != null : "Foreground color dark cannot be null";

      var foregroundColorD = Color.decode(foregroundColorDark);

      assert foregroundColorD != null : "Invalid foreground color dark format";

      this.foregroundColorDark = foregroundColorD;

      privateSetProperty(KEY_COLOR_DARK_FOREGROUND, foregroundColorDark);
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

      privateSetProperty(KEY_COLOR_PRIMARY_DEFAULT, primaryColor);
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

      privateSetProperty(KEY_COLOR_PRIMARY_FOREGROUND, primaryForegroundColor);
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

      privateSetProperty(KEY_COLOR_DARK_PRIMARY_DEFAULT, primaryColorDark);
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

      privateSetProperty(KEY_COLOR_DARK_PRIMARY_FOREGROUND, primaryForegroundColorDark);
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

      privateSetProperty(KEY_COLOR_SECONDARY_DEFAULT, secondaryColor);
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

      privateSetProperty(KEY_COLOR_SECONDARY_FOREGROUND, secondaryForegroundColor);
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

      privateSetProperty(KEY_COLOR_DARK_SECONDARY_DEFAULT, secondaryColorDark);
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

      privateSetProperty(KEY_COLOR_DARK_SECONDARY_FOREGROUND, secondaryForegroundColorDark);
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

      privateSetProperty(KEY_COLOR_TRANSPARENT_DEFAULT, transparentColor);
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

      privateSetProperty(KEY_COLOR_DARK_TRANSPARENT_DEFAULT, transparentColorDark);
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

      privateSetProperty(KEY_COLOR_BORDER_DEFAULT, borderColor);
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

      privateSetProperty(KEY_COLOR_DARK_BORDER_DEFAULT, borderColorDark);
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

      privateSetProperty(KEY_COLOR_PLACEHOLDER_DEFAULT, placeholderColor);

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

      privateSetProperty(KEY_COLOR_DARK_PLACEHOLDER_DEFAULT, placeholderColorDark);
    }

  }
}
