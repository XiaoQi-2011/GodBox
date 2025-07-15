package com.godpalace.godbox.modules;

import com.godpalace.godbox.UiSettings;
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
    private final String description = "设置模块的相关设置";
    private final String typeListName = "SETTINGS";

    private final BoxComboBox.BoxEnum ArgType = new BoxComboBox.BoxEnum(
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("是否启用快捷键", "boolean", true, "", "", ""),
    };


    @Override
    public void Enable() {
        enabled = true;
        UiSettings.keyBindEnabled = Boolean.parseBoolean(args[0].getValue().toString());
    }

    @Override
    public void Disable() {
        enabled = false;
    }
}