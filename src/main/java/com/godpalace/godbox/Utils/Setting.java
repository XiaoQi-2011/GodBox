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

    JTextField textfield;
    JTextArea textarea;
    JButton button;
    JComboBox<String> combobox;

    private final JLabel label;
    @Getter
    private final JPanel panel;

    public Setting(int type, String name, String value) {
        this.type = type;
        this.name = name;
        this.values = new String[]{value};
        this.label = new JLabel(name + ": ");
        this.panel = new JPanel();
        init();
    }
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
        CreateComponent();
    }

    public void CreateComponent() {
        switch (type) {
            case Type.TEXTFIELD: {
                textfield = new JTextField();
                panel.add(textfield);
                break;
            }
            case Type.TEXTAREA: {
                textarea = new JTextArea();
                panel.add(textarea);
                break;
            }
            case Type.BUTTON: {
                button = new JButton();
                panel.add(button);
                break;
            }
            case Type.CHECKBOX: {
                combobox = new JComboBox<>();
                panel.add(combobox);
                break;
            }
            default:
                break;
        }
        setValue(values);
    }

    public String getValue() {
        return switch (type) {
            case Type.TEXTFIELD -> textfield.getText();
            case Type.TEXTAREA -> textarea.getText();
            case Type.BUTTON -> button.getText();
            case Type.CHECKBOX -> Objects.toString(combobox.getSelectedItem());
            default -> "";
        };
    }

    public void setValue(String[] values) {
        switch (type) {
            case Type.TEXTFIELD: {
                textfield.setText(values[0]);
                break;
            }
            case Type.TEXTAREA: {
                textarea.setText(values[0]);
                break;
            }
            case Type.BUTTON: {
                button.setText(values[0]);
                button.setToolTipText(values[1]);
                break;
            }
            case Type.CHECKBOX: {
                combobox.removeAll();
                for (String value : values) {
                    combobox.addItem(value);
                }
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