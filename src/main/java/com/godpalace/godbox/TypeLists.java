package com.godpalace.godbox;

import lombok.Getter;

public enum TypeLists {
    TEST("测试"),
    SCREEN("屏幕");

    @Getter
    private final String name;

    TypeLists(String name) {
        this.name = name;
    }
}
