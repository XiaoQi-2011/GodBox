package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.module_mgr.ModuleArg;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class BoxShowPanel extends BoxPanel{
    private final BoxTextArea textArea = new BoxTextArea("");
    private final BoxButton button = new BoxButton("获取");
    private ModuleArg moduleArg;

    private final AtomicBoolean autoShow = new AtomicBoolean(true);
    private int delay = 100;
    private final Timer timer = new Timer(delay, e -> {
        if (autoShow.get()) {
            String value = moduleArg.getValue().toString();
            textArea.setText(value);
        }
    });

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
        setAutoShow(true, 100);
    }

    public void setAutoShow(boolean autoShow, int delay) {
        this.autoShow.set(autoShow);
        this.delay = delay;
        timer.setDelay(delay);
        if (autoShow) {
            timer.start();
        } else {
            timer.stop();
        }
    }
}
