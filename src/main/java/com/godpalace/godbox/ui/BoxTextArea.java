package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.module_mgr.ModuleArg;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

@Getter
@Setter
public class BoxTextArea extends JTextArea {
    private final ModuleSettingsPanel moduleSettingsPanel;
    public BoxTextArea(String text, ModuleSettingsPanel moduleSettingsPanel) {
        super(text);
        this.moduleSettingsPanel = moduleSettingsPanel;
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
        setWrapStyleWord(true);

        addCaretListener(e -> {
            if (getText().isEmpty() || getWidth() <= 0) {
                return;
            }
            String[] texts = getText().split("\n");
            FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(UiSettings.font);
            int maxWidth = 0;
            for (String text : texts) {
                maxWidth = Math.max(maxWidth, metrics.stringWidth(text));
            }
            int width;
            if (maxWidth < getWidth() - 5) {
                if (moduleSettingsPanel.getWidth() <= 400) {
                    width = moduleSettingsPanel.getWidth();
                } else {
                    width = moduleSettingsPanel.getWidth() - getWidth() + maxWidth + 5;
                }
            } else {
                width = moduleSettingsPanel.getWidth() - getWidth() + maxWidth + 5;
            }
            if (width < 400) width = 400;
            moduleSettingsPanel.setSize(width,
                    moduleSettingsPanel.getTopPanel().getPreferredSize().height + moduleSettingsPanel.getCenterPanel().getPreferredSize().height + 5);
            moduleSettingsPanel.revalidate();
        });
    }
}
