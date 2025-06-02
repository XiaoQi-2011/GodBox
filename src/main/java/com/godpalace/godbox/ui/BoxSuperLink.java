package com.godpalace.godbox.ui;

import com.godpalace.godbox.Main;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;

@Slf4j
public class BoxSuperLink extends JLabel implements MouseListener {
    private static final Color DEFAULT_COLOR = new Color(0, 153, 255);
    private static final Color PRESSED_COLOR = new Color(0, 200, 255);

    @Getter
    @Setter
    private String url;

    private boolean isEntered = false;

    public BoxSuperLink(String text, String url) {
        super(text);
        this.url = url;

        setForeground(DEFAULT_COLOR);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isEntered) {
            int width = g.getFontMetrics().stringWidth(getText());

            g.setColor(getForeground());
            g.drawLine(0, getHeight() - 2, width, getHeight() - 2);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Main.getUi().setVisible(false);
        Main.getSettings().setVisible(false);

        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setForeground(PRESSED_COLOR);
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setForeground(DEFAULT_COLOR);
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isEntered = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isEntered = false;
        repaint();
    }
}
