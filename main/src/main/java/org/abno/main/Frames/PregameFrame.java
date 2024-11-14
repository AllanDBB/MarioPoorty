package org.abno.main.Frames;

import javax.swing.*;
import java.awt.*;

public class PregameFrame extends JFrame {
    private static final Dimension SCREEN_SIZE = new Dimension(1366, 768);
    private static final Color BACKGROUND_COLOR = new Color(45, 21, 92); // Same background color as LobbyFrame
    private static final Color TEXT_COLOR = Color.WHITE; // Text color for visibility

    public PregameFrame() {
        setTitle("Pregame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SCREEN_SIZE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set up the main panel with the purple background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Add components to the main panel
        JLabel label = new JLabel("Pregame Screen");
        label.setForeground(TEXT_COLOR); // Set text color to white
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        mainPanel.add(label, BorderLayout.CENTER);
        add(mainPanel);

        // Make sure frame is visible
        setVisible(true);
    }
}