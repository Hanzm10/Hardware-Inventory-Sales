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
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.cache.LRU;
import com.kitfox.svg.app.beans.SVGIcon;

public class AssetManager {
	private static final LRU<String, Image> images = new LRU<>(30);
	private static final LRU<String, SVGIcon> icons = new LRU<>(20);

	public static SVGIcon getOrLoadIcon(@NotNull final String path) throws URISyntaxException {
		var icon = new SVGIcon();
		icon.setSvgURI(AssetManager.class.getResource(path).toURI());

		icons.update("path", icon);

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
