package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;

import javax.swing.*;
import java.awt.*;

public class BoxPanel extends JPanel {
    public BoxPanel() {
        super();
        setOpaque(false);
        setBackground(new Color(0, 0, 0, UiSettings.backgroundOpacity));
        setFocusable(false);
    }
}
