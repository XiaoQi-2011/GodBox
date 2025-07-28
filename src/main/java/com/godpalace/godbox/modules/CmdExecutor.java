package com.godpalace.godbox.modules;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class CmdExecutor implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean Enable = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "CMD执行器";
    private final String description = "自动执行CMD命令(一行为一条命令)";
    private final String typeListName = "SYSTEM";

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("CMD命令", "long-string", "", "", "", ""),
            new ModuleArg("执行间隔(ms)", "int", 100, 0, 2147483647, 1),
            new ModuleArg("是否循环", "boolean", true, "", "", "")
    };

    private boolean loop = true;

    Thread thread = new Thread(() -> {
        while (true) {
            if (Enable.get()) {
                String[] cmds = args[0].getValue().toString().split("\n");
                for (String cmd : cmds) {
                    if (!cmd.trim().isEmpty()) {
                        try {
                            Runtime.getRuntime().exec(cmd.trim());
                            Thread.sleep(Integer.parseInt(args[1].getValue().toString()));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (!loop) {
                    Enable.set(false);
                }
            }
        }
    });

    @Override
    public boolean isEnabled() {
        return Enable.get();
    }

    @Override
    public void Enable() {
        loop = Boolean.parseBoolean(args[2].getValue().toString());

        if (!thread.isAlive()) {
            thread.start();
        }
        Enable.set(true);
    }

    @Override
    public void Disable() {
        Enable.set(false);
    }

    @Override
    public void init() {}
}
