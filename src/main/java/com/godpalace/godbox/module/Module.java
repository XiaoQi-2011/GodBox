package com.godpalace.godbox.module;

import com.godpalace.godbox.TypeLists;
import lombok.Getter;

public abstract class Module {
    @Getter
    private boolean enabled = false;

    public abstract int getID();
    public abstract TypeLists getTypeListID();
    public abstract String getName();
    public abstract String getTooltip();

    public void Enable() {
        enabled = true;
        onEnable();
    }

    public void Disable() {
        enabled = false;
        onDisable();
    }

    protected abstract void onEnable();
    protected abstract void onDisable();
}
