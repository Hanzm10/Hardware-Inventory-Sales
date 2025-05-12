package com.github.hanzm_10.murico.swingapp.lib.utils; // Or your preferred utility package

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import java.awt.Graphics2D;
import java.awt.Image; // For potential scaling if you need it later
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import javax.swing.ImageIcon;

public class SvgIconUtil {

    private static final SVGUniverse SVG_UNIVERSE = new SVGUniverse();

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private SvgIconUtil() {}

    /**
     * Loads an SVG resource from the classpath, renders it to a BufferedImage,
     * and returns it as an ImageIcon.
     *
     * @param resourcePath The absolute path to the SVG resource within the classpath (e.g., "/icons/my_icon.svg").
     * @param width        The desired width of the rendered icon.
     * @param height       The desired height of the rendered icon.
     * @return An ImageIcon containing the rendered SVG, or null if an error occurs.
     */
    public static ImageIcon loadSVGIcon(String resourcePath, int width, int height) {
        if (width <= 0 || height <= 0) {
            System.err.println("Invalid dimensions for SVG icon: width and height must be positive. Path: " + resourcePath);
            return createFallbackIcon(width > 0 ? width : 16, height > 0 ? height : 16); // Fallback if dimensions are bad
        }

        try {
            // Use a class from the same module/jar to ensure correct classloader for getResource
            // If SvgIconUtil is in a different module/jar than where your icons are,
            // you might need to pass a reference Class object:
            // URL svgUrl = referenceClass.getResource(resourcePath);
            URL svgUrl = SvgIconUtil.class.getResource(resourcePath);

            if (svgUrl == null) {
                System.err.println("SVG resource not found at classpath location: " + resourcePath);
                return createFallbackIcon(width, height);
            }

            URI svgUri = svgUrl.toURI();
            SVGDiagram diagram = SVG_UNIVERSE.getDiagram(svgUri);

            if (diagram == null) {
                System.err.println("Could not load SVG diagram from URI: " + svgUri);
                return createFallbackIcon(width, height);
            }

            // It's good practice to set the diagram size if it doesn't have one,
            // or if you want to override its intrinsic size for rendering.
            // This ensures the rendering is done into the target BufferedImage dimensions.
            diagram.setDeviceViewport(new java.awt.Rectangle(0, 0, width, height));
            // diagram.setIgnoringSVGDimension(true); // Could be useful sometimes
             diagram.setPreferredSize(new java.awt.Dimension(width,height));


            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();

            // Set rendering hints for better quality
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);


            // Render the SVG to the BufferedImage
            // It's important to scale the diagram to fit if its intrinsic size differs.
            // diagram.render(g2); // This might not scale correctly if diagram's size is not set.

            // Alternative rendering with scaling (if needed, but setDeviceViewport should handle it):
            // float diagramWidth = diagram.getWidth();
            // float diagramHeight = diagram.getHeight();
            // if (diagramWidth > 0 && diagramHeight > 0) {
            //     double scaleX = (double) width / diagramWidth;
            //     double scaleY = (double) height / diagramHeight;
            //     double scale = Math.min(scaleX, scaleY); // Maintain aspect ratio
            //     g2.scale(scale, scale);
            //     diagram.render(g2);
            // } else {
            //     // If diagram has no size, render it as is, setDeviceViewport should have sized it.
                 diagram.render(g2);
            // }

            g2.dispose();

            return new ImageIcon(image);

        } catch (Exception e) {
            System.err.println("Error loading/rendering SVG icon '" + resourcePath + "': " + e.getMessage());
            e.printStackTrace();
            return createFallbackIcon(width, height); // Return a fallback on any error
        }
    }

    /**
     * Creates a simple fallback ImageIcon (e.g., a colored square).
     * @param width The width of the fallback icon.
     * @param height The height of the fallback icon.
     * @return A fallback ImageIcon.
     */
    private static ImageIcon createFallbackIcon(int width, int height) {
        BufferedImage fallbackImage = new BufferedImage(
            Math.max(1, width), // Ensure width/height are at least 1
            Math.max(1, height),
            BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = fallbackImage.createGraphics();
        g2.setColor(java.awt.Color.LIGHT_GRAY);
        g2.fillRect(0, 0, width, height);
        g2.setColor(java.awt.Color.RED);
        g2.drawLine(0, 0, width - 1, height - 1);
        g2.drawLine(0, height - 1, width - 1, 0);
        g2.dispose();
        return new ImageIcon(fallbackImage);
    }
}