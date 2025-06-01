package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Setter
@Getter
public class BoxLabel extends JComponent {
    private String text;
    private boolean hasBorder = false;
    private int pos = SwingConstants.CENTER;
    private Color color = UiSettings.themeColor;

    public BoxLabel() {
        this("");
    }

    public BoxLabel(String text) {
        this.text = text;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setFont(UiSettings.font);
        g.setColor(color);

        // 画边框
        if (hasBorder) {
            g.drawRect(0, 0, getWidth(), getHeight());
        }

        // 画文字
        int y = (getHeight() - g.getFontMetrics().getAscent()) / 2;
        g.drawString(text, getTextX(g), y);
    }

    private int getTextX(Graphics g) {
        if (pos == SwingConstants.CENTER) {
            return (getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
        }

        return 5;
    }
}
