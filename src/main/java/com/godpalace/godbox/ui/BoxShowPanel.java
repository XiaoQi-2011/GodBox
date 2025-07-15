package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.module_mgr.ModuleArg;
import lombok.Getter;

import java.awt.*;

@Getter
public class BoxShowPanel extends BoxPanel{
    private final BoxTextArea textArea = new BoxTextArea("");
    private final BoxButton button = new BoxButton("获取");
    private final ModuleArg moduleArg;

    public BoxShowPanel(ModuleArg moduleArg) {
        super();
        this.moduleArg = moduleArg;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(UiSettings.moduleHeight * 2, UiSettings.moduleHeight));
        button.addActionListener(e -> {
            String value = moduleArg.getValue().toString();
            textArea.setText(value);
        });
        textArea.setEditable(false);

        add(textArea, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
    }
}
