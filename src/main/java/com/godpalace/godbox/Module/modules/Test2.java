package com.godpalace.godbox.Module.modules;

import com.godpalace.godbox.Module.Module;
import com.godpalace.godbox.Module.ModuleUI;
import com.godpalace.godbox.Utils.Setting;
import com.godpalace.godbox.Utils.SettingGroup;

public class Test2 implements Module {
    private static final int ID = 2;
    private static final String NAME = "Test2";
    private static final String TOOLTIP = "This is a test module.";
    private static ModuleUI UI = null;
    private static boolean on = false;

    public static SettingGroup settings = new SettingGroup(new Setting[] {
            new Setting(Setting.Type.TEXTFIELD, "Text", "Hello World!"),
            new Setting(Setting.Type.TEXTAREA, "Text Area", "This is a test text area."),
    });

    @Override
    public void Enable() {
        on = true;
        System.out.println("Test module enabled.");
        System.out.println(settings.getValue("Text"));
        System.out.println(settings.getValue("Text Area"));
        System.out.println(settings.getValue("Button"));
        System.out.println(settings.getValue("Checkbox"));
    }

    @Override
    public void Disable() {
        on = false;
        System.out.println("Test module disabled.");
    }

    @Override
    public boolean isEnabled() {
        return on;
    }

    public Test2() {
        UI = new ModuleUI(this);
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTooltip() {
        return TOOLTIP;
    }

    @Override
    public ModuleUI getModulePanel() {
        return UI;
    }

    @Override
    public SettingGroup getSettings() {
        return settings;
    }
}
