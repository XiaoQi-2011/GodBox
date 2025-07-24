package com.godpalace.godbox.util;

public final class ArgUtils {
    public static boolean isNumber(String type) {
        return type.equals("byte") ||
                type.equals("short") ||
                type.equals("int") ||
                type.equals("long") ||
                type.equals("float") ||
                type.equals("double");
    }
}
