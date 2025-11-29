package com.godpalace.godbox.util;

public final class StringUtil {
    public static String charToString(char[] c) {
        String s1 = String.copyValueOf(c);
        return s1.substring(0, s1.indexOf("\0"));
    }

    public static int getNumber(String s) {
        StringBuilder result = new StringBuilder();
        if (s == null || s.isEmpty()) {
            return 0;
        }
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                result.append(c);
            }
        }
        return Integer.parseInt(result.toString());
    }

}
