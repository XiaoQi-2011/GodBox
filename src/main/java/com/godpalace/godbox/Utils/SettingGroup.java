package com.godpalace.godbox.Utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

@Slf4j
public class SettingGroup {
    private final Vector<Setting> settings = new Vector<Setting>();
    @Getter
    private final JPanel panel = new JPanel();

    public SettingGroup(Setting[] settings) {
        setSettings(settings);
        createPanel();
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    public void setSettings(Setting[] settings){
        for(Setting setting : settings) {
            this.addSetting(setting);
        }
    }

    public String getValue(String name) {
        for (Setting setting : settings) {
            if (setting.name.equals(name)) {
                return setting.getValue();
            }
        }
        return null;
    }

    public void setValue(String name, String[] values) {
        for(Setting setting : settings) {
            if(setting.name.equals(name)) {
                setting.setValue(values);
            }
        }
    }

    public void createPanel() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setSize(100, settings.size() * 50);

        for (Setting setting : settings) {
            panel.add(setting.getPanel());
        }
    }
}
