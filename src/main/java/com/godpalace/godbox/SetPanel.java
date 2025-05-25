package com.godpalace.godbox;

import com.godpalace.godbox.Utils.Setting;
import com.godpalace.godbox.Utils.SettingGroup;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class SetPanel extends JPanel {
    public SettingGroup settingGroup;
    public SetPanel() {
        init();
    }

    public void init() {
        this.removeAll();
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        if (settingGroup != null) {
            this.add(settingGroup.getPanel(), BorderLayout.CENTER);
        }
    }

    public void setGUI(SettingGroup settingGroup){
        this.settingGroup = settingGroup;
        init();
    }


}
