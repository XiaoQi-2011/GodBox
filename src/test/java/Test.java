import com.godpalace.godbox.ui.box_ui.BoxButton;

import javax.swing.*;
import java.awt.*;

public class Test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        BoxButton button = new BoxButton("a");
        button.setBounds(10, 10, 100, 50);
        button.addActionListener(e -> System.out.println("Button clicked"));
        frame.getContentPane().add(button);

        frame.setVisible(true);
    }
}
