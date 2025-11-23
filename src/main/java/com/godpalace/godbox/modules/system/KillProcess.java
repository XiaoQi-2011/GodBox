package com.godpalace.godbox.modules.system;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.box_ui.BoxComboBox;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.godpalace.godbox.util.CharToStringUtil;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class KillProcess implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "杀死进程";
    private final String description = "杀死指定进程";
    private final String typeListName = "SYSTEM";

    private final BoxComboBox.BoxEnum exeMode = new BoxComboBox.BoxEnum(new String[]{
            "CMD命令", "系统API"
    }, 1);

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("进程名(一行一个)", "long-string", "", "", "", ""),
            new ModuleArg("杀死全部进程", "boolean", false, "", "", ""),
            new ModuleArg("执行模式", "enum", exeMode.toSerializableString(), "", "", ""),
            new ModuleArg("忽略自己", "boolean", true, "", "", ""),
            new ModuleArg("忽略系统进程", "boolean", true, "", "", ""),
            new ModuleArg("是否循环", "boolean", true, "", "", ""),
            new ModuleArg("是否强制杀死", "boolean", false, "", "", "")
    };

    private boolean killAll = false;
    private boolean ignoreSelf = true;
    private boolean ignoreSystem = true;
    private boolean loop = true;
    private boolean force = false;

    private final Thread thread = new Thread(() -> {
        while (true) {
            if (enabled.get()) {
                Map<Integer, String> processPIDs = new HashMap<>();

                int currentProcessId = Kernel32.INSTANCE.GetCurrentProcessId();
                Tlhelp32.PROCESSENTRY32 processEntry = new Tlhelp32.PROCESSENTRY32();
                processEntry.dwSize = new WinDef.DWORD(processEntry.size());

                WinNT.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));
                Kernel32.INSTANCE.Process32First(snapshot, processEntry);
                do {
                    int pid = processEntry.th32ProcessID.intValue();
                    String name = CharToStringUtil.charToString(processEntry.szExeFile);

                    if (ignoreSelf && pid == currentProcessId) continue;
                    if (pid <= 1000 || name.equals("svchost.exe") && ignoreSystem) continue;

                    processPIDs.put(pid, name);
                } while (Kernel32.INSTANCE.Process32Next(snapshot, processEntry));
                Kernel32.INSTANCE.CloseHandle(snapshot);

                String[] processNames = args[0].getValue().toString().split("\n");
                String mode = exeMode.getSelectedItem();
                if (!killAll) {
                    try {
                        for (String processName : processNames) {
                            if (mode.equals("CMD命令")) {
                                Runtime.getRuntime().exec("taskkill " + (force ? "/f " : "") + " /im " + processName);
                            } else if (mode.equals("系统API")) {
                                for (Map.Entry<Integer, String> entry : processPIDs.entrySet()) {
                                    int pid = entry.getKey();
                                    if (!processPIDs.get(pid).equals(processName)) continue;
                                    WinNT.HANDLE handle = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_ALL_ACCESS, false, pid);
                                    if (handle != null) {
                                        Kernel32.INSTANCE.TerminateProcess(handle, 0);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        for (Map.Entry<Integer, String> entry : processPIDs.entrySet()) {
                            int pid = entry.getKey();

                            if (mode.equals("CMD命令")) {
                                Runtime.getRuntime().exec("taskkill " + (force ? "/f " : "") + " /pid " + pid);
                            } else if (mode.equals("系统API")) {
                                WinNT.HANDLE handle = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_ALL_ACCESS, false, pid);
                                if (handle != null) {
                                    Kernel32.INSTANCE.TerminateProcess(handle, 0);
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("All processes killed.");
                    enabled.set(false);
                }

                if (!loop) {
                    enabled.set(false);
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    @Override
    public void Enable() {
        killAll = Boolean.parseBoolean(args[1].getValue().toString());
        exeMode.serialize(args[2].getValue().toString());
        ignoreSelf = Boolean.parseBoolean(args[3].getValue().toString());
        ignoreSystem = Boolean.parseBoolean(args[4].getValue().toString());
        loop = Boolean.parseBoolean(args[5].getValue().toString());
        force = Boolean.parseBoolean(args[6].getValue().toString());

        if (!thread.isAlive()) {
            thread.start();
        }
        enabled.set(true);
    }

    @Override
    public void Disable() {
        enabled.set(false);
    }

    @Override
    public void init() {}
}
