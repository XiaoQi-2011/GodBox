package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

@Getter
@Setter
public class KeyBindField extends JComponent {
    private String keyBind;

    private boolean isSelecting = false;
    private Vector<String> keyList = new Vector<>();
    private final KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            String s = KeyEvent.getKeyText(e.getKeyCode());
            if (s.equals("Esc") || keyList.contains("None")) {
                s = "None";
                keyList.clear();
            }
            if (!keyList.contains(s)) {
                keyList.add(s);
            }

            repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // 将焦点丢失
            setFocusable(false);
            isSelecting = false;

            keyBind = "";
            for (int i = 0; i < keyList.size(); i++) {
                if (i != keyList.size() - 1) {
                    keyBind += keyList.get(i) + " + ";
                } else {
                    keyBind += keyList.get(i);
                }
            }

            keyList.clear();
            repaint();
        }
    };

    public KeyBindField(String keyBind) {
        this.keyBind = keyBind;

        setSize(UiSettings.moduleHeight, UiSettings.moduleHeight);
        addKeyListener(keyAdapter);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isSelecting = true;
                setFocusable(true);
                requestFocusInWindow();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        FontMetrics fm = g.getFontMetrics();
        int height = getHeight();
        int width = getWidth();

        // 画边框
        g.setColor(UiSettings.themeColor);
        g.drawRect(0, 0, width - 1, height - 1);

        // 画字符
        g.setColor(Color.WHITE);
        g.setFont(UiSettings.font);

        int y = (height - fm.getHeight()) / 2 + fm.getAscent();

        if (isSelecting) {
            String ellipsis = "...";
            int x = (width - fm.stringWidth(ellipsis)) / 2;
            g.drawString(ellipsis, x, y);
        } else {
            String s = keyBind;
            int x = (width - fm.stringWidth(s)) / 2;
            g.drawString(s, x, y);
        }
    }

    public void addActionListener(ActionListener listener) {
        if (listener != null) {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    listener.actionPerformed(new ActionEvent(KeyBindField.this, 0, keyBind));
                }
            });
        }
    }
}
