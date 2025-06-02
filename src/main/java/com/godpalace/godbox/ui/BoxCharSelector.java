package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@Getter
@Setter
public class BoxCharSelector extends JComponent {
    private char c;

    private boolean isSelecting = false;
    private final KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            char inputChar = e.getKeyChar();
            // 只接受可见字符
            if (inputChar >= ' ' && inputChar <= '~') {
                c = Character.toUpperCase(inputChar);
                isSelecting = false;
                repaint();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // 将焦点丢失
            setFocusable(false);
            repaint();
        }
    };

    public BoxCharSelector(char c) {
        this.c = c;

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
        g.setColor(getParent().getBackground());
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
            String s = Character.toString(c).toUpperCase();
            int x = (width - fm.stringWidth(s)) / 2;
            g.drawString(s, x, y);
        }
    }

    public void addActionListener(ActionListener listener) {
        if (listener != null) {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    listener.actionPerformed(new ActionEvent(BoxCharSelector.this, 0, String.valueOf(c)));
                }
            });
        }
    }
}
