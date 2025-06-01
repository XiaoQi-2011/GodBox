package com.godpalace.godbox.ui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.text.JTextComponent;

@Getter
@Setter
public class BoxTextField extends JTextComponent {
    private String text;

    public BoxTextField(String text) {
        super();
        this.text = text;
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
