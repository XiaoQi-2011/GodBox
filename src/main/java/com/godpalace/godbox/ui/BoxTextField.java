package com.godpalace.godbox.ui;

import com.godpalace.godbox.UiSettings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Setter
@Getter
public class BoxTextField extends JComponent implements MouseListener, KeyListener {
    private static final Color color = new Color(0, 0, 0, UiSettings.backgroundOpacity);

    private String text;

    public BoxTextField(String text) {
        super();
        this.text = text;

        setFocusable(true);
        addMouseListener(this);
        addKeyListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
