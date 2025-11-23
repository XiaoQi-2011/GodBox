package com.godpalace.godbox.ui.box_ui;

import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.godpalace.godbox.util.DialogUtil;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class BoxShowPanel extends BoxPanel{
    private BoxTextArea textArea;
    private final BoxButton button = new BoxButton("获取");
    private ShowType showType = ShowType.TEXTAREA;
    private ModuleArg moduleArg;
    @Setter
    private OnClickEvent onClickEvent;

    private final AtomicBoolean autoShow = new AtomicBoolean(false);
    private int delay = 100;
    private final Timer timer = new Timer(delay, e -> {
        if (autoShow.get()) {
            String value = moduleArg.getValue().toString();
            textArea.setText(value);
        }
    });

    public BoxShowPanel(ModuleArg moduleArg, ModuleSettingsPanel moduleSettingsPanel) {
        super();
        this.moduleArg = moduleArg;
        this.textArea = new BoxTextArea("", moduleSettingsPanel);
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(UiSettings.moduleHeight * 2, UiSettings.moduleHeight));
        button.addActionListener(e -> {
            if (onClickEvent!= null) {
                onClickEvent.onClick();
            }
            String value = moduleArg.getValue().toString();
            if (showType == ShowType.TEXTAREA) {
                textArea.setText(value);
            } else if (showType == ShowType.DIALOG) {
                new Thread(() -> DialogUtil.showTextMessage(value, moduleArg.getName())).start();
            }
        });
        textArea.setEditable(false);

        add(textArea, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
    }

    public void setAutoShow(boolean autoShow, int delay) {
        this.autoShow.set(autoShow);
        this.delay = delay;
        timer.setDelay(delay);
        if (autoShow && !timer.isRunning()) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    public void setShowType(ShowType showType) {
        this.showType = showType;
        if (showType == ShowType.TEXTAREA) {
            if (textArea.getParent() == null) {
                add(textArea, BorderLayout.CENTER);
            }
        } else if (showType == ShowType.DIALOG) {
            if (textArea.getParent() != null) {
                remove(textArea);
            }
        }
    }

    public enum ShowType {
        TEXTAREA, DIALOG
    }
    public interface OnClickEvent {
        void onClick();
    }
}
