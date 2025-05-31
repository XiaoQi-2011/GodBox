package com.godpalace.godbox.system;

import lombok.Getter;

public class OS {
    @Getter
    private static final SystemType systemType;

    static {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            systemType = SystemType.WINDOWS;
        } else if (osName.contains("mac")) {
            systemType = SystemType.MACOS;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            systemType = SystemType.LINUX;
        } else {
            systemType = SystemType.UNKNOWN;
        }
    }
}
