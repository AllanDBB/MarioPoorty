package org.abno.main.Frames;

import javax.swing.*;
import java.awt.*;
import org.abno.server.Server;

public class InitFrame extends JFrame {

    // Constants
    private static final String FRAME_TITLE = "MarioPoorty";
    private static final Dimension FRAME_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Color BUTTON_COLOR = Color.WHITE; // Button text color
    private static final Color BUTTON_HOVER_TEXT_COLOR = Color.LIGHT_GRAY; // Button hover text color
    private static final Font BUTTON_FONT = new Font("Monospaced", Font.BOLD, 36); // Button font
    private static final Dimension BUTTON_SIZE = new Dimension(400, 150); // Button preferred size
    private static final int BUTTON_RADIUS = 30; // Button radius

    public InitFrame() {
        configureFrame();
        JLayeredPane layeredPane = createLayeredPane();
        add(layeredPane);
        setVisible(true);
    }

    private void configureFrame() {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setSize(FRAME_SIZE);
        setLayout(new BorderLayout());
    }

    private JLayeredPane createLayeredPane() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(FRAME_SIZE);

        // Background
        JLabel background = new JLabel(new ImageIcon("main/Assets/Banner.jpg"));
        background.setBounds(0, 0, FRAME_SIZE.width, FRAME_SIZE.height);
        layeredPane.add(background, Integer.valueOf(0));

        // Main panel
        JPanel panel = createMainPanel();
        panel.setBounds(0, 0, FRAME_SIZE.width, FRAME_SIZE.height);
        layeredPane.add(panel, Integer.valueOf(1));

        return layeredPane;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false); // Make panel transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label with Rounded Background
        JLabel titleLabel = createTitleLabel();
        RoundedPanel roundedTitlePanel = new RoundedPanel(titleLabel, BUTTON_RADIUS, new Color(192, 192, 192, 128));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(200, 0, 50, 0); // Padding around the title
        panel.add(roundedTitlePanel, gbc);

        // Start Button with Rounded Background
        JButton startButton = createStartButton();
        RoundedPanel roundedButtonPanel = new RoundedPanel(startButton, BUTTON_RADIUS, new Color(192, 192, 192, 128));
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 200, 0); // Padding below button
        panel.add(roundedButtonPanel, gbc);

        return panel;
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel(FRAME_TITLE);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE); // WHITE color for the text
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    private JButton createStartButton() {
        JButton startButton = new JButton("Start");

        // Set preferred size for the button
        startButton.setPreferredSize(BUTTON_SIZE);
        startButton.setFont(BUTTON_FONT);
        startButton.setForeground(BUTTON_COLOR); // Initial text color for the button

        // Transparent background without border
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false); // Disable the focus border

        // Set an empty border to ensure no border is displayed
        startButton.setBorder(BorderFactory.createEmptyBorder());

        // Adding mouse listeners for hover effect to change text color
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                System.out.println("Mouse entered"); // Debug statement
                startButton.setForeground(BUTTON_HOVER_TEXT_COLOR); // Change text color to gray
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                System.out.println("Mouse exited"); // Debug statement
                startButton.setForeground(BUTTON_COLOR); // Revert text color to white
            }
        });

        startButton.addActionListener(e -> {
            new GameFrame();
            InitFrame.this.setVisible(false);
            dispose();
        });

        startServer();

        return startButton;
    }

    private void startServer() {
        new Thread(() -> {
            try {
                System.out.println("Starting the server...");
                Server.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;

        public RoundedPanel(Component component, int radius, Color bgColor) {
            super(new BorderLayout());
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false); // Make panel background transparent
            add(component, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(bgColor);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }
}