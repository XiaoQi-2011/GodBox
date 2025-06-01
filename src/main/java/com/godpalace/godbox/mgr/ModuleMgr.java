package com.godpalace.godbox.mgr;

import com.godpalace.godbox.TypeLists;
import com.godpalace.godbox.module.Module;
import com.godpalace.godbox.system.OS;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ModuleMgr {
    @Getter
    private static final List<Module> modules = new ArrayList<>();

    private static final File edf = new File("." + File.separator + "edf" + File.separator);
    private static final File exe = new File("." + File.separator + "exe" + File.separator);

    private static final String executableFileFormat = switch (OS.getSystemType()) {
        case WINDOWS -> ".exe";
        case LINUX, MACOS, UNKNOWN -> "";
    };

    public static void initialize() {
        if (!edf.exists()) {
            edf.mkdirs();
        }

        if (!exe.exists()) {
            exe.mkdirs();
        }

        File[] edfFiles = edf.listFiles();
        if (edfFiles != null) {
            for (File file : edfFiles) {
                // 判断是否为edf文件
                if (file.isFile() && file.getName().endsWith(".json")) {
                    Module module = Module.fromJsonFile(file);
                    module.setExePath(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + executableFileFormat);

                    // 判断是否存在对应的可执行文件
                    if (!new File(module.getExePath()).exists()) {
                        log.warn("Executable file {} not found in exe folder", module.getExePath());
                    }

                    // 判断TypeList是否存在
                    try {
                        TypeLists.valueOf(module.getTypeListName());
                    } catch (IllegalArgumentException e) {
                        log.error("TypeList {} not found", module.getTypeListName());
                        continue;
                    }

                    // 创建模块配置面板
                    ModuleSettingsPanel panel = new ModuleSettingsPanel(module.getDisplayName(), module.getDescription(), module.getArgs());
                    module.setSettingsPanel(panel);

                    // 添加到modules列表中
                    modules.add(module);
                    log.info("Module {} loaded", module.getDisplayName());
                }
            }
        }
    }
}
