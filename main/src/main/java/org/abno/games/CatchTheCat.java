package org.abno.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

public class CatchTheCat extends JFrame {
    private final int size = 11;
    private final JButton[][] grid = new JButton[size][size];
    private final boolean[][] blocked = new boolean[size][size];
    private int catRow = size / 2;
    private int catCol = size / 2;

    public CatchTheCat() {
        setTitle("Catch the Cat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new GridLayout(size, size));

        initializeBoard();
        setVisible(true);
    }


    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton button = new JButton();
                button.setBackground(Color.LIGHT_GRAY);


                if (i == catRow && j == catCol) {
                    button.setBackground(Color.ORANGE);
                }

                final int row = i;
                final int col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!blocked[row][col] && (row != catRow || col != catCol)) {
                            button.setBackground(Color.DARK_GRAY);
                            blocked[row][col] = true;
                            moveCat();
                        }
                    }
                });
                grid[i][j] = button;
                add(button);
            }
        }
    }

    private void moveCat() {
        int[] dirRow = {-1, 1, 0, 0, -1, 1};
        int[] dirCol = {0, 0, -1, 1, -1, 1};


        if (isAtEdge(catRow, catCol)) {
            JOptionPane.showMessageDialog(this, "¡El gato escapó! Has perdido.");
            resetGame();
            return;
        }

        boolean[][] visited = new boolean[size][size];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{catRow, catCol});
        visited[catRow][catCol] = true;

        boolean foundPathToEdge = false;


        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int row = pos[0];
            int col = pos[1];

            // Verificar si el gato llega a un borde
            if (isAtEdge(row, col)) {
                foundPathToEdge = true;
                break;
            }

            // Explorar movimientos posibles
            for (int d = 0; d < 6; d++) {
                int newRow = row + dirRow[d];
                int newCol = col + dirCol[d];

                if (isInsideGrid(newRow, newCol) && !blocked[newRow][newCol] && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }

        if (foundPathToEdge) {

            for (int d = 0; d < 6; d++) {
                int newRow = catRow + dirRow[d];
                int newCol = catCol + dirCol[d];

                if (isInsideGrid(newRow, newCol) && !blocked[newRow][newCol]) {
                    catRow = newRow;
                    catCol = newCol;
                    updateCatPosition();


                    if (isAtEdge(catRow, catCol)) {
                        JOptionPane.showMessageDialog(this, "¡El gato escapó! Has perdido.");
                        resetGame();
                    }
                    return;
                }
            }
        } else {

            JOptionPane.showMessageDialog(this, "¡Felicidades! Has atrapado al gato.");
            resetGame();
        }
    }


    private boolean isAtEdge(int row, int col) {
        return row == 0 || row == size - 1 || col == 0 || col == size - 1;
    }




    private boolean isInsideGrid(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }


    private void updateCatPosition() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == catRow && j == catCol) {
                    grid[i][j].setBackground(Color.ORANGE);
                } else if (!blocked[i][j]) {
                    grid[i][j].setBackground(Color.LIGHT_GRAY);
                }
            }
        }
    }


    private void resetGame() {
        catRow = size / 2;
        catCol = size / 2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blocked[i][j] = false;
                grid[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }
        grid[catRow][catCol].setBackground(Color.ORANGE);
    }

    public static void main(String[] args) {
        new CatchTheCat();
    }
}
