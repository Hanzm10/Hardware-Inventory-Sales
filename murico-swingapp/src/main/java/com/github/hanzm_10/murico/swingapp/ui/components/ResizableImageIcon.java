package com.github.hanzm_10.murico.swingapp.ui.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class ResizableImageIcon extends ImageIcon {
	public ResizableImageIcon(Image image) {
		super(image);
	}

	@Override
	public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		super.paintIcon(c, g, x, y);
	}
}
