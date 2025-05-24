package com.godpalace.godbox.Module;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
public class ModuleUI extends JPanel {
    private final Module module;
    public boolean on = false;
    public ModuleUI(Module module) {
        this.module = module;
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
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setOn(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setOn(false);
            }
        });

        JLabel nameLabel = new JLabel(module.getName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        this.add(nameLabel, BorderLayout.WEST);
    }

    public void setOn(boolean on){
        log.debug("ModuleUI setOn: {}", on);
        this.on = on;
        if(on){
            Graphics g = this.getGraphics();
            g.setColor(Color.BLUE);
            g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
        }else{
            Graphics g = this.getGraphics();
            g.setColor(Color.WHITE);
            g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
        }
    }
}
