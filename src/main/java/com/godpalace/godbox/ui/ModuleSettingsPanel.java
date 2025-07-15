package com.godpalace.godbox.ui;

import com.godpalace.godbox.Main;
import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.modules.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

@Slf4j
public class ModuleSettingsPanel extends BoxPanel implements MouseListener, MouseMotionListener {
    private final Module module;
    private BoxPanel centerPanel;
    private BoxPanel topPanel;

    public ModuleSettingsPanel(Module module) {
        super();
        this.module = module;

        // 设置面板
        setLayout(new BorderLayout());
        setSize(400, (module.getArgs().length + 4) * (UiSettings.moduleHeight + 5) - 24);
        setBorder(new LineBorder(UiSettings.themeColor));
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(new CloseKeyListener());

        initComponents();
        SwingUtilities.updateComponentTreeUI(this);
        requestFocus();

        toCenter();
    }

    // 初始化配置面板
    private void initComponents() {
        // 添加标题
        JLabel title = new JLabel(module.getDisplayName());
        title.setOpaque(true);
        title.setForeground(Color.WHITE);
        title.setBackground(UiSettings.themeColor);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(UiSettings.font.deriveFont(18.0f));

        // 添加描述
        JLabel desc = new JLabel(module.getDescription());
        desc.setForeground(Color.WHITE);
        desc.setFont(UiSettings.font);

        topPanel = new BoxPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(desc, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // 设置布局
        centerPanel = new BoxPanel();
        BoxLayout boxLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
        centerPanel.setLayout(boxLayout);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        // 添加配置项
        for (ModuleArg arg : module.getArgs()) {
            JLabel label = new JLabel(arg.getName() + ":");
            label.setForeground(Color.WHITE);
            label.setFont(UiSettings.font);

            JComponent input = switch (arg.getType()) {
                case "string" -> {
                    BoxTextField textField = new BoxTextField(arg.getValue() + "");

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

                case "long-string" -> {
                    BoxTextArea textArea = new BoxTextArea(arg.getValue() + "");

                    // 监听输入
                    textArea.addCaretListener(e -> {
                        arg.setValue(textArea.getText());
                        ModuleSettingsPanel.this.setSize(400, topPanel.getPreferredSize().height + centerPanel.getPreferredSize().height);
                        ModuleSettingsPanel.this.revalidate();
                    });
                    textArea.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue(textArea.getText());
                        }
                    });

                    yield textArea;
                }

                case "char" -> {
                    BoxCharSelector charSelector = new BoxCharSelector((arg.getValue() + "").charAt(0));
                    charSelector.setPreferredSize(new Dimension(UiSettings.moduleHeight, UiSettings.moduleHeight));

                    // 监听输入
                    charSelector.addActionListener(e -> arg.setValue(charSelector.getC()));

                    yield charSelector;
                }

                case "byte" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Byte.parseByte(arg.getValue() + ""),
                            Byte.parseByte(arg.getMin() + ""),
                            Byte.parseByte(arg.getMax() + ""),
                            Byte.parseByte(arg.getStep() + ""));

                    BoxSpinner spinner = new BoxSpinner(model);
                    BoxSpinner.NumberEditor editor = new BoxSpinner.NumberEditor(spinner, "#0");
                    spinner.setEditor(editor);

                    // 监听输入
                    editor.getTextField().addActionListener(e -> arg.setValue(Byte.parseByte(editor.getTextField().getText())));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue(Byte.parseByte(editor.getTextField().getText()));
                        }
                    });

                    yield spinner;
                }

                case "short" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Short.parseShort(arg.getValue() + ""),
                            Short.parseShort(arg.getMin() + ""),
                            Short.parseShort(arg.getMax() + ""),
                            Short.parseShort(arg.getStep() + ""));

                    BoxSpinner spinner = new BoxSpinner(model);
                    BoxSpinner.NumberEditor editor = new BoxSpinner.NumberEditor(spinner, "#0");
                    spinner.setEditor(editor);

                    // 监听输入
                    editor.getTextField().addActionListener(e -> arg.setValue(Short.parseShort(editor.getTextField().getText())));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue(Short.parseShort(editor.getTextField().getText()));
                        }
                    });

                    yield spinner;
                }

                case "int" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Integer.parseInt(arg.getValue() + ""),
                            Integer.parseInt(arg.getMin() + ""),
                            Integer.parseInt(arg.getMax() + ""),
                            Integer.parseInt(arg.getStep() + ""));

                    BoxSpinner spinner = new BoxSpinner(model);
                    BoxSpinner.NumberEditor editor = new BoxSpinner.NumberEditor(spinner, "#0");
                    spinner.setEditor(editor);

                    // 监听输入
                    editor.getTextField().addActionListener(e -> arg.setValue(Integer.parseInt(editor.getTextField().getText())));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue(Integer.parseInt(editor.getTextField().getText()));
                        }
                    });

                    yield spinner;
                }

                case "long" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Long.parseLong(arg.getValue() + ""),
                            Long.parseLong(arg.getMin() + ""),
                            Long.parseLong(arg.getMax() + ""),
                            Long.parseLong(arg.getStep() + ""));

                    BoxSpinner spinner = new BoxSpinner(model);
                    BoxSpinner.NumberEditor editor = new BoxSpinner.NumberEditor(spinner, "#0");
                    spinner.setEditor(editor);

                    // 监听输入
                    editor.getTextField().addActionListener(e -> arg.setValue(Long.parseLong(editor.getTextField().getText())));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue(Long.parseLong(editor.getTextField().getText()));
                        }
                    });

                    yield spinner;
                }

                case "boolean" -> {
                    BoxCheckBox checkbox = new BoxCheckBox();
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
                    String format = "#0." + "#".repeat((String.valueOf(arg.getStep())).length() - 2);

                    BoxSpinner spinner = new BoxSpinner(model);
                    BoxSpinner.NumberEditor editor = new BoxSpinner.NumberEditor(spinner, format);
                    spinner.setEditor(editor);

                    // 监听输入
                    editor.getTextField().addActionListener(e -> arg.setValue(Float.parseFloat(editor.getTextField().getText())));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue(Float.parseFloat(editor.getTextField().getText()));
                        }
                    });

                    yield spinner;
                }

                case "double" -> {
                    SpinnerNumberModel model = new SpinnerNumberModel(
                            Double.parseDouble(arg.getValue() + ""),
                            Double.parseDouble(arg.getMin() + ""),
                            Double.parseDouble(arg.getMax() + ""),
                            Double.parseDouble(arg.getStep() + ""));

                    // 格式化显示
                    String format = "#0." + "#".repeat((String.valueOf(arg.getStep())).length() - 2);

                    BoxSpinner spinner = new BoxSpinner(model);
                    BoxSpinner.NumberEditor editor = new BoxSpinner.NumberEditor(spinner, format);
                    spinner.setEditor(editor);

                    // 监听输入
                    editor.getTextField().addActionListener(e -> arg.setValue(Double.parseDouble(editor.getTextField().getText())));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue(Double.parseDouble(editor.getTextField().getText()));
                        }
                    });

                    yield spinner;
                }

                case "enum" -> {
                    String serializableString = String.valueOf(arg.getValue());
                    BoxComboBox.BoxEnum boxEnum = new BoxComboBox.BoxEnum(serializableString);
                    BoxComboBox comboBox = new BoxComboBox(boxEnum);
                    comboBox.setSelectedIndex(boxEnum.getSelectedIndex());
                    comboBox.setBackground(getBackground());

                    // 监听输入
                    comboBox.addItemListener(e -> {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            boxEnum.setSelectedItem(e.getItem().toString());
                            arg.setValue(boxEnum.toSerializableString());
                            comboBox.setFocusable(false);
                        }
                    });

                    yield comboBox;
                }

                case "file" -> {
                    BoxFileChooser fileChooser = new BoxFileChooser(String.valueOf(arg.getValue()));

                    // 监听输入
                    fileChooser.getTextField().addActionListener(e -> arg.setValue(fileChooser.getSelectedFile()));
                    fileChooser.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue(fileChooser.getSelectedFile());
                        }
                    });

                    yield fileChooser;
                }

                case "color" -> {
                    BoxColorChooser colorChooser = new BoxColorChooser(new Color(Integer.parseInt(arg.getValue().toString())));

                    // 监听输入
                    colorChooser.getColorButton().addActionListener(e -> {
                        Color color = colorChooser.getColor();
                        Color newColor = JColorChooser.showDialog(this, "Choose Color", color);
                        if (newColor!= null) {
                            colorChooser.setColor(newColor);
                            colorChooser.getColorPanel().setBackground(newColor);
                            arg.setValue(newColor.getRGB());
                        }
                    });

                    yield colorChooser;
                }

                case "show" -> {
                    BoxShowPanel showPanel = new BoxShowPanel(arg);
                    showPanel.getTextArea().setText(arg.getValue().toString());

                    yield showPanel;
                }

                default -> {
                    log.warn("Not support type: {}", arg.getType());

                    BoxTextField textField = new BoxTextField(arg.getValue() + "");

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
            input.addKeyListener(new CloseKeyListener());

            if (input instanceof BoxSpinner boxSpinner) {
                ((BoxSpinner.DefaultEditor) boxSpinner.getEditor()).getTextField().addKeyListener(new CloseKeyListener());
            }

            // 创建面板
            BoxPanel panel = new BoxPanel();
            panel.setLayout(new BorderLayout(10, 0));
            
            // 添加到面板中
            panel.add(label, BorderLayout.WEST);
            panel.add(input, (input instanceof JTextField || input instanceof JTextArea || input instanceof JPanel)? BorderLayout.CENTER : BorderLayout.EAST);
            
            // 添加垂直间距
            centerPanel.add(panel);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 5))); // 添加5像素的垂直间距
        }

        // 添加按键绑定
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        KeyBindField keyBindField = new KeyBindField(module.getKeyBind());
        if (module.getKeyBind() == null || module.getKeyBind().isEmpty()) {
            keyBindField.setKeyBind("None");
        }
        keyBindField.addActionListener(e -> module.setKeyBind(keyBindField.getKeyBind()));
        keyBindField.setFocusable(true);

        BoxPanel panel = new BoxPanel();
        panel.setLayout(new BorderLayout(10, 0));

        JLabel label = new JLabel("按键绑定:");
        label.setForeground(Color.WHITE);
        label.setFont(UiSettings.font);

        panel.add(label, BorderLayout.WEST);
        panel.add(keyBindField, BorderLayout.CENTER);

        centerPanel.add(panel);
        // 添加到面板中
        add(centerPanel, BorderLayout.CENTER);

    }

    public void toCenter() {
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2;
        setLocation(x, y);
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

    static class CloseKeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Main.getSettings().setVisible(false);
                Main.getUi().setVisible(true);
            }
        }
    }
}
