package com.godpalace.godbox;

import lombok.Getter;

@Getter
public enum TypeLists {
    TEST("测试"),
    SCREEN("屏幕"),
    SYSTEM("系统"),
    NETWORK("网络"),
    SETTINGS("设置");

    private final String name;

    TypeLists(String name) {
        this.name = name;
    }
}
