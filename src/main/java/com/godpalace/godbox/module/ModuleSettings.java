package com.godpalace.godbox.module;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class ModuleSettings {
    private static final List<ModuleSettings> settingsList = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (ModuleSettings settings : settingsList) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(settings.storagePath));
                    settings.properties.store(writer, "Module settings");
                    writer.close();
                } catch (Exception e) {
                    log.error("Failed to save module settings", e);
                }
            }
        }));
    }

    private final String storagePath;
    private final Properties properties = new Properties();

    public ModuleSettings(String fileName) {
        storagePath = ".\\modules\\" + fileName + ".properties";

        try {
            File file = new File(storagePath);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } else {
                properties.load(file.toURI().toURL().openStream());
            }
        } catch (Exception e) {
            log.error("Failed to load module settings", e);
        }

        settingsList.add(this);
    }

    public synchronized void put(String key, String value) {
        properties.setProperty(key, value);
    }

    public synchronized String get(String key) {
        return properties.getProperty(key);
    }
}
