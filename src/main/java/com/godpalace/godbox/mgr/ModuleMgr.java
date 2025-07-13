package com.godpalace.godbox.mgr;

import com.godpalace.godbox.TypeLists;
import com.godpalace.godbox.modules.Module;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.godpalace.godbox.util.PackageUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ModuleMgr {
    @Getter
    private static final List<Module> modules = new ArrayList<>();
    private static final ModuleConfigMgr moduleConfigMgr = new ModuleConfigMgr();
    private static final String CONFIG_FILE_PATH = "config.json";

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(moduleConfigMgr::save));
    }

    public static void initialize() {
        moduleConfigMgr.init(CONFIG_FILE_PATH, modules);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        List<String> classes = PackageUtil.getClassName("com.godpalace.godbox.modules");
        for (String classPath : classes) {
            String className = classPath.substring(classPath.lastIndexOf(".") + 1);
            if (className.equals("Module")) continue;

            Class<?> clazz;
            try {
                clazz = loader.loadClass(classPath);
            } catch (ClassNotFoundException e) {
                log.error("Error loading class: {}", e.getMessage());
                continue;
            }
            if (!Module.class.isAssignableFrom(clazz)) continue;

            Module module;
            try {
                module = (Module) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.error("Error creating module: {}", e.getMessage());
                continue;
            }

            // 判断TypeList是否存在
            try {
                TypeLists.valueOf(module.getTypeListName());
            } catch (IllegalArgumentException e) {
                log.error("TypeList {} not found", module.getTypeListName());
                continue;
            }

            // 添加到modules列表中
            modules.add(module);
            log.info("Module {} loaded", module.getDisplayName());

            // 从配置文件中加载模块配置
            moduleConfigMgr.load();

            // 创建模块配置面板
            ModuleSettingsPanel panel = new ModuleSettingsPanel(module);
            module.setSettingsPanel(panel);
        }
    }
}
