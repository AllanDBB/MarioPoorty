package org.abno.main.Frames;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import org.abno.players.Token;
import org.abno.server.Client;
import org.abno.server.Server;

public class PregameFrame extends JFrame {
    private static final Dimension SCREEN_SIZE = new Dimension(1366, 768);
    private static final Color BACKGROUND_COLOR = new Color(45, 21, 92);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Dimension IMAGE_SIZE = new Dimension(300, 300);
    private boolean isReady = false;

    public PregameFrame(Chat chatComponent, String selectedId, String selectedToken) {
        setTitle("Pregame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SCREEN_SIZE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(BACKGROUND_COLOR);
        containerPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        containerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel(selectedId + " (you)");
        userLabel.setForeground(TEXT_COLOR);
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        containerPanel.add(userLabel);

        containerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        Token selectedTokenObj = Server.getTokenByName(selectedToken);
        if (selectedTokenObj != null) {
            ImagePanel imagePanel = new ImagePanel(selectedTokenObj.getImg(), IMAGE_SIZE);
            imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            containerPanel.add(imagePanel);
        } else {
            System.out.println("Token not found: " + selectedToken);
        }

        containerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton readyButton = createReadyButton();
        readyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        containerPanel.add(readyButton);

        // Add container panel to main panel
        mainPanel.add(containerPanel, gbc);

        // Configure and add chat component at the bottom
        gbc.gridy++;
        JPanel chatWrapper = new JPanel(new BorderLayout());
        chatWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        chatWrapper.setOpaque(false);
        chatWrapper.add(chatComponent, BorderLayout.SOUTH);

        mainPanel.add(chatWrapper, gbc);

        setVisible(true);
    }

    private JButton createReadyButton() {
        JButton readyButton = new JButton("Click to begin");
        readyButton.setPreferredSize(new Dimension(200, 50));
        readyButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        readyButton.setForeground(Color.WHITE);
        readyButton.setOpaque(true);
        readyButton.setBackground(BACKGROUND_COLOR);
        readyButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        readyButton.addActionListener(e -> {
            if (!isReady) {
                isReady = true;
                readyButton.setText("Ready");
                readyButton.setBackground(BACKGROUND_COLOR.darker());
                readyButton.setEnabled(false);
                Client.sendValue("@Ready");
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
            setPreferredSize(imageSize);
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        public void setImage(String imagePath) {
            String fullImagePath = Paths.get("main/Assets", imagePath).toString();
            this.image = new ImageIcon(fullImagePath).getImage();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int x = (getWidth() - imageSize.width) / 2;
                int y = (getHeight() - imageSize.height) / 2;
                g.drawImage(image, x, y, imageSize.width, imageSize.height, this);
            }
        }
    }
}