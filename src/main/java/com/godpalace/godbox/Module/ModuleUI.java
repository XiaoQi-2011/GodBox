package com.godpalace.godbox.Module;

import com.godpalace.godbox.Main;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
public class ModuleUI extends JPanel {
    private final Module module;
    private boolean on = false;
    private boolean into = false;
    private Color lineColor = Color.GRAY;

    public ModuleUI(Module module) {
        this.module = module;
        CreateUI();
        new Thread(() -> {
            while (true) {
                painting();
            }
        }).start();
    }

    public void CreateUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setSize(200, 40);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1) {
                    setOn(!on);
                }
                if (e.getButton() == 3) {
                    Main.clientUI.setGUI(module.getSettings());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setInto(true);
                Main.clientUI.setTip(module.getTooltip());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setInto(false);
                Main.clientUI.setTip(Main.tip);
            }
        });

        JLabel nameLabel = new JLabel(module.getName());
        nameLabel.setPreferredSize(new Dimension(module.getName().length() * 10, 40));
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setHorizontalAlignment(JLabel.LEFT);

        this.add(nameLabel, BorderLayout.WEST);
    }

    public void setOn(boolean on){
        log.debug("ModuleUI setOn: {}", on);
        this.on = on;
        if (on) {
            this.setBackground(Color.cyan);
            setInto(into);
            module.Enable();
        } else {
            this.setBackground(Color.WHITE);
            setInto(into);
            module.Disable();
        }
    }

    public void setInto(boolean into){
        log.debug("ModuleUI setInto: {}", into);
        this.into = into;
        if (into) {
            this.lineColor = Color.LIGHT_GRAY;
        } else {
            this.lineColor = Color.GRAY;
        }
    }

    private void painting(){
        Graphics g = this.getGraphics();
        if (g == null) return;
        g.setColor(lineColor);
        g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
    }
}
