package com.godpalace.godbox.modules;

import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class KillProcess implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean Enable = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "杀死进程";
    private final String description = "杀死指定进程";
    private final String typeListName = "SYSTEM";

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("进程名", "string", "", "", "", ""),
            new ModuleArg("杀死全部进程", "boolean", false, "", "", ""),
            new ModuleArg("忽略自己", "boolean", true, "", "", ""),
            new ModuleArg("是否循环", "boolean", true, "", "", ""),
            new ModuleArg("是否强制杀死", "boolean", false, "", "", "")
    };

    private boolean killAll = false;
    private boolean ignoreSelf = true;
    private boolean loop = true;
    private boolean force = false;

    Thread thread = new Thread(() -> {
        while (true) {
            if (Enable.get()) {
                int currentProcessId = Kernel32.INSTANCE.GetCurrentProcessId();
                String processName = args[0].getValue().toString();
                if (!killAll) {
                    try {
                        Runtime.getRuntime().exec("taskkill " + (force? "/f " : "") + " /im " + processName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Tlhelp32.PROCESSENTRY32 processEntry = new Tlhelp32.PROCESSENTRY32();
                    processEntry.dwSize = new WinDef.DWORD(processEntry.size());

                    WinNT.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));
                    Kernel32.INSTANCE.Process32First(snapshot, processEntry);
                    do {
                        int pid = processEntry.th32ProcessID.intValue();
                        String name = Arrays.toString(processEntry.szExeFile);
                        if (ignoreSelf && pid == currentProcessId) continue;
                        if (pid <= 1000 || name.equals("svchost.exe")) continue;
                        try {
                            Runtime.getRuntime().exec("taskkill " + (force? "/f " : "") + " /pid " + pid);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } while (Kernel32.INSTANCE.Process32Next(snapshot, processEntry));
                    System.out.println("All processes killed.");
                    Kernel32.INSTANCE.CloseHandle(snapshot);
                }
                if (!loop) {
                    Enable.set(false);
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });

    @Override
    public boolean isEnabled() {
        return Enable.get();
    }

    @Override
    public void Enable() {
        killAll = Boolean.parseBoolean(args[1].getValue().toString());
        ignoreSelf = Boolean.parseBoolean(args[2].getValue().toString());
        loop = Boolean.parseBoolean(args[3].getValue().toString());
        force = Boolean.parseBoolean(args[4].getValue().toString());

        if (!thread.isAlive()) {
            thread.start();
        }
        Enable.set(true);
    }

    @Override
    public void Disable() {
        Enable.set(false);
    }
}
