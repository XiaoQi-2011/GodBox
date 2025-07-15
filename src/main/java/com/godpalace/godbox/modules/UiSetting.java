package com.godpalace.godbox.modules;

import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.BoxComboBox;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
public class UiSetting implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private boolean enabled;

    @Setter
    private String keyBind = "None";

    private final String displayName = "UI设置";
    private final String description = "设置UI样式";
    private final String typeListName = "SETTINGS";

    private final BoxComboBox.BoxEnum ArgType = new BoxComboBox.BoxEnum(
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("背景透明度", "int", UiSettings.backgroundOpacity, 0, 255, 1),
            new ModuleArg("主题颜色", "color", UiSettings.themeColor.getRGB(), "", "", ""),
            new ModuleArg("字体", "enum", ArgType.toSerializableString(), "", "", ""),
            new ModuleArg("模块高度", "int", UiSettings.moduleHeight, 0, 100, 1)
    };


    @Override
    public void Enable() {
        enabled = true;
        ArgType.serialize(args[2].getValue().toString());
        UiSettings.backgroundOpacity = Integer.parseInt(args[0].getValue().toString());
        UiSettings.themeColor = new Color(Integer.parseInt(args[1].getValue().toString()));
        UiSettings.moduleHeight = Integer.parseInt(args[3].getValue().toString());
        UiSettings.font = new Font(ArgType.getSelectedItem(), Font.PLAIN, 14);
    }

    @Override
    public void Disable() {
        enabled = false;
    }
}
