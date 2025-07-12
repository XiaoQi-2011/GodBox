package com.godpalace.godbox.module.modules;

import com.godpalace.godbox.mgr.GsonFactory;
import com.godpalace.godbox.module.ModuleArg;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public interface Module {

    String getDisplayName();
    String getDescription();
    String getVersion();
    String getAuthor();
    String getWebsite();

    String getTypeListName();

    boolean isEnabled();

    // 判断鼠标是否进入
    boolean isEntered();
    void setEntered(boolean entered);

    // 模块运行时参数
    ModuleArg[] getArgs();
    void setArgs(ModuleArg[] args);

    // 模块设置面板
    ModuleSettingsPanel getSettingsPanel();
    void setSettingsPanel(ModuleSettingsPanel settingsPanel);

    void Enable();

    void Disable();

    default String getJsonString() {
        return GsonFactory.getGson().toJson(this);
    }
}
