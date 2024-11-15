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

        // Create and add the Chat component
        Chat chatComponent = new Chat("user");

        // Create a wrapper panel for the chat with padding
        JPanel chatWrapper = new JPanel(new BorderLayout());
        chatWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0)); // Increased spacing at the bottom
        chatWrapper.setOpaque(false); // Make the wrapper panel transparent
        chatWrapper.add(chatComponent, BorderLayout.SOUTH);

        // Add the chat wrapper to the main panel at the bottom
        mainPanel.add(chatWrapper, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Make sure frame is visible
        setVisible(true);
    }

    public static void main(String[] args) {
        new PregameFrame();
    }
}