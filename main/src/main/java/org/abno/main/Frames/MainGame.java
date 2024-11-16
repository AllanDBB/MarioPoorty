package org.abno.main.Frames;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.List;

import org.abno.board.Tile;
import org.abno.players.Token;
import org.abno.server.Client;
import org.abno.server.Server;
import org.abno.players.Dices;
import org.abno.board.Board;

public class MainGame extends JFrame {

    private static final Dimension SCREEN_SIZE = new Dimension(1366, 768);
    private static final Color BACKGROUND_COLOR = new Color(45, 21, 92);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Dimension IMAGE_SIZE = new Dimension(150, 150);
    private static Board Boardlist;
    // Add Dice panel
    private Dice dicePanel;

    public MainGame(Chat chatComponent, String selectedId, String selectedToken) {
        setTitle("Main Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SCREEN_SIZE); // Set your preferred screen size
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set background color to white
        getContentPane().setBackground(Color.WHITE);

        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false); // Make the panel transparent to show the white background
        add(contentPanel, BorderLayout.CENTER);

        // Create left panel for user info and dice
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.add(leftPanel, BorderLayout.WEST);

        // Set preferred, minimum, and maximum size for left panel
        Dimension leftPanelDimension = new Dimension(300, SCREEN_SIZE.height);
        leftPanel.setPreferredSize(leftPanelDimension);
        leftPanel.setMinimumSize(leftPanelDimension);
        leftPanel.setMaximumSize(leftPanelDimension);

        // Create and add user info panel
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(BACKGROUND_COLOR);
        userInfoPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        userInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel(selectedId + " (you)");
        userLabel.setForeground(TEXT_COLOR);
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userInfoPanel.add(userLabel);

        Token selectedTokenObj = Server.getTokenByName(selectedToken);
        ImagePanel imagePanel;
        if (selectedTokenObj != null) {
            imagePanel = new ImagePanel(selectedTokenObj.getImg(), IMAGE_SIZE);
            imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            userInfoPanel.add(imagePanel);
        } else {
            System.out.println("Token not found: " + selectedToken);
            imagePanel = new ImagePanel("", IMAGE_SIZE);
        }

        leftPanel.add(userInfoPanel);

        // Add space between panels
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create and add dice panel
        JPanel dicePanelContainer = new JPanel();
        dicePanelContainer.setLayout(new GridLayout(2, 1, 10, 10));
        dicePanelContainer.setBackground(BACKGROUND_COLOR);

        // Initialize dice panel with smaller image sizes
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

        // Add roll button
        JButton rollButton = new JButton("Roll Dice");
        rollButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollButton.setPreferredSize(new Dimension(100, 30));
        rollButton.setMaximumSize(new Dimension(100, 30));
        rollButton.addActionListener(e -> rollDice());
        dicePanelContainer.add(rollButton);

        leftPanel.add(dicePanelContainer);

        // Create and add the BoardComponent
        Boardlist= Server.getBoard();
        BoardComponent boardComponent = new BoardComponent(Boardlist);

        contentPanel.add(boardComponent, BorderLayout.CENTER);

        // Optionally, add the chat component at the bottom
        if (chatComponent != null) {
            contentPanel.add(chatComponent, BorderLayout.SOUTH);
        }

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