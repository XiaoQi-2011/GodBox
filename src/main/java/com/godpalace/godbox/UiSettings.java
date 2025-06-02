package com.godpalace.godbox;

import com.godpalace.data.annotation.Data;
import com.godpalace.data.annotation.LocalDatabase;
import com.godpalace.data.database.FileDatabaseEngine;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
@LocalDatabase(path = "ui_settings.db")
public class UiSettings {
    @Data
    public static int openUiKey = 3638; // 右SHIFT

    @Data
    public static int backgroundOpacity = 50;

    @Data
    public static Color themeColor = new Color(255, 80, 80);

    @Data
    public static Font font = new Font("宋体", Font.PLAIN, 14);

    @Data
    public static int moduleHeight = 20;

    public static void initialize() {
        try {
            FileDatabaseEngine.init(UiSettings.class, null);
        } catch (Exception e) {
            log.error("Error initializing database", e);
        }
    }
}
