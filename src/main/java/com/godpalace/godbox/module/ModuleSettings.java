package com.godpalace.godbox.module;

import com.godpalace.godbox.PrimitiveValue;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Slf4j
public class ModuleSettings {
    private static final List<ModuleSettings> settingsList = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (ModuleSettings settings : settingsList) {
                try {
                    // 保存Mappings
                    for (String key : settings.mappings.keySet()) {
                        settings.properties.setProperty(key, settings.mappings.get(key).toString());
                    }

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
    private final HashMap<String, PrimitiveValue<?>> mappings = new HashMap<>();

    public ModuleSettings(String fileName) {
        storagePath = ".\\modules\\" + fileName + ".properties";

        try {
            File file = new File(storagePath);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } else {
                properties.load(file.toURI().toURL().openStream());

                // 加载Mappings
                for (String key : properties.stringPropertyNames()) {
                    int index = properties.getProperty(key).lastIndexOf("@");
                    String data = properties.getProperty(key).substring(0, index);
                    String type = properties.getProperty(key).substring(index + 1);

                    // 获取PrimitiveValue并存入mappings
                    PrimitiveValue<?> primitiveValue = PrimitiveValue.getFromString(data, type);
                    mappings.put(key, primitiveValue);
                }
            }
        } catch (Exception e) {
            log.error("Failed to load module settings", e);
        }

        settingsList.add(this);
    }

    @Synchronized
    public void put(String key, PrimitiveValue<?> primitiveValue) {
        mappings.put(key, primitiveValue);
    }

    @Synchronized
    public PrimitiveValue<?> get(String key) {
        if (!mappings.containsKey(key)) {
            return null;
        }

        return mappings.get(key);
    }
}
