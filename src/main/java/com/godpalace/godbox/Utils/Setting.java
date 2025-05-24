package com.godpalace.godbox.Utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Slf4j
public class Setting {
    public int type;
    public String name;
    public String[] values;

    JTextField component0;
    JTextArea component1;
    JButton component2;
    JComboBox<String> component3;

    private final JLabel label;
    @Getter
    private final JPanel panel;

    public Setting(int type, String name, String[] values) {
        this.type = type;
        this.name = name;
        this.values = values;
        this.label = new JLabel(name + ": ");
        this.panel = new JPanel();
        init();
    }

    public void init() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setSize(100, 50);
        panel.setBackground(Color.WHITE);
        panel.add(label);
        CreateValue();
    }

    public void CreateValue() {
        switch (type) {
            case Type.TEXTFIELD: {
                component0 = new JTextField();
                component0.setText(values[0]);
                panel.add(component0);
                break;
            }
            case Type.TEXTAREA: {
                component1 = new JTextArea();
                component1.setText(values[0]);
                panel.add(component1);
                break;
            }
            case Type.BUTTON: {
                component2 = new JButton();
                component2.setText(values[0]);
                component2.setToolTipText(values[1]);
                panel.add(component2);
                break;
            }
            case Type.CHECKBOX: {
                component3 = new JComboBox<>();
                for (String value : values) {
                    component3.addItem(value);
                }
                panel.add(component3);
                break;
            }
            default:
                break;
        }
    }

    public String getValue() {
        return switch (type) {
            case Type.TEXTFIELD -> component0.getText();
            case Type.TEXTAREA -> component1.getText();
            case Type.BUTTON -> component2.getText();
            case Type.CHECKBOX -> Objects.toString(component3.getSelectedItem());
            default -> "";
        };
    }

    public void setValue(String value) {
        switch (type) {
            case Type.TEXTFIELD: {
                component0.setText(value);
                break;
            }
            case Type.TEXTAREA: {
                component1.setText(value);
                break;
            }
            case Type.BUTTON: {
                component2.setText(value);
                break;
            }
            case Type.CHECKBOX: {
                component3.setSelectedItem(value);
                break;
            }
            default:
                break;
        }
    }

    public static class Type {
        public static final int TEXTFIELD = 0;
        public static final int TEXTAREA = 1;
        public static final int BUTTON = 2;
        public static final int CHECKBOX = 3;
    }
}