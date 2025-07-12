package com.godpalace.godbox.mgr;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.godpalace.godbox.module.ModuleArg;
import com.godpalace.godbox.module.modules.Module;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModuleConfigMgr {
    public ModuleConfigMgr() {}

    private List<Module> modules = new ArrayList<>();
    private List<Config> configs = new ArrayList<>();
    private String configFileName;

    public void init(String configFileName, List<Module> modules) {
        this.configFileName = configFileName;
        this.modules = modules;
    }

    public void save() {
        configs.clear();
        for (Module module : modules) {
            Config config = new Config(module.getDisplayName(), module.getArgs(), module.isEnabled());
            configs.add(config);
        }
        try {
            FileWriter writer = new FileWriter(configFileName);
            GsonFactory.getGson().toJson(configs, writer);
            writer.close();
        } catch (IOException e) {
            log.error("Error writing config file: {}", e.getMessage());
        }
    }

    public void load() {
        try {
            FileReader reader = new FileReader(configFileName);
            Type type = new TypeToken<List<Config>>() {}.getType();
            configs = GsonFactory.getGson().fromJson(reader, type);
        } catch (IOException e) {
            log.error("Error reading config file: {}", e.getMessage());
        }
        for (Module module : modules) {
            Config config = null;
            for (Config c : configs) {
                if (c.name.equals(module.getDisplayName())) {
                    config = c;
                    break;
                }
            }
            if (config == null) {
                continue;
            }

            module.setArgs(config.value);
            if (config.enabled) {
                module.Enable();
            } else {
                module.Disable();
            }
        }
    }

    public static class Config {
        private final String name;
        private final ModuleArg[] value;
        private final boolean enabled;

        public Config(String name, ModuleArg[] value, boolean enabled) {
            this.name = name;
            this.value = value;
            this.enabled = enabled;
        }
    }
}
