package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoxCheckBox extends JCheckBox {
    public BoxCheckBox() {
        super();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 画边框
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        // 画选择
        if (isSelected()) {
            g.setColor(UiSettings.themeColor);
            g.fillRect(3, 3, getWidth() - 6, getHeight() - 6);
        }
    }

    @Override
    public void addActionListener(ActionListener l) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                l.actionPerformed(new ActionEvent(this, 0, "click"));
            }
        });
    }
}
