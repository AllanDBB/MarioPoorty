package org.abno.main.Frames;

import javax.swing.*;
import java.awt.*;

public class MainGame extends JFrame {

    public MainGame() {
        setTitle("Main Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1366, 768)); // Set your preferred screen size
        setLocationRelativeTo(null);

        // Set background color to white
        getContentPane().setBackground(Color.WHITE);

        // Create and add a label that says "Board"
        JLabel boardLabel = new JLabel("Board", SwingConstants.CENTER);
        boardLabel.setFont(new Font("Monospaced", Font.BOLD, 50));
        boardLabel.setForeground(Color.BLACK);
        add(boardLabel);

        setVisible(true);
    }
}