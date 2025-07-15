package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

@Getter
@Setter
public class BoxTextArea extends JTextArea {
    public BoxTextArea(String text) {
        super(text);
        init();
    }

    private void init() {
        setFont(UiSettings.font);
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        setCaretColor(Color.WHITE);
        setSelectionColor(UiSettings.themeColor);
        setSelectedTextColor(Color.WHITE);
        setBorder(new LineBorder(UiSettings.themeColor));
        setOpaque(false);
        setLineWrap(true);
        setWrapStyleWord(true);
    }
}
