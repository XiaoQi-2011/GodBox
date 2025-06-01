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

    public BoxLabel() {
        this("");
    }

    public BoxLabel(String text) {
        super();
        this.text = text;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setFont(UiSettings.font);
        g.setColor(UiSettings.themeColor);

        int x = (getWidth() - g.getFontMetrics().stringWidth(text)) / 2;
        int y = (getHeight() - g.getFontMetrics().getAscent()) / 2;
        g.drawString(text, x, y);
    }
}
