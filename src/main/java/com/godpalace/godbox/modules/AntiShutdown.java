package com.godpalace.godbox.modules;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.BoxComboBox;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class AntiShutdown implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean Enabled = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "防关机";
    private final String description = "防止电脑自动关机。";
    private final String typeListName = "SYSTEM";

    BoxComboBox.BoxEnum mode = new BoxComboBox.BoxEnum(new String[]{
            "CMD"
    });

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("模式", "enum", mode.toSerializableString(), "", "", ""),
    };

    private String modeValue;
    private final Thread thread = new Thread(() -> {
        while (true) {
            if (Enabled.get()) {
                if (modeValue.equals("CMD")) {
                    try {
                        Runtime.getRuntime().exec("shutdown -a");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    });

    @Override
    public boolean isEnabled() {
        return Enabled.get();
    }

    @Override
    public void Enable() {
        Enabled.set(true);
        mode.serialize(args[0].getValue().toString());
        modeValue = mode.getSelectedItem();

        if (!thread.isAlive()) {
            thread.start();
        }
    }

    @Override
    public void Disable() {
        Enabled.set(false);
        thread.interrupt();
    }
}