package com.godpalace.godbox.modules;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.BoxComboBox;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.godpalace.godbox.util.AntiMostTopUtil;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lombok.Getter;
import lombok.Setter;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.godpalace.godbox.util.CharToStringUtil.charToString;

@Getter
public class AntiMostTop implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "反置顶";
    private final String description = "将强制置顶的窗口取消置顶";
    private final String typeListName = "SCREEN";

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
    };

    private final AntiMostTopUtil antiMostTopUtil = new AntiMostTopUtil();

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    @Override
    public void Enable() {
        enabled.set(true);
        antiMostTopUtil.start();
    }

    @Override
    public void Disable() {
        enabled.set(false);
        antiMostTopUtil.stop();
    }
}
