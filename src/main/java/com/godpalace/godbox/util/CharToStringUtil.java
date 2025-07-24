package com.godpalace.godbox.util;

public final class CharToStringUtil {
    public static String charToString(char[] c) {
        String s1 = String.copyValueOf(c);
        return s1.substring(0, s1.indexOf("\0"));
    }
}
