package org.abno.main.Frames;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import org.abno.board.Tile;
import org.abno.board.Board;

public class BoardComponent extends JPanel {

    private static final int PANEL_WIDTH = 1066;
    private static final int PANEL_HEIGHT = 618;
    private static final int ROWS = 5;
    private static final int COLUMNS = 8;
    private static final int TILE_WIDTH = PANEL_WIDTH / COLUMNS;
    private static final int TILE_HEIGHT = PANEL_HEIGHT / ROWS;

    public BoardComponent(Board board) {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new GridLayout(ROWS, COLUMNS)); // 5 rows, 8 columns
        initializeBoard(board.getTiles());
    }

    private void initializeBoard(List<Tile> tiles) {
        removeAll(); // Clear previous cells if any

        if (tiles.size() != ROWS * COLUMNS) {
            System.err.println("Mismatch between number of tiles and grid dimensions");
        }

        for (int i = 0; i < ROWS * COLUMNS; i++) {
            if (i < tiles.size()) {
                Tile tile = tiles.get(i);
                // Load and resize the image
                ImageIcon tileIcon = resizeImageIcon(tile.getImg(), TILE_WIDTH, TILE_HEIGHT);
                System.out.println(tile.getId());

                // Create a label for each tile with the resized image icon
                JLabel tileLabel = new JLabel(tileIcon);
                tileLabel.setHorizontalAlignment(JLabel.CENTER);
                tileLabel.setVerticalAlignment(JLabel.CENTER);

                // Set a border for each tile if needed
                tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                // Add the label to the panel
                add(tileLabel);
            } else {
                // Add an empty label if there are not enough tiles
                JLabel emptyLabel = new JLabel();
                add(emptyLabel);
            }
        }

        // Refresh the panel to display the new components
        revalidate();
        repaint();
    }

    private ImageIcon resizeImageIcon(String imgPath, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imgPath));
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageIcon(); // Return an empty icon in case of failure
        }
    }
}
