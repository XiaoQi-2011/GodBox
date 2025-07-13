package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.mgr.ModuleMgr;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

@Getter
@Setter
public class BoxFileChooser extends BoxPanel {
    private final JFileChooser fileChooser = new JFileChooser();
    private final BoxTextField textField = new BoxTextField("");
    private final BoxButton button = new BoxButton("C");

    public BoxFileChooser(String filePath) {
        super();
        textField.setText(filePath);
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
        button.setPreferredSize(new Dimension(UiSettings.moduleHeight, UiSettings.moduleHeight));
        add(button, BorderLayout.EAST);

        button.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(BoxFileChooser.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        textField.addKeyListener(new ModuleSettingsPanel.CloseKeyListener());
    }

    public String getSelectedFile() {
        return textField.getText();
    }
}
