package org.abno.main.Frames;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import org.abno.players.Token;
import org.abno.server.Client;
import org.abno.server.Server;
import org.abno.players.Dices;

public class PregameFrame extends JFrame {
    private static final Dimension SCREEN_SIZE = new Dimension(1366, 768);
    private static final Color BACKGROUND_COLOR = new Color(45, 21, 92);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Dimension IMAGE_SIZE = new Dimension(300, 300);
    private boolean isReady = false;

    // Add Dice panel
    private Dice dicePanel;

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

        // Create a panel to hold the dice and roll button to the right of the main container
        gbc.gridx = 1; // Move to the next column
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel dicePanelContainer = new JPanel();
        dicePanelContainer.setLayout(new BoxLayout(dicePanelContainer, BoxLayout.Y_AXIS));
        dicePanelContainer.setBackground(BACKGROUND_COLOR);

        // Initialize dice panel
        String[] diceFaces = {
                "Dice1.png",
                "Dice2.png",
                "Dice3.png",
                "Dice4.png",
                "Dice5.png",
                "Dice6.png"
        };
        String rollGifPath = "DiceRoll.gif";
        dicePanel = new Dice(diceFaces, rollGifPath);
        dicePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dicePanelContainer.add(dicePanel);

        dicePanelContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add roll button
        JButton rollButton = new JButton("Roll Dice");
        rollButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollButton.addActionListener(e -> rollDice());
        dicePanelContainer.add(rollButton);

        // Add dice panel and roll button to the main panel
        mainPanel.add(dicePanelContainer, gbc);

        // Configure and add chat component at the bottom
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // Span both columns
        JPanel chatWrapper = new JPanel(new BorderLayout());
        chatWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        chatWrapper.setOpaque(false);
        chatWrapper.add(chatComponent, BorderLayout.SOUTH);

        mainPanel.add(chatWrapper, gbc);

        setVisible(true);
    }

    private void rollDice() {
        Client.sendValue("@Roll");
        Dices dices = new Dices();
        dices.roll();
        int finalFace1 = dices.getDice1();
        int finalFace2 = dices.getDice2();
        dicePanel.rollDice(finalFace1, finalFace2);
        Client.sendValue(Integer.toString(finalFace1));
        Client.sendValue(Integer.toString(finalFace2));
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