import com.godpalace.godbox.ui.BoxCharSelector;

import javax.swing.*;
import java.awt.*;

public class Test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        frame.getContentPane().add(new BoxCharSelector('a'), BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
