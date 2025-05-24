package com.godpalace.godbox;

import com.godpalace.godbox.Utils.Setting;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class SetPanel extends JPanel {
    public Vector<Setting> settings = new Vector<>();
    public HashMap<String, String> map = new HashMap<>();

    public SetPanel() {
    }

    public void init() {
        this.setBackground(Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        for (Setting setting : settings) {
            this.add(setting.getPanel());
        }
    }

    public void setConfigs(Setting[]  configs) {
        this.settings.addAll(Arrays.asList(configs));
        this.removeAll();
        this.init();
    }

    public void getAllValues() {
        for (Setting setting : settings) {
            String value = setting.getValue();
            map.put(setting.name, value);
        }
    }
}
