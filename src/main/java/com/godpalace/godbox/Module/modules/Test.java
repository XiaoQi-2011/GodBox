package com.godpalace.godbox.Module.modules;

import com.godpalace.godbox.Module.Module;
import com.godpalace.godbox.Module.ModuleUI;

public class Test implements Module {
    public static final int ID = 1;
    public static final String NAME = "Test";
    public static final String TOOLTIP = "This is a test module.";
    public static ModuleUI UI = null;
    public static boolean on = false;

    public Test() {
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
    public void Enable() {
        System.out.println("Test module enabled.");
    }

    @Override
    public void Disable() {
        System.out.println("Test module disabled.");
    }
}
