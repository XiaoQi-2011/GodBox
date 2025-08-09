package com.godpalace.godbox.modules;

import com.godpalace.godbox.mgr.ModuleMgr;
import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.BoxComboBox;
import com.godpalace.godbox.ui.HUDFrame;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class HUD implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "HUD";
    private final String description = "显示模块状态";
    private final String typeListName = "SCREEN";

    BoxComboBox.BoxEnum location = new BoxComboBox.BoxEnum(new String[]{
            "左侧", "右侧"
    }, 1);

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("显示位置", "enum", location.toSerializableString(), "", "", ""),
    };

    private final HUDFrame frame = new HUDFrame();

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    @Override
    public void Enable() {
        enabled.set(true);
        location.serialize(args[0].getValue().toString());
        String locationStr = location.getSelectedItem();
        HUDFrame.Location loc = locationStr.equals("左侧")? HUDFrame.Location.LEFT : HUDFrame.Location.RIGHT;
        frame.setLocation(loc);
        frame.start();
    }

    @Override
    public void Disable() {
        enabled.set(false);
        frame.stop();
    }

    @Override
    public void init() {}
}
