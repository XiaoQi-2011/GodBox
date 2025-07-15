package com.godpalace.godbox.util;

public final class CharToStringUtil {
    public static String charToString(char[] c) {
        StringBuilder s = new StringBuilder();
        for (char ch : c) {
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || (ch == '-' || ch == '_' || ch == '.' || ch == '@')) {
                s.append(ch);
            }
        }
        return s.toString();
    }
}
