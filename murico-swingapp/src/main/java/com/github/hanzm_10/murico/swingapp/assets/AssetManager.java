/**
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.assets;

import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.cache.LRU;
import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.github.weisj.jsvg.renderer.SVGRenderingHints;

public class AssetManager {
	private static final LRU<String, Image> images = new LRU<>(30);
	private static final LRU<String, ImageIcon> icons = new LRU<>(20);

	public static SVGDocument getDocument(String path) {
		var svgLoader = new SVGLoader();
		var url = AssetManager.class.getResource(path);
		
	    if (url == null) { // ***** ADD THIS CHECK *****
	        System.err.println("SVGDocument resource not found: " + path + ". Returning null.");
	        // throw new IllegalArgumentException("SVG resource not found at path: " + path);
	        return null; // Or handle as appropriate
	    }
		
		var svgDocument = svgLoader.load(url, LoaderContext.builder().build());

		return svgDocument;
	}

	public static ImageIcon getOrLoadIcon(@NotNull final String path) throws URISyntaxException {
		var icon = icons.get(path);

		if (icon == null) {
			icon = loadIcon(path);
		}

		icons.update(path, icon);

		return icon;
	}

	public static Image getOrLoadImage(@NotNull final String path) throws IOException, InterruptedException {
		var image = images.get(path);

		if (image == null) {
			image = loadImage(path);
		}

		images.update(path, image);

		return image;
	}

	private static ImageIcon loadIcon(@NotNull final String path) {
		var svgLoader = new SVGLoader();
		var url = AssetManager.class.getResource(path);
		
	    if (url == null) { // ***** ADD THIS CHECK *****
	        System.err.println("ImageIcon resource not found: " + path + ". Returning null or default icon.");
	        // Optionally return a default placeholder icon or throw an exception
	        // For example, return a small transparent ImageIcon:
	        // return new ImageIcon(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
	        throw new IllegalArgumentException("Icon resource not found at path: " + path); // Or handle more gracefully
	    }
		
		var svgDocument = svgLoader.load(url, LoaderContext.builder().build());
		var size = svgDocument.size();
		var icon = new BufferedImage((int) size.width, (int) size.height, BufferedImage.TYPE_INT_ARGB);
		var g = icon.createGraphics();
		// Will use the value of RenderingHints.KEY_ANTIALIASING by default
		g.setRenderingHint(SVGRenderingHints.KEY_IMAGE_ANTIALIASING, SVGRenderingHints.VALUE_IMAGE_ANTIALIASING_ON);
		g.setRenderingHint(SVGRenderingHints.KEY_SOFT_CLIPPING, SVGRenderingHints.VALUE_SOFT_CLIPPING_ON);
		g.setRenderingHint(SVGRenderingHints.KEY_MASK_CLIP_RENDERING,
				SVGRenderingHints.VALUE_MASK_CLIP_RENDERING_ACCURACY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT, RenderingHints.VALUE_RESOLUTION_VARIANT_BASE);
		svgDocument.render(null, g);
		g.dispose();
		return new ImageIcon(icon);
	}

	private static Image loadImage(@NotNull final String path) throws IOException, InterruptedException {
		var resourceStream = AssetManager.class.getResourceAsStream(path);

		if (resourceStream == null) {
			throw new IllegalArgumentException("Resource not found: " + path);
		}

		try (var inputStream = AssetManager.class.getResourceAsStream(path)) {
			return ImageIO.read(inputStream);
		}

		/*
		 * try (var dataInputStream = new
		 * DataInputStream(AssetManager.class.getResourceAsStream(path))) { byte bytes[]
		 * = new byte[dataInputStream.available()];
		 *
		 * dataInputStream.readFully(bytes);
		 *
		 * return Toolkit.getDefaultToolkit().createImage(bytes); }
		 */
	}
}
