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
package com.github.hanzm_10.murico.swingapp.lib.auth;

import java.util.ArrayList;
import java.util.List;

public class PasswordHasher {
    private static List<Integer> unicodeValues = new ArrayList<>();

    public static List<Integer> getUnicode() {
        return unicodeValues;
    }

    public static String hash(String password) {
        unicode(password);
        var val2 = getUnicode();
        var sum2 = sum(val2);
        var intPassword = Math.pow(sum2, 10) * 15;
        var strPass = String.valueOf(intPassword);
        return strPass;
    }

    public static void setUnicode(int value) {
        unicodeValues.clear();
        unicodeValues.add(value);
    }

    public static int sum(List<Integer> list) {
        var sum = 0;
        for (int num : list) {
            sum += num;
        }
        return sum;
    }

    public static void unicode(String str) {
        unicodeValues.clear();
        for (var i = 0; i < str.length(); i++) {
            var c = str.charAt(i);
            int unicodeValue = c;
            unicodeValues.add(unicodeValue);
            // System.out.println("Character: " + c + ", Unicode Value: " + unicodeValue);
        }
    }
}
