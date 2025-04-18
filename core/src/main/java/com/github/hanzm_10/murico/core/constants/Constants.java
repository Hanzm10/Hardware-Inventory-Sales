/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.core.constants;

import java.awt.Dimension;

public final class Constants {
    public static final class Button {
        public static enum ButtonSize {
            LARGE(new Dimension(225, 50)), MEDIUM(new Dimension(175, 40)), SMALL(
                    new Dimension(125, 30)), XSMALL(
                            new Dimension(75, 20)), ICON(new Dimension(48, 48));

            private final Dimension size;

            ButtonSize(Dimension size) {
                this.size = size;
            }

            public Dimension getValue() {
                return size;
            }
        }

        public static final class Font {
            public static final int STYLE = java.awt.Font.BOLD;
            public static final int SIZE = 14;
        }

        public static final String ICON_PATH = "/icons/";
    }

    public static final String CONFIG_FILE_NAME = "murico";
    public static final String FONT_NAME = "Montserrat";
}
