package com.godpalace.godbox;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.godpalace.godbox.Module.ModuleMgr;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
public class Main {

    public static ClientUI clientUI;
    public static final String tip = "左键开启模块，右键开启设置";
    public static void main(String[] args) throws Exception {
        GlobalScreen.registerNativeHook();

        try {
            ModuleMgr.initialize();
        } catch (Exception e) {
            log.error("Error initializing modules", e);
        }
        clientUI = new ClientUI();
        clientUI.init();
        clientUI.setTip(tip);
        SwingUtilities.updateComponentTreeUI(clientUI);
        log.info("GodBoxUI init done");
    }
}
