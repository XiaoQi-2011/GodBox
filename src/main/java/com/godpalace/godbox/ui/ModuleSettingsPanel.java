package com.godpalace.godbox.ui;

import com.godpalace.godbox.module.ModuleArg;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@Slf4j
public class ModuleSettingsPanel extends JPanel {
    private final ModuleArg[] args;

    public ModuleSettingsPanel(ModuleArg[] args) {
        this.args = args;
        initComponents();
    }

    // 初始化配置面板
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (ModuleArg arg : args) {
            JLabel label = new JLabel(arg.getName() + ":");
            JComponent input = switch (arg.getType().getName()) {
                case "java.lang.String" -> {
                    JTextField textField = new JTextField((String) arg.getValue());

                    // 监听输入
                    textField.addActionListener(e -> arg.setValue(textField.getText()));

                    yield textField;
                }

                case "java.lang.Character", "char" -> {
                    JLabel keyLabel = new JLabel("Null");
                    keyLabel.setOpaque(true);
                    keyLabel.setFocusable(true);

                    // 点击时开始监听
                    keyLabel.addFocusListener(new FocusListener() {
                        private String last = "Null";

                        @Override
                        public void focusGained(FocusEvent e) {
                            last = keyLabel.getText();
                            keyLabel.setText("...");
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            if (keyLabel.getText().equals("...")) {
                                keyLabel.setText(last);
                                last = "Null";
                            }
                        }
                    });

                    // 监听输入
                    keyLabel.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            // 判断是否有焦点
                            if (keyLabel.isFocusOwner()) {
                                arg.setValue(e.getKeyChar());
                                keyLabel.setText(e.getKeyChar() + "");
                            }
                        }
                    });

                    yield keyLabel;
                }

                case "short", "java.lang.Short" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            (short) arg.getValue(),
                            (short) arg.getMin(),
                            (short) arg.getMax(),
                            (short) arg.getStep());

                    JSpinner spinner = new JSpinner(model);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((short) spinner.getValue()));

                    yield spinner;
                }

                case "int", "Java.lang.Integer" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            (int) arg.getValue(),
                            (int) arg.getMin(),
                            (int) arg.getMax(),
                            (int) arg.getStep());

                    JSpinner spinner = new JSpinner(model);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((int) spinner.getValue()));

                    yield spinner;
                }

                case "long", "java.lang.Long" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            (long) arg.getValue(),
                            (long) arg.getMin(),
                            (long) arg.getMax(),
                            (long) arg.getStep());

                    JSpinner spinner = new JSpinner(model);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((long) spinner.getValue()));

                    yield spinner;
                }

                case "boolean", "java.lang.Boolean" -> {
                    JCheckBox checkbox = new JCheckBox();
                    checkbox.setSelected((boolean) arg.getValue());

                    // 监听输入
                    checkbox.addActionListener(e -> arg.setValue(checkbox.isSelected()));

                    yield checkbox;
                }

                case "float", "java.lang.Float" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            (float) arg.getValue(),
                            (float) arg.getMin(),
                            (float) arg.getMax(),
                            (float) arg.getStep());

                    // 格式化显示
                    String format = "#0." + "#".repeat(((String) arg.getStep()).length() - 2);

                    JSpinner spinner = new JSpinner(model);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, format));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((float) spinner.getValue()));

                    yield spinner;
                }

                case "double", "java.lang.Double" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            (double) arg.getValue(),
                            (double) arg.getMin(),
                            (double) arg.getMax(),
                            (double) arg.getStep());

                    // 格式化显示
                    String format = "#0." + "#".repeat(((String) arg.getStep()).length() - 2);

                    JSpinner spinner = new JSpinner(model);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, format));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((double) spinner.getValue()));

                    yield spinner;
                }

                default -> throw new IllegalStateException("Unexpected value: " + arg.getType().getName());
            };

            // 创建面板
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // 添加到面板中, 并添加到父面板中
            panel.add(label, BorderLayout.WEST);
            panel.add(input, BorderLayout.EAST);
            add(panel);
        }
    }
}
