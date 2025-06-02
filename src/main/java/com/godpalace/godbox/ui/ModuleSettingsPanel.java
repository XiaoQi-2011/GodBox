package com.godpalace.godbox.ui;

import com.godpalace.godbox.Main;
import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.module.Module;
import com.godpalace.godbox.module.ModuleArg;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

@Slf4j
public class ModuleSettingsPanel extends BoxPanel implements MouseListener, MouseMotionListener {
    private final Module module;

    public ModuleSettingsPanel(Module module) {
        super();
        this.module = module;

        // 设置面板
        setLayout(new BorderLayout());
        setSize(300, (module.getArgs().length + 3) * (UiSettings.moduleHeight + 5) - 24);
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

        BoxPanel topPanel = new BoxPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(desc, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // 设置布局
        BoxPanel centerPanel = new BoxPanel();
        BoxLayout boxLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
        centerPanel.setLayout(boxLayout);

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
                    editor.getTextField().addActionListener(e -> arg.setValue((byte) spinner.getValue()));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue((byte) spinner.getValue());
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
                    editor.getTextField().addActionListener(e -> arg.setValue((short) spinner.getValue()));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue((short) spinner.getValue());
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
                    editor.getTextField().addActionListener(e -> arg.setValue((int) spinner.getValue()));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue((int) spinner.getValue());
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
                    editor.getTextField().addActionListener(e -> arg.setValue((long) spinner.getValue()));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue((long) spinner.getValue());
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
                    String format = "#0." + "#".repeat(((String) arg.getStep()).length() - 2);

                    BoxSpinner spinner = new BoxSpinner(model);
                    BoxSpinner.NumberEditor editor = new BoxSpinner.NumberEditor(spinner, format);
                    spinner.setEditor(editor);

                    // 监听输入
                    editor.getTextField().addActionListener(e -> arg.setValue((float) spinner.getValue()));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue((float) spinner.getValue());
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
                    String format = "#0." + "#".repeat(((String) arg.getStep()).length() - 2);

                    BoxSpinner spinner = new BoxSpinner(model);
                    BoxSpinner.NumberEditor editor = new BoxSpinner.NumberEditor(spinner, format);
                    spinner.setEditor(editor);

                    // 监听输入
                    editor.getTextField().addActionListener(e -> arg.setValue((double) spinner.getValue()));
                    editor.getTextField().addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            arg.setValue((double) spinner.getValue());
                        }
                    });

                    yield spinner;
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
            panel.add(input, (input instanceof JTextField ? BorderLayout.CENTER : BorderLayout.EAST));
            
            // 添加垂直间距
            centerPanel.add(panel);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 5))); // 添加5像素的垂直间距
        }

        add(centerPanel, BorderLayout.CENTER);

        // 添加作者和版本信息
        BoxSuperLink info = new BoxSuperLink("作者: " + module.getAuthor() + "  版本: " + module.getVersion(), module.getWebsite());
        info.setFont(UiSettings.font.deriveFont(12.0f));
        add(info, BorderLayout.SOUTH);
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
