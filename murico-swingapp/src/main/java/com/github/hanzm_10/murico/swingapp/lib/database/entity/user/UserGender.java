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
package com.github.hanzm_10.murico.swingapp.lib.database.entity.user;

import java.util.Locale;

public enum UserGender {
    MALE, FEMALE, UNKNOWN;

    /**
     * Returns the UserGender corresponding to the given string.
     *
     * @param gender
     *            the string representation of the UserGender
     * @return the UserGender corresponding to the given string
     * @throws IllegalArgumentException
     *             if the string does not match any UserGender
     */
    public static UserGender fromString(String gender) throws IllegalArgumentException {
        return switch (gender.toUpperCase(Locale.ENGLISH)) {
            case "MALE" -> MALE;
            case "FEMALE" -> FEMALE;
            case "UNKNOWN" -> UNKNOWN;
            default -> throw new IllegalArgumentException("Invalid gender: " + gender);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case MALE -> "male";
            case FEMALE -> "female";
            case UNKNOWN -> "unknown";
        };
    }
}
