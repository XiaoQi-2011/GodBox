package com.godpalace.godbox;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.godpalace.godbox.mgr.ModuleMgr;
import com.godpalace.godbox.modules.Module;
import com.godpalace.godbox.ui.BackgroundFrame;
import com.godpalace.godbox.ui.TypeList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Main {
    @Getter
    private static final ConcurrentHashMap<TypeLists, TypeList> typeLists = new ConcurrentHashMap<>();

    @Getter
    private static final BackgroundFrame ui = new BackgroundFrame();

    @Getter
    private static final BackgroundFrame settings = new BackgroundFrame();

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
        Locale.setDefault(Locale.CHINA);

        ui.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    ui.setVisible(false);
                }
            }
        });

        settings.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    settings.setVisible(false);
                    ui.setVisible(true);
                }
            }
        });

        try {
            GlobalScreen.registerNativeHook();
            UiSettings.initialize();
            ModuleMgr.initialize();

            // 创建类型列表
            for (TypeLists typeList : TypeLists.values()) {
                typeLists.put(typeList, new TypeList(typeList.getName()));
            }

            // 添加模块
            for (Module module : ModuleMgr.getModules()) {
                TypeLists typeList = TypeLists.valueOf(module.getTypeListName());

                if (typeLists.containsKey(typeList)) {
                    typeLists.get(typeList).addModule(module);
                } else {
                    log.warn("Module {} has no type list", module.getDisplayName());
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
                    if (nativeEvent.getKeyCode() == UiSettings.openUiKey && !ui.isVisible() && !settings.isVisible()) {
                        ui.setVisible(true);
                    }
                }
            });

            log.info("GodBox started successfully");
        } catch (Exception e) {
            log.error("Error initializing GodBox", e);
        }
    }
}
