package com.godpalace.godbox.modules;


import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.BoxComboBox;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.godpalace.godbox.util.CharToStringUtil;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class AutoKillProcess implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean Enable = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "自动杀进程";
    private final String description = "当进程数量超过最大值时，自动杀死指定进程。";
    private final String typeListName = "SYSTEM";

    private final BoxComboBox.BoxEnum killMode = new BoxComboBox.BoxEnum(new String[]{
            "杀死所有进程", "杀死数量最多的进程"
    }, 0);

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("最大进程数量", "int", 100, 1, 10000, 1),
            new ModuleArg("执行模式", "enum", killMode.toSerializableString(), "", "", ""),
            new ModuleArg("忽略自己", "boolean", true, "", "", ""),
            new ModuleArg("忽略系统进程", "boolean", true, "", "", ""),
            new ModuleArg("是否强制杀死", "boolean", false, "", "", ""),
            new ModuleArg("获取进程数", "show", "", "", "", "")
    };

    private boolean ignoreSelf = true;
    private boolean ignoreSystem = true;
    private boolean force = false;

    Thread thread = new Thread(() -> {
        while (true) {
            if (Enable.get()) {
                int maxCount = Integer.parseInt(args[0].getValue().toString());
                int currentProcessId = Kernel32.INSTANCE.GetCurrentProcessId();
                Map<String, Integer> processCount = new HashMap<>();
                int count = 0;
                Tlhelp32.PROCESSENTRY32 processEntry = new Tlhelp32.PROCESSENTRY32();
                processEntry.dwSize = new WinDef.DWORD(processEntry.size());

                WinNT.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));
                Kernel32.INSTANCE.Process32First(snapshot, processEntry);
                do {
                    int pid = processEntry.th32ProcessID.intValue();
                    String name = CharToStringUtil.charToString(processEntry.szExeFile);

                    if (ignoreSelf && pid == currentProcessId) continue;
                    if (pid <= 1000 || name.equals("svchost.exe") && ignoreSystem) continue;

                    count++;
                    processCount.put(name, processCount.getOrDefault(name, 0) + 1);
                    System.out.println(name + " : " + processCount.get(name));
                } while (Kernel32.INSTANCE.Process32Next(snapshot, processEntry));
                Kernel32.INSTANCE.CloseHandle(snapshot);

                args[5].setValue(count);
                if (count > maxCount) {
                    String mode = killMode.getSelectedItem();
                    if (mode.equals("杀死数量最多的进程")) {
                        String maxName = "";
                        int maxCountTemp = 0;
                        for (Map.Entry<String, Integer> entry : processCount.entrySet()) {
                            if (entry.getValue() > maxCountTemp) {
                                maxName = entry.getKey();
                                maxCountTemp = entry.getValue();
                            }
                        }
                        try {
                            Runtime.getRuntime().exec("taskkill " + (force ? "/f " : "") + " /im " + maxName);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (mode.equals("杀死所有进程")) {
                        for (Map.Entry<String, Integer> entry : processCount.entrySet()) {
                            String name = entry.getKey();
                            try {
                                Runtime.getRuntime().exec("taskkill " + (force ? "/f " : "") + " /im " + name);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
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
        killMode.serialize(args[1].getValue().toString());
        ignoreSelf = Boolean.parseBoolean(args[2].getValue().toString());
        ignoreSystem = Boolean.parseBoolean(args[3].getValue().toString());
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
