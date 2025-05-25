package com.godpalace.godbox.module;

import com.godpalace.godbox.TypeLists;

public class TestModule extends Module {
    @Override
    public int getID() {
        return ModuleIdMap.ID_TEST;
    }

    @Override
    public TypeLists getTypeListID() {
        return TypeLists.TEST;
    }

    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public String getTooltip() {
        return "This is a test module.";
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }
}
