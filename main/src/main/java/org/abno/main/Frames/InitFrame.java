package org.abno.main.Frames;

import javax.swing.*;
import java.awt.*;
import org.abno.server.Server;

public class InitFrame extends JFrame {

    // Constants
    private static final String FRAME_TITLE = "MarioPoorty";
    private static final Dimension FRAME_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public InitFrame() {
        configureFrame();
        JPanel panel = createMainPanel();
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Configures the initial window (title, size, and fullscreen settings)
    private void configureFrame() {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setSize(FRAME_SIZE);
    }

    // Creates the main panel with all components (e.g., buttons)
    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton startButton = createStartButton();
        panel.add(startButton);

        return panel;
    }

    private JButton createStartButton() {
        JButton startButton = new JButton("Start");

        // Set preferred size for the button
        startButton.setPreferredSize(new Dimension(200, 100));

        startServer();
        startButton.addActionListener(e -> {
            new GameFrame();
            this.setVisible(false);
            dispose();
        });
        return startButton;
    }


    private void startServer(){

        new Thread(() -> {
            try {
                System.out.println("Starting the server...");
                Server.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
