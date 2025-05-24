package com.godpalace.godbox;

import com.godpalace.godbox.Module.Module;
import com.godpalace.godbox.Module.ModuleMgr;
import com.godpalace.godbox.Module.ModuleUI;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class ModulePanel extends JPanel {

    public Thread ResizerThread = new Thread(() -> {
        while (true) {
            for (Module module : ModuleMgr.getModules().values()) {
                ModuleUI panel = module.getModulePanel();
                panel.setSize(this.getWidth(), panel.getHeight());
            }
        }
    });

    public ModulePanel() {
        try {
            ModuleMgr.initialize();
        } catch (Exception e) {
            log.error("Error initializing modules", e);
        }
        init();
    }

    public void init(){
        this.setBackground(Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        for (Module module : ModuleMgr.getModules().values()) {
            System.out.println("Loading module: " + module.getName());
            this.add(module.getModulePanel());
        }
       ResizerThread.start();
    }


}
