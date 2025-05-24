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
    public boolean on = false;
    public boolean into = false;
    public ModuleUI(Module module) {
        this.module = module;
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                    repaint();
                } catch (InterruptedException e) {
                    log.error("ModuleUI Thread Interrupted", e);
                    throw new RuntimeException(e);
                }
            }
        }).start();
        CreateUI();
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
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setInto(false);
            }
        });

        JLabel nameLabel = new JLabel(module.getName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        nameLabel.setForeground(Color.GREEN);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        this.add(nameLabel, BorderLayout.WEST);
    }

    @Override
    public void paint(Graphics g) {
        if (on) {
            g.setColor(Color.BLUE);
            g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
        } else {
            g.setColor(Color.WHITE);
            g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
        }

    }

    public void setOn(boolean on){
        log.debug("ModuleUI setOn: {}", on);
        this.on = on;
    }

    public void setInto(boolean into){
        log.debug("ModuleUI setInto: {}", into);
        this.into = into;
        if (into) {
            this.setBackground(new Color(0, 0, 0, 15));
        } else {
            this.setBackground(Color.WHITE);
        }
    }
}
