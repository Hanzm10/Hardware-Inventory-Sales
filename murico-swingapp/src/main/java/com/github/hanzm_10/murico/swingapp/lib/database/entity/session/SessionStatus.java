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
package com.github.hanzm_10.murico.swingapp.lib.database.entity.session;

import java.util.Locale;

public enum SessionStatus {
    ACTIVE, INACTIVE, REVOKED;

    /**
     * Returns the SessionStatus enum constant that matches the given string.
     *
     * @param status The string representation of the session status.
     * @return The corresponding SessionStatus enum constant.
     * @throws IllegalArgumentException if the string does not match any enum constant.
     */
    public static SessionStatus fromString(String status) {
        for (SessionStatus sessionStatus : SessionStatus.values()) {
            if (sessionStatus.name().equalsIgnoreCase(status)) {
                return sessionStatus;
            }
        }

        throw new IllegalArgumentException("No constant with text " + status + " found");
    }


    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
