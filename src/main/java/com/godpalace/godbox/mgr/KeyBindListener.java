package com.godpalace.godbox.mgr;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.godpalace.godbox.Main;
import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.util.DialogUtil;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class KeyBindListener {
    public KeyBindListener() {}

    @Getter
    private static boolean isRunning = false;
    private static final Vector<String> keyList = new Vector<>();
    private static boolean compare(String[] keyPressed) {
        boolean result = true;
        if (KeyBindListener.keyList.size()!= keyPressed.length) {
            return false;
        }
        for (String key : keyPressed) {
            if (!KeyBindListener.keyList.contains(key)) {
                result = false;
                break;
            }
        }
        return result;
    }

    private static final NativeKeyListener keyListener = new NativeKeyListener() {
        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
            String key = NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
            if (!keyList.contains(key)) {
                keyList.add(key);
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
            if (keyList.isEmpty()) {
                return;
            }
            for (Module module : ModuleMgr.getModules()) {
                String[] keyBind = module.getKeyBind().split(" \\+ ");
                if (compare(keyBind)) {
                    if (module.isEnabled()) {
                        new Thread(() -> DialogUtil.showModuleInfo("[" + module.getDisplayName() + "] 关闭", Color.RED, 2000)).start();
                        module.Disable();
                    } else {
                        new Thread(() -> DialogUtil.showModuleInfo("[" + module.getDisplayName() + "] 开启", Color.GREEN, 2000)).start();
                        module.Enable();
                    }
                    SwingUtilities.updateComponentTreeUI(Main.getUi());
                }
            }
            keyList.clear();
        }
    };

    public static void start(){
        GlobalScreen.addNativeKeyListener(keyListener);
        isRunning = true;
    }

    public static void stop(){
        GlobalScreen.removeNativeKeyListener(keyListener);
        isRunning = false;
    }
}
