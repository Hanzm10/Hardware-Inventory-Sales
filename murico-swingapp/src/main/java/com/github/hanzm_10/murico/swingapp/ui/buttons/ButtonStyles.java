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
package com.github.hanzm_10.murico.swingapp.ui.buttons;

import java.awt.Color;

import com.formdev.flatlaf.FlatLaf;
import com.github.hanzm_10.murico.swingapp.constants.Styles;

public enum ButtonStyles {
    PRIMARY {
        @Override
        public Color getBackgroundColor() {
            return FlatLaf.isLafDark() ? Styles.PRIMARY_COLOR_DARK : Styles.PRIMARY_COLOR;
        }

        @Override
        public Color getForegroundColor() {
            return FlatLaf.isLafDark() ? Styles.PRIMARY_FOREGROUND_COLOR_DARK : Styles.PRIMARY_FOREGROUND_COLOR;
        }
    },
    SECONDARY {
        @Override
        public Color getBackgroundColor() {
            return FlatLaf.isLafDark() ? Styles.SECONDARY_COLOR_DARK : Styles.SECONDARY_COLOR;
        }

        @Override
        public Color getForegroundColor() {
            return FlatLaf.isLafDark() ? Styles.SECONDARY_FOREGROUND_COLOR_DARK : Styles.SECONDARY_FOREGROUND_COLOR;
        }
    },
    TERTIARY {
        @Override
        public Color getBackgroundColor() {
            return FlatLaf.isLafDark() ? Styles.TERTIARY_COLOR_DARK : Styles.TERTIARY_COLOR;
        }

        @Override
        public Color getForegroundColor() {
            return FlatLaf.isLafDark() ? Styles.TERTIARY_FOREGROUND_COLOR_DARK : Styles.TERTIARY_FOREGROUND_COLOR;
        }
    },
    CONFIRM {
        @Override
        public Color getBackgroundColor() {
            return FlatLaf.isLafDark() ? Styles.CONFIRM_COLOR_DARK : Styles.CONFIRM_COLOR;
        }

        @Override
        public Color getForegroundColor() {
            return FlatLaf.isLafDark() ? Styles.CONFIRM_FOREGROUND_COLOR_DARK : Styles.CONFIRM_FOREGROUND_COLOR;
        }
    },
    DANGER {
        @Override
        public Color getBackgroundColor() {
            return FlatLaf.isLafDark() ? Styles.DANGER_COLOR_DARK : Styles.DANGER_COLOR;
        }

        @Override
        public Color getForegroundColor() {
            return FlatLaf.isLafDark() ? Styles.DANGER_FOREGROUND_COLOR_DARK : Styles.DANGER_FOREGROUND_COLOR;
        }
    },
    SUCCESS {

        @Override
        public Color getBackgroundColor() {
            return FlatLaf.isLafDark() ? Styles.SUCCESS_COLOR_DARK : Styles.SUCCESS_COLOR;
        }

        @Override
        public Color getForegroundColor() {
            return FlatLaf.isLafDark() ? Styles.SUCCESS_FOREGROUND_COLOR_DARK : Styles.SUCCESS_FOREGROUND_COLOR;
        }
    };

    public abstract Color getBackgroundColor();

    public abstract Color getForegroundColor();

}
