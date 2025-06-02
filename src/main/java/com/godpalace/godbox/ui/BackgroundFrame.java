package com.godpalace.godbox.ui;

import com.godpalace.godbox.Main;
import com.godpalace.godbox.UiSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BackgroundFrame extends JFrame {
    private static final String[] texts = {
            "GodBox v1.0.0 by GodPalace",
            "按下ESC键退出"
    };

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
        setFocusable(true);

        // 添加文字
        JLabel[] labels = new JLabel[texts.length];
        for (int i = 0; i < texts.length; i++) {
            labels[i] = new JLabel(texts[i]);
            labels[i].setFont(UiSettings.font);
            labels[i].setForeground(Color.WHITE);
            labels[i].setBounds(10, getHeight() - (texts.length - i) * (UiSettings.font.getSize() + 5) - 40, UiSettings.font.getSize() * texts[i].length(), UiSettings.font.getSize());

            getContentPane().add(labels[i]);
        }
    }
}
