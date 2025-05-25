package com.godpalace.godbox.Module;

import com.godpalace.godbox.Utils.Setting;
import com.godpalace.godbox.Utils.SettingGroup;

import javax.swing.*;
import java.awt.image.BufferedImage;

public interface Module {

    int getID();
    String getName();
    String getTooltip();
    ModuleUI getModulePanel();
    SettingGroup getSettings();

    void Enable();
    void Disable();
    boolean isEnabled();
}
