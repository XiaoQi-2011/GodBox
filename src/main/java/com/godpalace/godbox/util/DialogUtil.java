package com.godpalace.godbox.util;

import com.godpalace.godbox.UiSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class DialogUtil {
    public static void showModuleInfo(String message, long duration) {
        try {
            Color backgroundColor = new Color(25, 25, 25);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            FontMetrics metrics = toolkit.getFontMetrics(UiSettings.font);

            JFrame frame = new JFrame();
            frame.setSize(metrics.stringWidth(message) + 10, metrics.getHeight() + 10);
            frame.setLocationRelativeTo(null);
            frame.setType(JFrame.Type.UTILITY);
            frame.setAlwaysOnTop(true);
            frame.setUndecorated(true);
            frame.setResizable(false);
            frame.setBackground(backgroundColor);

            JLabel label = new JLabel(message);
            label.setBackground(backgroundColor);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setForeground(Color.WHITE);
            label.setBorder(BorderFactory.createLineBorder(UiSettings.themeColor));
            label.setFont(UiSettings.font);
            frame.setContentPane(label);

            new Thread(() -> {
                try {
                    Thread.sleep(duration);
                    frame.dispose();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        synchronized (frame) {
                            frame.notifyAll();
                        }
                    } catch (Exception ignored) {
                    }
                }
            }).start();

            frame.setVisible(true);
            synchronized (frame) {
                frame.wait();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void showModuleInfo(String message) {
        try {
            Color backgroundColor = new Color(25, 25, 25);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            FontMetrics metrics = toolkit.getFontMetrics(UiSettings.font);

            JFrame frame = new JFrame();
            frame.setSize(metrics.stringWidth(message) + 10, metrics.getHeight() + 10);
            frame.setLocationRelativeTo(null);
            frame.setType(JFrame.Type.UTILITY);
            frame.setAlwaysOnTop(true);
            frame.setUndecorated(true);
            frame.setResizable(false);
            frame.setBackground(backgroundColor);

            JLabel label = new JLabel(message);
            label.setBackground(backgroundColor);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setForeground(Color.WHITE);
            label.setBorder(BorderFactory.createLineBorder(UiSettings.themeColor));
            label.setFont(UiSettings.font);
            frame.setContentPane(label);

            frame.setFocusable(true);
            frame.requestFocus();
            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        frame.dispose();
                        synchronized (frame) {
                            frame.notifyAll();
                        }
                    }
                }
            });

            frame.setVisible(true);
            synchronized (frame) {
                frame.wait();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
