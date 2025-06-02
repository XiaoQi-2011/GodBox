package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

@Setter
@Getter
public class BoxTextField extends JTextField {
    public BoxTextField(String text) {
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
    }
}