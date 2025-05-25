package com.godpalace.godbox;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.godpalace.godbox.mgr.ModuleMgr;
import com.godpalace.godbox.ui.BackgroundFrame;
import com.godpalace.godbox.ui.TypeList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Main {
    @Getter
    private static final ConcurrentHashMap<TypeLists, TypeList> typeLists = new ConcurrentHashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (Exception e) {
                log.error("Error unregistering native hook", e);
            }
        }));
    }

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
            UiSettings.initialize();
            ModuleMgr.initialize();

            // 创建UI
            BackgroundFrame ui = new BackgroundFrame();

            // 创建类型列表
            for (TypeLists typeList : TypeLists.values()) {
                typeLists.put(typeList, new TypeList(typeList.name()));
            }

            // 添加模块
            for (com.godpalace.godbox.module.Module module : ModuleMgr.getModules().values()) {
                TypeLists typeList = module.getTypeListID();

                if (typeLists.containsKey(typeList)) {
                    typeLists.get(typeList).addModule(module);
                } else {
                    log.warn("Module {} has no type list", module.getName());
                }
            }

            // 添加类型列表
            for (int i = 0; i < typeLists.size(); i++) {
                TypeList typeList = typeLists.get(TypeLists.values()[i]);
                typeList.setLocation(10 + i * (UiSettings.moduleHeight * 5 + 10), 10);

                ui.getContentPane().add(typeList);
            }

            GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
                @Override
                public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
                    // 右Shift键打开UI
                    if (nativeEvent.getKeyCode() == UiSettings.openUiKey && !ui.isVisible()) {
                        ui.setVisible(true);
                    }
                }
            });
        } catch (Exception e) {
            log.error("Error initializing GodBox", e);
        }
    }
}
