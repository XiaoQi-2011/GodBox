package com.godpalace.godbox.ui.box_ui;

import com.godpalace.godbox.UiSettings;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

@Slf4j
public class BoxSpinner extends JSpinner {
    public BoxSpinner(SpinnerNumberModel model) {
        super(model);
        init();
    }

    private void init() {
        setFont(UiSettings.font);
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        setBorder(new LineBorder(UiSettings.themeColor));
    }

    @Override
    public void setEditor(JComponent editor) {
        if (!(editor instanceof DefaultEditor editor1)) return;

        // 设置文本框
        JFormattedTextField textField = editor1.getTextField();
        if (textField != null) {
            textField.setFont(UiSettings.font);
            textField.setBackground(Color.BLACK);
            textField.setForeground(Color.WHITE);
            textField.setCaretColor(Color.WHITE);
            textField.setSelectionColor(UiSettings.themeColor);
            textField.setSelectedTextColor(Color.WHITE);
        }

        super.setEditor(editor1);
    }
}
