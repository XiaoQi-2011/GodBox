package com.godpalace.godbox.ui.box_ui;

import com.godpalace.godbox.UiSettings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class BoxButton extends JComponent implements MouseListener {
    @Setter
    @Getter
    private String text;

    private final AtomicBoolean isAction = new AtomicBoolean(false);
    private final AtomicBoolean isEnter = new AtomicBoolean(false);

    public BoxButton(String text) {
        this.text = text;

        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setFont(UiSettings.font);

        // 画背景
        g.setColor(getBackgroundColor());
        g.fillRect(0, 0, getWidth(), getHeight());

        // 画边框
        g.setColor(UiSettings.themeColor);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        // 画文字
        g.setColor(getTextColor());
        int x = (getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
        int y = (getHeight() + g.getFontMetrics().getAscent()) / 2 - 1;
        g.drawString(text, x, y);
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            isAction.set(true);
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            isAction.set(false);

            if (isEnter.get()) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    for (ActionListener listener : listenerList.getListeners(ActionListener.class)) {
                        listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text));
                    }
                }
            }

            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isEnter.set(true);
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isEnter.set(false);
        repaint();
    }

    private Color getBackgroundColor() {
        if (isAction.get()) {
            return UiSettings.themeColor;
        }

        if (isEnter.get()) {
            return UiSettings.themeColor.brighter();
        }

        return Color.BLACK;
    }

    private Color getTextColor() {
        if (isAction.get() || isEnter.get()) {
            return Color.WHITE;
        }

        return UiSettings.themeColor;
    }
}
