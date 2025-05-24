package com.godpalace.godbox.Module;

import com.godpalace.godbox.Utils.PackageUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class ModuleMgr {
    @Getter
    private static final HashMap<String, Module> modules = new HashMap<>();

    public static void initialize() throws Exception {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            List<String> classes = PackageUtil.getClassName("com.godpalace.godbox.Module.modules");

            for (String classPath : classes) {
                String className = classPath.substring(classPath.lastIndexOf(".") + 1);

                Class<?> clazz = loader.loadClass(classPath);
                if (!Module.class.isAssignableFrom(clazz)) continue;

                Module module = (Module) clazz.getDeclaredConstructor().newInstance();
                if (modules.containsKey(module.getName())) {
                    log.error("Duplicate module ID: {}", module.getID());
                    continue;
                }

                modules.put(module.getName(), module);
                log.debug("Loading module: {}", className);
            }
        } catch (Exception e) {
            log.error("Error initializing modules", e);
            throw new Exception("Error initializing modules", e);
        }
    }
}

