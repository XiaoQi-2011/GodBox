package com.godpalace.godbox.util;

import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.ui.box_ui.BoxButton;
import com.godpalace.godbox.ui.box_ui.BoxPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class DialogUtil {
    public static void showModuleInfo(String message, Color color, long duration) {
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
                    int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
                    for (int i = 1; i <= metrics.stringWidth(message) + 10; i++) {
                        int x = screenWidth - i;
                        frame.setLocation(x, 0);
                        Thread.sleep(1);
                    }
                    Thread.sleep(duration);
                    for (int i = metrics.stringWidth(message) + 10; i >= 1; i--) {
                        int x = screenWidth - i;
                        frame.setLocation(x, 0);
                        Thread.sleep(1);
                    }
                    frame.dispose();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        synchronized (frame) {
                            frame.notifyAll();
                        }
                    } catch (Exception ignored) {}
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

    public static void showTextMessage(String message, String title) {
        try {
            Color backgroundColor = new Color(25, 25, 25);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            FontMetrics metrics = toolkit.getFontMetrics(UiSettings.font);

            JFrame frame = new JFrame();
            String[] messages = message.split("\n");
            int height = 0, maxWidth = 0;
            for (String msg : messages) {
                maxWidth = Math.max(maxWidth, metrics.stringWidth(msg));
                height += metrics.getHeight();
            }
            frame.setSize(maxWidth + 10, height + UiSettings.moduleHeight + 5);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());
            frame.setType(JFrame.Type.UTILITY);
            frame.setAlwaysOnTop(true);
            frame.setUndecorated(true);
            frame.setResizable(false);
            frame.setBackground(backgroundColor);

            BoxPanel panel = new BoxPanel();
            panel.setLayout(new BorderLayout());
            panel.setSize(maxWidth + 10, UiSettings.moduleHeight);
            panel.setBorder(BorderFactory.createLineBorder(UiSettings.themeColor));
            JLabel label = new JLabel(title);
            label.setForeground(Color.WHITE);
            panel.add(label, BorderLayout.WEST);
            BoxButton closeButton = new BoxButton("×");
            closeButton.setPreferredSize(new Dimension(UiSettings.moduleHeight, UiSettings.moduleHeight));
            closeButton.setForeground(Color.RED);
            closeButton.addActionListener(e -> {
                frame.dispose();
                synchronized (frame) {
                    frame.notifyAll();
                }
            });
            panel.add(closeButton, BorderLayout.EAST);
            frame.add(panel, BorderLayout.NORTH);

            JTextArea textArea = new JTextArea(message);
            textArea.setBackground(backgroundColor);
            textArea.setForeground(Color.WHITE);
            textArea.setBorder(BorderFactory.createLineBorder(UiSettings.themeColor));
            textArea.setEditable(false);
            textArea.setFont(UiSettings.font);
            frame.add(textArea, BorderLayout.CENTER);
            frame.setFocusable(true);

            textArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        frame.dispose();
                        synchronized (frame) {
                            frame.notifyAll();
                        }
                    }
                }
            });
            frame.addMouseWheelListener(e -> {
                int rotation = e.getWheelRotation();
                int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
                if (rotation < 0 && frame.getY() < 0) {
                    frame.setLocation(frame.getX(), frame.getY() + 20);
                }
                if (rotation > 0 && frame.getY() + frame.getHeight() > screenHeight) {
                    frame.setLocation(frame.getX(), frame.getY() - 20);
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
