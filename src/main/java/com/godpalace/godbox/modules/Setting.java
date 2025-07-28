package com.godpalace.godbox.modules;

import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.mgr.KeyBindListener;
import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.BoxComboBox;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
public class Setting implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private boolean enabled;

    @Setter
    private String keyBind = "None";

    private final String displayName = "设置";
    private final String description = "设置模块的相关设置(保存更改需运行一次模块，重启程序生效)";
    private final String typeListName = "SETTINGS";

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("是否启用快捷键", "boolean", true, "", "", ""),
            new ModuleArg("是否启用模块开关提示", "boolean", true, "", "", "")
    };


    @Override
    public void Enable() {
        enabled = true;
        UiSettings.keyBindEnabled = Boolean.parseBoolean(args[0].getValue().toString());
        UiSettings.moduleInfoEnabled = Boolean.parseBoolean(args[1].getValue().toString());
        if (UiSettings.keyBindEnabled) {
            KeyBindListener.start();
        } else {
            KeyBindListener.stop();
        }
    }

    @Override
    public void Disable() {
        enabled = false;
    }

    @Override
    public void init() {}
}