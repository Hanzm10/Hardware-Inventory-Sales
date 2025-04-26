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
package com.github.hanzm_10.murico.swingapp.constants;

import java.io.IOException;
import java.util.Properties;
import com.github.hanzm_10.murico.swingapp.lib.io.PropertiesIO;

public final class Metadata {
    private static final Properties properties = new Properties();

    public static final String APP_TITLE;
    public static final String APP_VERSION;
    public static final String APP_ENV;

    static {
        try {
            PropertiesIO.loadProperties(Metadata.class, properties, "metadata");
        } catch (IllegalArgumentException e) {
            // DO nothing
        } catch (IOException e) {
            throw new RuntimeException("Failed to load metadata.properties", e);
        }

        APP_TITLE = properties.getProperty(PropertyKey.Metadata.APP_TITLE);
        APP_VERSION = properties.getProperty(PropertyKey.Metadata.APP_VERSION);
        APP_ENV = properties.getProperty(PropertyKey.Metadata.APP_ENV);
    }

    private Metadata() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
