package com.godpalace.godbox;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.godpalace.godbox.Module.ModuleMgr;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static ClientUI clientUI;
    public static void main(String[] args) throws Exception {
        GlobalScreen.registerNativeHook();

        try {
            ModuleMgr.initialize();
        } catch (Exception e) {
            log.error("Error initializing modules", e);
        }
        clientUI = new ClientUI();
        clientUI.init();
        log.info("GodBoxUI init done");
    }
}
