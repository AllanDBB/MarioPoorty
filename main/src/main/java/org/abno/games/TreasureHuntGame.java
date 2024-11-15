package org.abno.games;

import org.abno.players.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TreasureHuntGame extends JFrame implements Game {
    private JButton[][] grid;
    private int[][] board;
    private int boardSize;
    private int remainingBombs = 7;
    private final int TREASURE = 1;
    private int foundTreasures = 0;
    private final String[] bombTypes = {"Simple", "Double", "Cross", "Line"};
    private ImageIcon explosionIcon;
    private ImageIcon treasureIcon;

    private static boolean win = false;

    public TreasureHuntGame() {
        setTitle("Treasure Hunt Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initializeIcons();
        initializeGame();
        setSize(boardSize * 50, boardSize * 50);
        setVisible(true);
    }

    private void initializeIcons() {
      
        explosionIcon = resizeIcon(new ImageIcon("src/main/java/utils/explosion.png"), 50, 50);
        treasureIcon = resizeIcon(new ImageIcon("src/main/java/utils/coin.png"), 50, 50);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    private void initializeGame() {
        Random random = new Random();
        boardSize = 10 + random.nextInt(3) * 5;
        board = new int[boardSize][boardSize];
        grid = new JButton[boardSize][boardSize];


        setLayout(new GridLayout(boardSize, boardSize));


        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JButton button = new JButton();
                button.setBackground(Color.LIGHT_GRAY);
                button.addActionListener(new CellClickListener(i, j));
                grid[i][j] = button;
                add(button);
            }
        }


        placeTreasure();
    }

    private void placeTreasure() {
        Random random = new Random();
        boolean placed = false;

        while (!placed) {
            int startRow = random.nextInt(boardSize - 1);
            int startCol = random.nextInt(boardSize - 1);
            boolean horizontal = random.nextBoolean();

            if (horizontal && startCol <= boardSize - 4) {

                for (int i = 0; i < 4; i++) {
                    board[startRow][startCol + i] = TREASURE;
                }
                placed = true;
            } else if (!horizontal && startRow <= boardSize - 4) {

                for (int i = 0; i < 4; i++) {
                    board[startRow + i][startCol] = TREASURE;
                }
                placed = true;
            }
        }
    }

    private void explode(int row, int col, String bombType) {
        switch (bombType) {
            case "Simple":
                revealCell(row, col);
                break;
            case "Double":
                for (int i = row; i <= row + 1; i++) {
                    for (int j = col; j <= col + 1; j++) {
                        revealCell(i, j);
                    }
                }
                break;
            case "Cross":
                revealCell(row, col);
                revealCell(row - 1, col);
                revealCell(row + 1, col);
                revealCell(row, col - 1);
                revealCell(row, col + 1);
                break;
            case "Line":

                if (new Random().nextBoolean()) {
                    for (int j = col - 2; j <= col + 2; j++) {
                        revealCell(row, j);
                    }
                } else {
                    for (int i = row - 2; i <= row + 2; i++) {
                        revealCell(i, col);
                    }
                }
                break;
        }


        remainingBombs--;
        if (foundTreasures == 4) {
            JOptionPane.showMessageDialog(this, "¡Has encontrado el tesoro! ¡Ganaste!");
            win = true;
            //resetGame();
        } else if (remainingBombs <= 0) {
            JOptionPane.showMessageDialog(this, "¡Te quedaste sin bombas! ¡Perdiste!");
            //resetGame();
        }
    }

    private void revealCell(int row, int col) {
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            if (board[row][col] == TREASURE && grid[row][col].getIcon() != treasureIcon) {
                foundTreasures++;
                grid[row][col].setIcon(treasureIcon);
            } else {
                grid[row][col].setIcon(explosionIcon);
            }
        }
    }

    private void resetGame() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid[i][j].setIcon(null);
                grid[i][j].setBackground(Color.LIGHT_GRAY);
                board[i][j] = 0;
            }
        }
        remainingBombs = 7;
        foundTreasures = 0;
        placeTreasure();
    }

    private class CellClickListener implements ActionListener {
        private final int row, col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (remainingBombs > 0) {

                String bombType = bombTypes[new Random().nextInt(bombTypes.length)];
                explode(row, col, bombType);
            } else {
                JOptionPane.showMessageDialog(null, "¡No tienes bombas restantes!");
            }
        }
    }

    public void play(PlayerData player) {
        TreasureHuntGame game = new TreasureHuntGame();

        if (win){
            player.changeInteract();
        }

        game.setVisible(false);
    }
}
