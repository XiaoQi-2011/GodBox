package com.godpalace.godbox.mgr;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import lombok.Getter;

public class KeyBindListener {
    public KeyBindListener() {}

    @Getter
    private boolean isRunning = false;

    private final NativeKeyListener keyListener = new NativeKeyListener() {
        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
            String key = NativeKeyEvent.getKeyText(e.getKeyCode());
        }
    };

    public void start(){
        GlobalScreen.addNativeKeyListener(keyListener);
        isRunning = true;
    }

    public void stop(){
        GlobalScreen.removeNativeKeyListener(keyListener);
        isRunning = false;
    }
}
