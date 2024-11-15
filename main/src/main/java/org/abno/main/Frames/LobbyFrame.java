package org.abno.main.Frames;

import org.abno.players.Token;
import org.abno.server.Client;
import org.abno.server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Set;

public class LobbyFrame extends JFrame {
    // Constants
    private static final String FRAME_TITLE = "Lobby";
    private static final Dimension SCREEN_SIZE = new Dimension(1366, 768); // Manually setting the fixed dimension 1366x768
    private static final Color BACKGROUND_COLOR = new Color(45, 21, 92); // Lobby background color
    private static final Color BORDER_COLOR = Color.WHITE; // Border color
    private static final Color INPUT_BOX_BG_COLOR = new Color(192, 192, 192, 128); // Input box background color

    private static final String ASSETS_BASE_PATH = "main/Assets"; // Base path for assets

    private static final int GRID_ROWS = 2;
    private static final int GRID_COLUMNS = 5;
    private static final int CELL_SIZE = 133; // 2/3 larger than 100
    private static final Dimension CONTAINER_SIZE = new Dimension(GRID_COLUMNS * CELL_SIZE, GRID_ROWS * CELL_SIZE);
    private String selectedToken;
    private String selectedId;
    private ImagePanel imagePanel;



    public LobbyFrame() {
        configureFrame();

        // Create a main panel with absolute positioning
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Add the side panel (with existing components)
        JPanel sidePanel = createSidePanel();
        sidePanel.setBounds(100, 100, 400, 768);

        // Create and position the new container
        JPanel newContainer = createNewContainer();
        Rectangle newContainerBounds = calculateNewContainerBounds();
        newContainer.setBounds(newContainerBounds);

        // Add sidePanel and newContainer appropriately
        mainPanel.add(sidePanel);
        mainPanel.add(newContainer);

        add(mainPanel);
        setVisible(true);

    }

