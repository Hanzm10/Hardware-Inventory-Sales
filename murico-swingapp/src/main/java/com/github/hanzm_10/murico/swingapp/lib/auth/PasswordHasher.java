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
