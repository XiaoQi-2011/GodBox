package com.godpalace.godbox.ui;

import com.godpalace.godbox.Main;
import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.module.ModuleArg;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

@Slf4j
public class ModuleSettingsPanel extends BoxPanel implements MouseListener, MouseMotionListener {
    private final ModuleArg[] args;
    private final String moduleName;
    private final String description;
    private static final Color DISABLED_COLOR = new Color(25, 25, 25);

    public ModuleSettingsPanel(String moduleName, String description, ModuleArg[] args) {
        super();
        this.args = args;
        this.moduleName = moduleName;
        this.description = description;

        // 设置面板
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setSize(300, (args.length + 2) * UiSettings.moduleHeight);
        setBorder(new LineBorder(UiSettings.themeColor));
        setFocusable(true);
        addKeyListener(new KeyListener());

        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2;
        setLocation(x, y);

        initComponents();
    }

    // 初始化配置面板
    private void initComponents() {
        // 添加标题
        JPanel titlePanel = new JPanel();
        titlePanel.setSize(getWidth(), UiSettings.moduleHeight);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(UiSettings.themeColor);

        BoxLabel title = new BoxLabel(moduleName);
        title.setColor(Color.WHITE);
        titlePanel.add(title, BorderLayout.CENTER);
        add(titlePanel);

        // 添加描述
        BoxLabel dsp = new BoxLabel(description);
        dsp.setSize(getWidth(), UiSettings.moduleHeight);
        dsp.setPos(SwingConstants.LEFT);
        dsp.setPreferredSize(new Dimension(getWidth(), UiSettings.moduleHeight));
        dsp.setColor(Color.WHITE);
        add(dsp);

        // 添加配置项
        for (ModuleArg arg : args) {
            BoxLabel label = new BoxLabel(arg.getName() + ":");
            label.setColor(Color.WHITE);
            label.setPos(SwingConstants.CENTER);

            JComponent input = switch (arg.getType()) {
                case "string" -> {
                    JTextField textField = new JTextField(arg.getValue() + "");
                    textField.setFont(UiSettings.font);

                    // 监听输入
                    textField.addActionListener(e -> arg.setValue(textField.getText()));
                    textField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue(textField.getText());
                        }
                    });

                    yield textField;
                }

                case "char" -> {
                    BoxLabel keyLabel = new BoxLabel(arg.getValue() + "");
                    keyLabel.setFocusable(true);
                    keyLabel.setHasBorder(true);

                    // 点击时开始监听
                    keyLabel.addFocusListener(new FocusListener() {
                        private String last = keyLabel.getText();

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

                case "byte" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Byte.parseByte(arg.getValue() + ""),
                            Byte.parseByte(arg.getMin() + ""),
                            Byte.parseByte(arg.getMax() + ""),
                            Byte.parseByte(arg.getStep() + ""));

                    JSpinner spinner = new JSpinner(model);
                    spinner.setFont(UiSettings.font);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((byte) spinner.getValue()));

                    yield spinner;
                }

                case "short" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Short.parseShort(arg.getValue() + ""),
                            Short.parseShort(arg.getMin() + ""),
                            Short.parseShort(arg.getMax() + ""),
                            Short.parseShort(arg.getStep() + ""));

                    JSpinner spinner = new JSpinner(model);
                    spinner.setFont(UiSettings.font);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((short) spinner.getValue()));

                    yield spinner;
                }

                case "int" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Integer.parseInt(arg.getValue() + ""),
                            Integer.parseInt(arg.getMin() + ""),
                            Integer.parseInt(arg.getMax() + ""),
                            Integer.parseInt(arg.getStep() + ""));

                    JSpinner spinner = new JSpinner(model);
                    spinner.setFont(UiSettings.font);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((int) spinner.getValue()));

                    yield spinner;
                }

                case "long" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Long.parseLong(arg.getValue() + ""),
                            Long.parseLong(arg.getMin() + ""),
                            Long.parseLong(arg.getMax() + ""),
                            Long.parseLong(arg.getStep() + ""));

                    JSpinner spinner = new JSpinner(model);
                    spinner.setFont(UiSettings.font);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((long) spinner.getValue()));

                    yield spinner;
                }

                case "boolean" -> {
                    JCheckBox checkbox = new JCheckBox();
                    checkbox.setSelected(Boolean.parseBoolean(arg.getValue() + ""));

                    // 监听输入
                    checkbox.addActionListener(e -> arg.setValue(checkbox.isSelected()));

                    yield checkbox;
                }

                case "float" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Float.parseFloat(arg.getValue() + ""),
                            Float.parseFloat(arg.getMin() + ""),
                            Float.parseFloat(arg.getMax() + ""),
                            Float.parseFloat(arg.getStep() + ""));

                    // 格式化显示
                    String format = "#0." + "#".repeat(((String) arg.getStep()).length() - 2);

                    JSpinner spinner = new JSpinner(model);
                    spinner.setFont(UiSettings.font);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, format));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((float) spinner.getValue()));

                    yield spinner;
                }

                case "double" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Double.parseDouble(arg.getValue() + ""),
                            Double.parseDouble(arg.getMin() + ""),
                            Double.parseDouble(arg.getMax() + ""),
                            Double.parseDouble(arg.getStep() + ""));

                    // 格式化显示
                    String format = "#0." + "#".repeat(((String) arg.getStep()).length() - 2);

                    JSpinner spinner = new JSpinner(model);
                    spinner.setFont(UiSettings.font);
                    spinner.setEditor(new JSpinner.NumberEditor(spinner, format));

                    // 监听输入
                    spinner.addChangeListener(e -> arg.setValue((double) spinner.getValue()));

                    yield spinner;
                }

                default -> {
                    log.warn("Not support type: {}", arg.getType());

                    JTextField textField = new JTextField(arg.getValue() + "");
                    textField.setFont(UiSettings.font);

                    // 监听输入
                    textField.addActionListener(e -> arg.setValue(textField.getText()));
                    textField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue(textField.getText());
                        }
                    });

                    yield textField;
                }
            };

            input.setFont(UiSettings.font);
            input.setFocusable(true);
            input.addKeyListener(new KeyListener());

            // 创建面板
            BoxPanel panel = new BoxPanel();
            panel.setLayout(new BorderLayout());

            // 添加到面板中, 并添加到父面板中
            panel.add(label, BorderLayout.WEST);
            panel.add(input, BorderLayout.EAST);
            add(panel);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // 拖动时的鼠标位置
    private int dragX;
    private int dragY;

    @Override
    public void mousePressed(MouseEvent e) {
        // 限制拖动范围为模块标题
        if (e.getY() <= UiSettings.moduleHeight && e.getButton() == MouseEvent.BUTTON1) {
            dragX = e.getX();
            dragY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragX = 0;
        dragY = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // 移动列表位置
        if (dragX != 0 && dragY != 0) {
            setLocation(e.getXOnScreen() - dragX, e.getYOnScreen() - dragY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    static class KeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Main.getUi().setVisible(true);
                Main.getSettings().setVisible(false);
            }
        }
    }
}
