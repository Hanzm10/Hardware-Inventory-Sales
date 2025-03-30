package com.murico.app.view.components.images;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/** TODO: Add aspect-ratio resizing */
public class ResizableImageIcon {
  private ImageIcon imageIcon;

  public ResizableImageIcon(String path) {
    URL imageUrl = getClass().getResource(path);

    assert imageUrl != null : "Image in path " + path + " not found";

    this.imageIcon = new ImageIcon(imageUrl);
  }

  public ImageIcon getImageIcon() {
    return imageIcon;
  }

  public ResizableImageIcon resizeImageIcon(int width, int height) {
    assert width > 0 : "Width must be greater than 0";
    assert height > 0 : "Height must be greater than 0";

    Image image = this.imageIcon.getImage();
    Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

    this.imageIcon = new ImageIcon(newImage);

    return this;
  }
}
