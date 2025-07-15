package com.godpalace.godbox.mgr;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.module_mgr.Module;
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
            Config config = new Config(
                    module.getDisplayName(),
                    module.getArgs(),
                    module.isEnabled(),
                    module.getKeyBind());
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
        long run_time = System.currentTimeMillis();
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

            for (ModuleArg arg : config.value) {
                for (ModuleArg arg2 : module.getArgs()) {
                    if (arg2.getName().equals(arg.getName())) {
                        arg2.setValue(arg.getValue());
                        break;
                    }
                }
            }
            module.setKeyBind(config.keyBind);
            if (config.enabled) {
                module.Enable();
            } else {
                module.Disable();
            }
        }
        log.info("Config load time: {}ms", System.currentTimeMillis() - run_time);
    }

    public static class Config {
        private final String name;
        private final boolean enabled;
        private final String keyBind;
        private final ModuleArg[] value;

        public Config(String name, ModuleArg[] value, boolean enabled, String keyBind) {
            this.name = name;
            this.value = value;
            this.enabled = enabled;
            this.keyBind = keyBind;
        }
    }
}
