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
    private final Vector<String> names = new Vector<>();
    private int xOffset = 0;
    private int yOffset = 0;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    private final JPanel contentPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(getForeground());
            g.drawLine(0, 0, getWidth(), 0);
            int x = 0, y = 0;
            for (String name : names) {
                int width = Toolkit.getDefaultToolkit().getFontMetrics(UiSettings.font).stringWidth(name);
                int height = Toolkit.getDefaultToolkit().getFontMetrics(UiSettings.font).getHeight();
                x = (location == Location.LEFT ? 0 : getWidth() - width);
                y += height;
                g.setFont(UiSettings.font);
                g.drawString(name, x, y);
            }
        }
    };

    public HUDFrame() {
        Color background = new Color(0, 0, 0, 0);
        setAlwaysOnTop(true);
        setType(JFrame.Type.UTILITY);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setBackground(background);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        contentPanel.setBackground(background);
        contentPanel.setForeground(UiSettings.themeColor);
        contentPanel.setFont(UiSettings.font);
        setContentPane(contentPanel);
    }

    private final Thread updateThread = new Thread(() -> {
        while (true) {
            if (isRunning.get()) {
                try {
                    Thread.sleep(100);
                    update();
                    repaint();
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

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    private void update() {
        int maxWidth = 0;
        int totalHeight = 0;
        names.clear();
        for (Module module : ModuleMgr.getModules()) {
            if (module.isEnabled()) {
                String name = module.getDisplayName();
                int width = Toolkit.getDefaultToolkit().getFontMetrics(UiSettings.font).stringWidth(name);
                int height = Toolkit.getDefaultToolkit().getFontMetrics(UiSettings.font).getHeight();

                maxWidth = Math.max(maxWidth, width);
                totalHeight += height;
                names.add(name);
            }
        }
        int x = (location == Location.LEFT? 0 : Toolkit.getDefaultToolkit().getScreenSize().width - getWidth());
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2;
        setSize(maxWidth + 5, totalHeight + 5);
        setLocation(x + xOffset, y + yOffset);
    }

    public enum Location {
        LEFT, RIGHT
    }
}
