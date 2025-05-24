package com.godpalace.godbox;

import com.godpalace.godbox.Utils.SettingGroup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class ClientUI extends JFrame {

    private JSplitPane splitPane;
    public ModulePanel modulePanel;
    public SetPanel setPanel;
    public JLabel tipLabel;

    public ClientUI() {
        modulePanel = new ModulePanel();
        setPanel = new SetPanel();
    }

    public void init() {
        log.info("init");
        this.setTitle("God Box 1.0");
        this.setBounds(500, 300, 450, 350);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Bar
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("文件");
        JMenuItem loadConfigItem = new JMenuItem("加载配置文件");
        JMenuItem saveConfigItem = new JMenuItem("保存配置文件");
        JMenuItem exitItem = new JMenuItem("退出");

        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        //SplitPane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(setPanel), new JScrollPane(modulePanel));
        splitPane.setDividerLocation(150);
        splitPane.setDividerSize(7);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);

        //TipLabel
        tipLabel = new JLabel("提示：");
        tipLabel.setSize(100, 10);

        //Add to frame
        this.add(tipLabel, BorderLayout.SOUTH);
        this.add(splitPane, BorderLayout.CENTER);
        this.setJMenuBar(menuBar);
        this.setVisible(true);
    }

    public void setTip(String tip) {
        tipLabel.setText(tip);
    }

    public void setGUI(SettingGroup settingGroup){
        setPanel.setGUI(settingGroup);
    }
}
