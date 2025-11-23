package com.godpalace.godbox.modules.system;


import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.box_ui.BoxComboBox;
import com.godpalace.godbox.ui.box_ui.BoxShowPanel;
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
public class AutoKillProcess implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "自动杀进程";
    private final String description = "当进程数量超过最大值时，自动杀死指定进程。";
    private final String typeListName = "SYSTEM";

    private final BoxComboBox.BoxEnum killMode = new BoxComboBox.BoxEnum(new String[]{
            "杀死所有进程", "杀死数量最多的进程"
    }, 1);

    private final BoxComboBox.BoxEnum exeMode = new BoxComboBox.BoxEnum(new String[]{
            "CMD命令", "系统API"
    }, 1);

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("最大进程数量", "int", 110, 1, 2147483647, 1),
            new ModuleArg("杀死模式", "enum", killMode.toSerializableString(), "", "", ""),
            new ModuleArg("执行模式", "enum", exeMode.toSerializableString(), "", "", ""),
            new ModuleArg("忽略自己", "boolean", true, "", "", ""),
            new ModuleArg("忽略系统进程", "boolean", true, "", "", ""),
            new ModuleArg("是否强制杀死", "boolean", false, "", "", ""),
            new ModuleArg("获取进程数", "show", "", "", "", "")
    };

    private boolean ignoreSelf = true;
    private boolean ignoreSystem = true;
    private boolean force = false;

    private final Thread thread = new Thread(() -> {
        while (true) {
            if (enabled.get()) {
                Map<String, Integer> processCount = new HashMap<>();
                Map<Integer, String> processPIDs = new HashMap<>();

                int maxCount = Integer.parseInt(args[0].getValue().toString());
                int currentProcessId = Kernel32.INSTANCE.GetCurrentProcessId();
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
                    processPIDs.put(pid, name);
                } while (Kernel32.INSTANCE.Process32Next(snapshot, processEntry));
                Kernel32.INSTANCE.CloseHandle(snapshot);

                args[6].setValue(count);
                if (count > maxCount) {
                    String mode = killMode.getSelectedItem();
                    String eMode = exeMode.getSelectedItem();

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
                            if (eMode.equals("CMD命令")) {
                                Runtime.getRuntime().exec("taskkill " + (force ? "/f " : "") + " /im " + maxName);
                            } else if (eMode.equals("系统API")) {
                                for (Map.Entry<Integer, String> entry : processPIDs.entrySet()) {
                                    int pid = entry.getKey();
                                    if (!processPIDs.get(pid).equals(maxName)) continue;
                                    WinNT.HANDLE handle = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_ALL_ACCESS, false, pid);
                                    if (handle != null) {
                                        Kernel32.INSTANCE.TerminateProcess(handle, 0);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (mode.equals("杀死所有进程")) {
                        try {
                            for (Map.Entry<Integer, String> entry : processPIDs.entrySet()) {
                                int pid = entry.getKey();

                                if (eMode.equals("CMD命令")) {
                                    Runtime.getRuntime().exec("taskkill " + (force ? "/f " : "") + " /pid " + pid);
                                } else if (eMode.equals("系统API")) {
                                    WinNT.HANDLE handle = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_ALL_ACCESS, false, pid);
                                    if (handle != null) {
                                        Kernel32.INSTANCE.TerminateProcess(handle, 0);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    });

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    @Override
    public void Enable() {
        killMode.serialize(args[1].getValue().toString());
        exeMode.serialize(args[2].getValue().toString());
        ignoreSelf = Boolean.parseBoolean(args[3].getValue().toString());
        ignoreSystem = Boolean.parseBoolean(args[4].getValue().toString());
        force = Boolean.parseBoolean(args[5].getValue().toString());

        if (!thread.isAlive()) {
            thread.start();
        }
        enabled.set(true);
    }

    @Override
    public void Disable() {
        enabled.set(false);
        thread.interrupt();
        args[6].setValue("");
    }

    @Override
    public void init() {
        BoxShowPanel showPanel = (BoxShowPanel) args[6].getComponent();
        showPanel.setAutoShow(true, 100);
        showPanel.setOnClickEvent(() -> {
            if (args[6].getValue() == null || args[6].getValue().toString().isEmpty()) {
                args[6].setValue("请先开启模块");
            }
        });
    }
}


