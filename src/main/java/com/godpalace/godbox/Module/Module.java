package com.godpalace.godbox.Module;

import javax.swing.*;
import java.awt.image.BufferedImage;

public interface Module {

    int getID();
    String getName();
    String getTooltip();
    ModuleUI getModulePanel();

    void Enable();
    void Disable();
}
