package com.godpalace.godbox.modules;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.godpalace.godbox.Main;
import com.godpalace.godbox.mgr.ModuleMgr;
import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.BoxComboBox;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.godpalace.godbox.util.DialogUtil;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class KeyRecorder implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private boolean enabled = false;

    @Setter
    private String keyBind = "None";

    private final String displayName = "键盘记录器";
    private final String description = "记录键盘按键并保存到文件中";
    private final String typeListName = "SYSTEM";

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("文件路径", "string", "key_record.txt", "", "", ""),
    };

    private int currentTime = 0;
    private String lastKey = "";
    private int counts = 1;
    private final AtomicBoolean flag = new AtomicBoolean(false);
    private final NativeKeyListener keyListener = new NativeKeyListener() {
        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
            String key = NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
            if (key.equals(lastKey) && flag.get()) {
                currentTime = 0;
                counts ++;
                return;
            }
            if (!key.equals(lastKey) && flag.get()) {
                currentTime = 0;
                saveToFile();
                counts = 1;
                lastKey = key;
                return;
            }

            currentTime = 0;
            flag.set(true);
            lastKey = key;
        }
    };
    private final Thread thread = new Thread(() -> {
        while (true) {
            if (!flag.get()) continue;
            while (currentTime <= 500) {
                try {
                    Thread.sleep(1);
                    currentTime++;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            saveToFile();
            flag.set(false);
            currentTime = 0;
            counts = 1;
        }
    });
    private void saveToFile() {
        String filePath = args[0].getValue().toString();
        try {
            FileWriter writer = new FileWriter(filePath, true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ");
            Date now = new Date(); // 获取当前日期和时间
            String formattedDate = dateFormat.format(now); // 格式化当前日期和时间
            if (counts > 1) {
                writer.write(formattedDate + lastKey + " (" + counts + ")\n");
            } else {
                writer.write(formattedDate + lastKey + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Enable() {
        enabled = true;
        if (!thread.isAlive()) {
            thread.start();
        }
        GlobalScreen.addNativeKeyListener(keyListener);
    }

    @Override
    public void Disable() {
        enabled = false;
        GlobalScreen.removeNativeKeyListener(keyListener);
    }

    @Override
    public void init() {
        if (!new File(args[0].getValue().toString()).exists()) {
            try {
                new File(args[0].getValue().toString()).createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
