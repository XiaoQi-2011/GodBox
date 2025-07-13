package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class BoxColorChooser extends BoxPanel{
    private Color color;
    private final BoxPanel colorPanel = new BoxPanel();
    private final BoxButton colorButton = new BoxButton("Choose");

    public BoxColorChooser(Color color) {
        super();
        this.color = color;
        colorPanel.setBackground(color);
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        colorPanel.setPreferredSize(new Dimension(UiSettings.moduleHeight, UiSettings.moduleHeight));
        colorButton.setPreferredSize(new Dimension(UiSettings.moduleHeight * 2 + 5, UiSettings.moduleHeight));

        add(colorPanel, BorderLayout.WEST);
        add(colorButton, BorderLayout.EAST);

    }
}
