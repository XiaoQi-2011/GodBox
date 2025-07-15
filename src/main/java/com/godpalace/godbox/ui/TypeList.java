package com.godpalace.godbox.ui;

import com.godpalace.godbox.Main;
import com.godpalace.godbox.UiSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import com.godpalace.godbox.module_mgr.Module;

public class TypeList extends JComponent implements MouseListener, MouseMotionListener {
    private static final Color DISABLED_COLOR = new Color(35, 35, 35);
    private static final Color ENTERED_COLOR = new Color(80, 80, 80);

    private final String typeName;
    private final List<Module> modules = new ArrayList<>();

    private boolean isOpen = true;

    public TypeList(String typeName) {
        super();
        this.typeName = typeName;

        addMouseListener(this);
        addMouseMotionListener(this);

        resize();
    }

    public void addModule(Module module) {
        if (module == null) return;

        modules.add(module);
        resize();
        repaint();
    }

    public void removeModule(Module module) {
        if (module == null) return;

        modules.remove(module);
        resize();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 设置字体
        g.setFont(UiSettings.font);

        // 绘制边框
        g.setColor(UiSettings.themeColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        // 绘制列表标题背景
        g.fillRect(0, 0, getWidth(), UiSettings.moduleHeight);
        g.setColor(Color.WHITE);

        // 绘制列表标题文字
        int x = getWidth() / 2 - g.getFontMetrics().stringWidth(typeName) / 2;
        int y = UiSettings.moduleHeight / 2 + g.getFontMetrics().getAscent() / 2;
        g.drawString(typeName, x, y);

        // 绘制列表标题状态图标
        g.setColor(Color.WHITE);
        if (!isOpen) {
            // 绘制向右箭头
            g.drawLine(getWidth() - 10, 5, getWidth() - 5, 10);  // \
            g.drawLine(getWidth() - 5, 10, getWidth() - 10, 15); // |
        } else {
            // 绘制向下箭头
            g.drawLine(getWidth() - 10, 10, getWidth() - 15, 5); // \
            g.drawLine(getWidth() - 10, 10, getWidth() - 5, 5);  // /
        }

        // 绘制所有模块
        if (isOpen) {
            for (int i = 0; i < modules.size(); i++) {
                Module module = modules.get(i);
                int yOffset = UiSettings.moduleHeight * (i + 1);

                // 绘制模块
                g.setColor(UiSettings.themeColor);
                g.fillRect(0, yOffset, getWidth(), UiSettings.moduleHeight);

                // 绘制模块状态
                if (module.isEnabled()){
                    g.setColor(ENTERED_COLOR);
                    g.fillRect(0, yOffset, getWidth(), UiSettings.moduleHeight);

                    g.setColor(UiSettings.themeColor);
                    g.fillRect(0, yOffset, 3, UiSettings.moduleHeight);
                } else {
                    if (module.isEntered()){
                        g.setColor(ENTERED_COLOR);
                        g.fillRect(0, yOffset, getWidth(), UiSettings.moduleHeight);
                    } else {
                        g.setColor(DISABLED_COLOR);
                        g.fillRect(0, yOffset, getWidth(), UiSettings.moduleHeight);
                    }
                }

                // 绘制模块名称
                x = getWidth() / 2 - g.getFontMetrics().stringWidth(module.getDisplayName()) / 2;
                y = yOffset + UiSettings.moduleHeight / 2 + g.getFontMetrics().getAscent() / 2;
                g.setColor(Color.WHITE);
                g.drawString(module.getDisplayName(), x, y);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int index = e.getY() / UiSettings.moduleHeight;

        if (index >= 0 && index <= modules.size()) {
            if (index == 0) {
                // 点击列表标题时，切换列表状态
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isOpen = !isOpen;
                }
            } else {
                Module module = modules.get(index - 1);

                // 点击模块时，切换模块状态
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (module.isEnabled()) {
                        module.Disable();
                    } else {
                        module.Enable();
                    }
                }

                // 打开模块配置面板
                if (e.getButton() == MouseEvent.BUTTON3) {
                    ModuleSettingsPanel panel = module.getSettingsPanel();
                    panel.toCenter();

                    // 配置面板内容
                    BackgroundFrame settings = Main.getSettings();
                    settings.getContentPane().removeAll();
                    settings.getContentPane().add(panel);

                    // 显示模块配置面板
                    settings.setVisible(true);

                    // 关闭模块面板
                    Main.getUi().setVisible(false);
                }
            }

            resize();
            repaint();
        }
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
        // 鼠标退出模块时，显示模块退出状态
        for (Module m : modules){
            m.setEntered(false);
        }

        resize();
        repaint();
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
        int index = e.getY() / UiSettings.moduleHeight;
        if (index >= 0 && index <= modules.size()) {
            for (Module m : modules){
                // 鼠标退出模块时，显示模块退出状态
                m.setEntered(false);
            }
            if (index != 0) {
                Module module = modules.get(index - 1);

                // 鼠标进入模块时，显示模块进入状态
                module.setEntered(true);
            }

            resize();
            repaint();
        }
    }

    public void resize() {
        // 计算新的窗口高度，确保每个模块都有足够的空间显示
        int height = UiSettings.moduleHeight * (modules.size() + 1);

        if (!isOpen) {
            // 收起列表时，高度仅为1个模块高度
            height = UiSettings.moduleHeight;
        }

        // 调整窗口大小，宽度保持不变，仅调整高度
        setSize(UiSettings.moduleHeight * 5, height);
    }
}
