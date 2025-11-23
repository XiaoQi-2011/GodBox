package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.mgr.ModuleMgr;
import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.ui.box_ui.BoxLabel;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class HUDFrame extends JFrame {
    @Setter
    private Location location;
    private final Vector<BoxLabel> labels = new Vector<>();
    private int maxWidth = 0;
    private int totalHeight = 0;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public HUDFrame() {

        setLayout(null);
        setAlwaysOnTop(true);
        setType(JFrame.Type.UTILITY);
        setResizable(false);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setFocusable(true);
    }

    private final Thread updateThread = new Thread(() -> {
        while (true) {
            if (isRunning.get()) {
                try {
                    Thread.sleep(100);
                    loadModules();
                    updateUI();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    public void start() {
        isRunning.set(true);
        setVisible(true);
        if (!updateThread.isAlive()) {
            updateThread.start();
        }
    }

    public void stop() {
        isRunning.set(false);
        setVisible(false);
    }

    private void loadModules() {
        maxWidth = 0;
        totalHeight = 0;
        labels.clear();
        for (Module module : ModuleMgr.getModules()) {
            if (module.isEnabled()) {
                String name = module.getDisplayName();
                int alignment = (location == Location.LEFT ? SwingConstants.LEFT : SwingConstants.RIGHT);
                int width = Toolkit.getDefaultToolkit().getFontMetrics(UiSettings.font).stringWidth(name);
                int height = Toolkit.getDefaultToolkit().getFontMetrics(UiSettings.font).getHeight();
                BoxLabel label = new BoxLabel(name, alignment);
                maxWidth = Math.max(maxWidth, width);
                totalHeight += height;
                labels.add(label);
            }
        }
    }

    private void updateUI() {
        int size = labels.size();
        setSize(maxWidth, totalHeight);
        int x = (location == Location.LEFT? 0 : Toolkit.getDefaultToolkit().getScreenSize().width - getWidth());
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2;
        setLocation(x, y);
        for (int i = 0; i < size; i++) {
            BoxLabel label = labels.get(i);
            label.setBounds(0, i * label.getHeight(), maxWidth, label.getHeight());
            add(label);
        }
    }

    public enum Location {
        LEFT, RIGHT
    }
}
