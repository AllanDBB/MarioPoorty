package org.abno.main.Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LobbyFrame extends JFrame {
    // Constants
    private static final String FRAME_TITLE = "Lobby";
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Color BACKGROUND_COLOR = new Color(45, 21, 92); // Lobby background color
    private static final Color BORDER_COLOR = Color.WHITE; // Border color
    private static final Color INPUT_BOX_BG_COLOR = new Color(192, 192, 192, 128); // Input box background color

    public LobbyFrame() {
        configureFrame();
        // Create a main panel with absolute positioning
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Add the side panel (with existing components)
        JPanel sidePanel = createSidePanel();
        sidePanel.setBounds(0, 0, SCREEN_SIZE.width / 3, SCREEN_SIZE.height);

        // Create and position the new container
        JPanel newContainer = createNewContainer();
        int rightX = SCREEN_SIZE.width / 2;
        int topY = SCREEN_SIZE.height / 6;
        newContainer.setBounds(rightX, topY, 2 * SCREEN_SIZE.width / 5, SCREEN_SIZE.height / 3);

        mainPanel.add(sidePanel);
        mainPanel.add(newContainer);

        add(mainPanel);
        setVisible(true);
    }

    private void configureFrame() {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(SCREEN_SIZE);
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
        leftSide.add(Box.createRigidArea(new Dimension(0, 20)));

        // Text Input Box
        JPanel inputContainer = createInputContainer();
        leftSide.add(inputContainer);

        // Create the new container
        JPanel newContainer = createNewContainer();

        // Add both containers to the horizontal container
        horizontalContainer.add(leftSide);
        horizontalContainer.add(Box.createRigidArea(new Dimension(50, 0))); // Space between containers
        horizontalContainer.add(newContainer);

        panel.add(horizontalContainer);
        return panel;
    }

    private JPanel createNewContainer() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 3, true));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(2, 5, 10, 10)); // 2 rows, 5 columns, with 10px gaps

        // Define an array of 10 different colors
        Color[] colors = {
                new Color(255, 87, 87),   // Red
                new Color(87, 87, 255),   // Blue
                new Color(87, 255, 87),   // Green
                new Color(255, 255, 87),  // Yellow
                new Color(255, 87, 255),  // Magenta
                new Color(87, 255, 255),  // Cyan
                new Color(255, 165, 87),  // Orange
                new Color(165, 87, 255),  // Purple
                new Color(87, 255, 165),  // Mint
                new Color(255, 87, 165)   // Pink
        };

        // Create grid cells
        int totalCells = 10; // 5 columns * 2 rows
        for (int i = 0; i < totalCells; i++) {
            JPanel cell = new JPanel();
            cell.setBackground(colors[i % colors.length]);

            // Calculate dimensions based on container size
            int cellWidth = (2 * SCREEN_SIZE.width / 5 - 60) / 5;  // Subtracting border and gaps
            int cellHeight = (SCREEN_SIZE.height / 3 - 40) / 2;  // Subtracting border and gaps

            cell.setPreferredSize(new Dimension(cellWidth, cellHeight));
            cell.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));

            // Add mouse listener to print color on click
            cell.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Clicked color: " + cell.getBackground().toString());
                }
            });

            // Add the cell to the container
            panel.add(cell);
        }

        Dimension containerSize = new Dimension(2 * SCREEN_SIZE.width / 5, SCREEN_SIZE.height / 3);
        panel.setPreferredSize(containerSize);
        panel.setMaximumSize(containerSize);
        panel.setMinimumSize(containerSize);

        return panel;
    }

    private static JPanel getJPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Calculate the left padding to position the container and input box at 1/6 of the screen width
        int leftPadding = SCREEN_SIZE.width / 6;

        // Calculate the top padding to position the container 1/12 from the top of the screen
        int topPadding = SCREEN_SIZE.height / 12;

        // Set the border with the calculated padding
        panel.setBorder(BorderFactory.createEmptyBorder(topPadding, leftPadding, 50, 50)); // Padding around the panel
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    private JPanel createRoundedBorderPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 3, true)); // White rounded border

        // Calculating the required container height
        int containerHeight = SCREEN_SIZE.height / 2;
        Dimension containerSize = new Dimension(200, containerHeight);

        // Setting the dimensions to control the container height
        panel.setPreferredSize(containerSize);
        panel.setMaximumSize(containerSize);
        panel.setMinimumSize(containerSize);

        // Layout to fill the entire panel with the image
        panel.setLayout(new BorderLayout());

        panel.setOpaque(false);

        // Create and add the image panel to the container panel
        JPanel imagePanel = createImagePanel(containerSize);
        panel.add(imagePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createImagePanel(Dimension size) {
        return new ImagePanel("main/Assets/Banner.jpg", size);
    }

    private JPanel createInputContainer() {
        JTextField textField = new JTextField(20);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textField, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true)); // White rounded border
        panel.setBackground(INPUT_BOX_BG_COLOR); // Transparent background
        panel.setMaximumSize(new Dimension(200, textField.getPreferredSize().height)); // Ensure the height matches the preferred height of the text field
        return panel;
    }

    public static void main(String[] args) {
        new LobbyFrame();
    }
}

class ImagePanel extends JPanel {
    private final Image image;

    public ImagePanel(String imagePath, Dimension size) {
        this.image = new ImageIcon(imagePath).getImage();
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Scale the image to fit the panel
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}