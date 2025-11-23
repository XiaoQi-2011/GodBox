package com.godpalace.godbox.modules.system;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.godpalace.jprotector.ProcessProtector;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProtectProcess implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private boolean enabled;

    @Setter
    private String keyBind = "None";

    private final String displayName = "保护进程";
    private final String description = "保护指定进程，防止进程被杀死(保护自己后将不能保护其他进程)";
    private final String typeListName = "SYSTEM";

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("进程pid", "int", 0, 0, 65535, 1),
            new ModuleArg("保护自己", "boolean", false, "", "", "")
    };


    @Override
    public void Enable() {
        enabled = true;
        ProcessProtector protector = ProcessProtector.getInstance();
        int pid = Integer.parseInt(args[0].getValue().toString());
        boolean protectSelf = Boolean.parseBoolean(args[1].getValue().toString());

        if (pid != 0) {
            protector.protect(pid);
        }
        if (protectSelf) {
            protector.protect();
        }
    }

    @Override
    public void Disable() {
        enabled = false;
        ProcessProtector protector = ProcessProtector.getInstance();
        if (protector.isProtected()) {
            protector.unprotect();
        }
    }

    @Override
    public void init() {}
}