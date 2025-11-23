package com.godpalace.godbox.ui.box_ui;

import javax.swing.*;
import java.awt.*;

public class BoxLabel extends JLabel {
    private int alignment;
    
    public BoxLabel(String text) {
        super(text);
        setBackground(new Color(25, 25, 25));
    }
    public BoxLabel(String text, int alignment) {
        super(text);
        this.alignment = alignment;
        setBackground(new Color(25, 25, 25));
    }

    @Override
    public void paintComponent(Graphics g) {
        Font font = getFont();
        font = new Font(font.getName(), font.getStyle(), font.getSize() - 1);
        font.deriveFont(getHeight() * 0.7f);
        g.setFont(font);

        // 画背景
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // 画文字
        g.setColor(getForeground());
        int x = 0, y = 0;
        if (alignment == SwingConstants.CENTER) {
            x = (getWidth() - g.getFontMetrics().stringWidth(getText())) / 2;
            y = (getHeight() + g.getFontMetrics().getAscent()) / 2 - 1;
        }
        if (alignment == SwingConstants.RIGHT) {
            x = getWidth() - g.getFontMetrics().stringWidth(getText()) - 1;
            y = (getHeight() + g.getFontMetrics().getAscent()) / 2 - 1;
        }
        if (alignment == SwingConstants.LEFT) {
            x = 1;
            y = (getHeight() + g.getFontMetrics().getAscent()) / 2 - 1;
        }
        g.drawString(getText(), x, y);
    }
}
