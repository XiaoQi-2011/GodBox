package com.godpalace.godbox.modules.screen;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.godpalace.godbox.util.AntiMostTopUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;


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
            new ModuleArg("关闭模块后取消反置顶", "boolean", false, "", "", "")
    };

    private final AntiMostTopUtil antiMostTopUtil = new AntiMostTopUtil();

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    @Override
    public void Enable() {
        enabled.set(true);
        boolean isReMostTop = Boolean.parseBoolean(args[0].getValue().toString());
        antiMostTopUtil.setReMostTop(isReMostTop);
        antiMostTopUtil.start();
    }

    @Override
    public void Disable() {
        enabled.set(false);
        boolean isReMostTop = Boolean.parseBoolean(args[0].getValue().toString());
        antiMostTopUtil.setReMostTop(isReMostTop);
        antiMostTopUtil.stop();
    }

    @Override
    public void init() {}
}
