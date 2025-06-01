package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class BoxButton extends JComponent implements MouseListener {
    private static final Color defalutBackgroundColor = new Color(0, 0, 0, UiSettings.backgroundOpacity);

    @Setter
    @Getter
    private String text;

    private final AtomicBoolean isAction = new AtomicBoolean(false);
    private final AtomicBoolean isEnter = new AtomicBoolean(false);

    private BoxButton(String text) {
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
        g.drawRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        // 画文字
        g.setColor(getTextColor());
        int x = (getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
        int y = (getHeight() - g.getFontMetrics().getAscent()) / 2;
        g.drawString(text, x, y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        isAction.set(true);
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isAction.set(false);
        repaint();
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

        return defalutBackgroundColor;
    }

    private Color getTextColor() {
        if (isAction.get()) {
            return Color.WHITE;
        }

        return UiSettings.themeColor;
    }
}
