package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BackgroundFrame extends JFrame {
    public BackgroundFrame() {
        setLayout(null);
        setAlwaysOnTop(true);
        setType(JFrame.Type.UTILITY);
        setResizable(false);
        setUndecorated(true);
        setLocation(0, 0);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBackground(new Color(0, 0, 0, UiSettings.backgroundOpacity));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setVisible(false);
                }
            }
        });
    }
}
