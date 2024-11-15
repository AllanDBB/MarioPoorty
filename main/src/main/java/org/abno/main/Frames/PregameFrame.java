package org.abno.main.Frames;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import org.abno.players.Token;
import org.abno.server.Server;

public class PregameFrame extends JFrame {
    private static final Dimension SCREEN_SIZE = new Dimension(1366, 768);
    private static final Color BACKGROUND_COLOR = new Color(45, 21, 92); // Same background color as LobbyFrame
    private static final Color TEXT_COLOR = Color.WHITE; // Text color for visibility
    private static final Dimension IMAGE_SIZE = new Dimension(300, 300); // Image size
    private boolean isReady = false; // State to track if the user is ready

    public PregameFrame(Chat chatComponent, String selectedId, String selectedToken) {
        // Frame setup
        setTitle("Pregame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SCREEN_SIZE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main panel with purple background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        add(mainPanel, BorderLayout.CENTER);

        // Dimension for the container (50% of the screen height, a bit wider than IMAGE_SIZE)
        Dimension containerSize = new Dimension(IMAGE_SIZE.width + 50, SCREEN_SIZE.height / 2);

        // Container panel with white border
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(BACKGROUND_COLOR);
        containerPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        containerPanel.setPreferredSize(containerSize);
        containerPanel.setMaximumSize(containerSize);
        containerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "You" label
        JLabel userLabel = new JLabel(selectedId + " (you)");
        userLabel.setForeground(TEXT_COLOR);
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the text horizontally
        containerPanel.add(userLabel);

        // Space between label and image
        containerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Retrieve the token and set the image
        Token selectedTokenObj = Server.getTokenByName(selectedToken);
        if (selectedTokenObj != null) {
            ImagePanel imagePanel = new ImagePanel(selectedTokenObj.getImg(), IMAGE_SIZE);
            containerPanel.add(imagePanel);
        } else {
            System.out.println("Token not found: " + selectedToken);
        }

        // Space between image and button
        containerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Ready button
        JButton readyButton = createReadyButton();
        readyButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally
        containerPanel.add(readyButton);

        // Align container panel to the left with padding from the left
        JPanel leftAlignedPanel = new JPanel(new GridBagLayout());
        leftAlignedPanel.setBackground(BACKGROUND_COLOR);
        int paddingLeft = SCREEN_SIZE.width / 12; // 1/12 of the screen width
        leftAlignedPanel.setBorder(BorderFactory.createEmptyBorder(0, paddingLeft, 0, 0)); // Padding from the left
        leftAlignedPanel.add(containerPanel);
        mainPanel.add(leftAlignedPanel, BorderLayout.WEST);

        // Add chat component
        JPanel chatWrapper = new JPanel(new BorderLayout());
        chatWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0)); // Padding
        chatWrapper.setOpaque(false); // Transparent background
        chatWrapper.add(chatComponent, BorderLayout.SOUTH);
        mainPanel.add(chatWrapper, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createReadyButton() {
        JButton readyButton = new JButton("Click to begin");
        readyButton.setPreferredSize(new Dimension(200, 50)); // Slightly smaller than the InitFrame start button
        readyButton.setFont(new Font("Monospaced", Font.BOLD, 24)); // Smaller font
        readyButton.setForeground(Color.WHITE); // Initial text color
        readyButton.setOpaque(true);
        readyButton.setBackground(BACKGROUND_COLOR); // Initial background color
        readyButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // White border

        // Action listener for the ready button
        readyButton.addActionListener(e -> {
            if (!isReady) {
                isReady = true;
                readyButton.setText("Ready");
                readyButton.setBackground(BACKGROUND_COLOR.darker()); // Darker purple to indicate faded state
                readyButton.setEnabled(false); // Disable the button after clicking
            }
        });

        return readyButton;
    }

    static class ImagePanel extends JPanel {
        private Image image;
        private Dimension imageSize;

        public ImagePanel(String imagePath, Dimension imageSize) {
            this.imageSize = imageSize;
            setImage(imagePath);
            setPreferredSize(imageSize); // Set preferred size for the panel
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        public void setImage(String imagePath) {
            String fullImagePath = Paths.get("main/Assets", imagePath).toString();
            this.image = new ImageIcon(fullImagePath).getImage();
            repaint(); // Repaint the panel to update the displayed image
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int x = (getWidth() - imageSize.width) / 2; // Center the image horizontally
                int y = (getHeight() - imageSize.height) / 2; // Center the image vertically
                g.drawImage(image, x, y, imageSize.width, imageSize.height, this); // Draw the image
            }
        }
    }
}