    private void configureFrame() {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SCREEN_SIZE); // Use the fixed size of 1366x768
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR); // Set background color of the frame
    }

    private JPanel createSidePanel() {
        JPanel panel = getJPanel();

        // Create a horizontal container to hold both panels side by side
        JPanel horizontalContainer = new JPanel();
        horizontalContainer.setLayout(new BoxLayout(horizontalContainer, BoxLayout.X_AXIS));
        horizontalContainer.setBackground(BACKGROUND_COLOR);

        // Left side with existing components
        JPanel leftSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.setBackground(BACKGROUND_COLOR);

        // Left Container with White Rounded Border
        JPanel leftContainer = createRoundedBorderPanel();
        leftSide.add(leftContainer);

        // Add some spacing between the container and the text input box
        leftSide.add(Box.createRigidArea(new Dimension(0, 27))); // 2/3 of 20 spacing

        // Text Input Box
        JPanel inputContainer = createInputContainer();
        leftSide.add(inputContainer);

        // Create the new container
        JPanel newContainer = createNewContainer();

        // Add both containers to the horizontal container
        horizontalContainer.add(leftSide);
        horizontalContainer.add(Box.createRigidArea(new Dimension(134, 0))); // Increased padding to 134
        horizontalContainer.add(newContainer);

        panel.add(horizontalContainer);
        return panel;
    }

    private JPanel createNewContainer() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 4, true)); // 2/3 larger than 3
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(GRID_ROWS, GRID_COLUMNS, 13, 13)); // 2/3 larger than 10 gaps

        // Fetch tokens from server
        Set<Token> tokensSet = Server.getAvailableTokens();
        Token[] tokensArray = tokensSet.toArray(new Token[0]);
        int totalCells = GRID_COLUMNS * GRID_ROWS;

        // Ensure the number of tokens is enough to fill the grid
        if (tokensArray.length < totalCells) {
            System.out.println("Not enough tokens available to fill the grid");
            return panel;
        }

        // Create grid cells with images
        for (int i = 0; i < totalCells; i++) {
            JPanel cell = createImageCell(tokensArray[i]);
            panel.add(cell);
        }

        panel.setPreferredSize(CONTAINER_SIZE);
        panel.setMaximumSize(CONTAINER_SIZE);
        panel.setMinimumSize(CONTAINER_SIZE);

        return panel;
    }

    private JPanel createImageCell(Token token) {
        JPanel cell = new JPanel();
        cell.setLayout(new BorderLayout());
        cell.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2, true)); // 2/3 larger than 1

        // Load and set the image
        String imagePath = Paths.get(ASSETS_BASE_PATH, token.getImg()).toString();
        ImageIcon icon = new ImageIcon(imagePath);
        JLabel iconLabel = new JLabel();
        Image scaledImage = icon.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        iconLabel.setIcon(new ImageIcon(scaledImage));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setVerticalAlignment(JLabel.CENTER);
        cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));

        // Add the icon label to the cell
        cell.add(iconLabel, BorderLayout.CENTER);

        // Add mouse listener to print token name and update the image panel on click
        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedToken = token.getName(); // Save the clicked token to the selectedToken variable
                imagePanel.setImage(token.getImg()); // Update the image panel
            }
        });


        return cell;
    }

    private Rectangle calculateNewContainerBounds() {
        int rightX = SCREEN_SIZE.width - 200 - CONTAINER_SIZE.width; // Position relative to the right side
        int topY = SCREEN_SIZE.height / 4;
        return new Rectangle(rightX, topY, CONTAINER_SIZE.width, CONTAINER_SIZE.height);
    }

    private static JPanel getJPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Calculate the left padding to position the container and input box at 1/12 of the screen width
        int leftPadding = SCREEN_SIZE.width / 30; // 2/3 of 40 padding

        // Calculate the top padding to position the container 1/6 from the top of the screen
        int topPadding = SCREEN_SIZE.height / 9; // 2/3 of height / 12 padding

        // Set the border with the calculated padding
        panel.setBorder(BorderFactory.createEmptyBorder(topPadding, leftPadding, 67, 67)); // 2/3 padding
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    private JPanel createRoundedBorderPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 4, true)); // 2/3 of 3 border size

        Dimension containerSize = new Dimension(267, 267); // 2/3 of 200 x 200

        // Setting the dimensions to control the container height
        panel.setPreferredSize(containerSize);
        panel.setMaximumSize(containerSize);
        panel.setMinimumSize(containerSize);

        // Layout to fill the entire panel with the image
        panel.setLayout(new BorderLayout());

        panel.setOpaque(false);

        // Create and add the image panel to the container panel
        this.imagePanel = createImagePanel(containerSize); // Store reference to imagePanel
        panel.add(imagePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createInputContainer() {
        // Text Input
        JTextField textField = new JTextField(27);
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2, true));
        inputPanel.setBackground(INPUT_BOX_BG_COLOR);
        inputPanel.setMaximumSize(new Dimension(267, textField.getPreferredSize().height));

        // Start Button
        JButton startButton = getJButton(textField);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        container.add(inputPanel);
        container.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        container.add(startButton);

        return container;
    }

    private JButton getJButton(JTextField textField) {
        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(JComponent.CENTER_ALIGNMENT); // Center button
        startButton.addActionListener(e -> {
            if (selectedToken != null) {
                startButton.setBackground(Color.GREEN);
                startButton.setEnabled(false);
                selectedId = textField.getText();

                Client.sendValue(selectedId);
                Client.sendValue(selectedToken);

                // Switching to PregameFrame
                SwingUtilities.invokeLater(() -> {
                    Client.setChatLog(new Chat(selectedId));
                    new PregameFrame(Client.getChat(),selectedId,selectedToken);
                    LobbyFrame.this.dispose(); // Dispose the current frame
                });
            } else {
                System.out.println("No token selected.");
            }
        });
        return startButton;
    }

    private ImagePanel createImagePanel(Dimension size) {
        return new ImagePanel("Banner.jpg", size);
    }

}

class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(String imagePath, Dimension size) {
        setImage(imagePath);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
    }

    public void setImage(String imagePath) {
        String fullImagePath = Paths.get("main/Assets", imagePath).toString();
        this.image = new ImageIcon(fullImagePath).getImage();
        repaint(); // Repaint the panel to update the displayed image
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Scale the image to fit the panel
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

}