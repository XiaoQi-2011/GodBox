package com.godpalace.godbox.mgr;

import com.godpalace.godbox.module.Module;
import com.godpalace.godbox.util.PackageUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class ModuleMgr {
    @Getter
    private static final HashMap<Integer, com.godpalace.godbox.module.Module> modules = new HashMap<>();

    public static void initialize() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            List<String> classes = PackageUtil.getClassName("com.godpalace.godbox.module");

            for (String classPath : classes) {
                String className = classPath.substring(classPath.lastIndexOf(".") + 1);

                Class<?> clazz = loader.loadClass(classPath);
                if (clazz.equals(com.godpalace.godbox.module.Module.class)) continue;
                if (!com.godpalace.godbox.module.Module.class.isAssignableFrom(clazz)) continue;

                com.godpalace.godbox.module.Module module = (Module) clazz.getDeclaredConstructor().newInstance();
                if (modules.containsKey(module.getID())) {
                    log.error("Duplicate module ID: {}", module.getID());
                    continue;
                }

                modules.put(module.getID(), module);
                log.debug("Loading module: {}", className);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

