package org.abno.games;

import org.abno.players.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

public class CatchTheCat extends JFrame implements Game {
    private final int size = 11;
    private final JButton[][] grid = new JButton[size][size];
    private final boolean[][] blocked = new boolean[size][size];
    private int catRow = size / 2;
    private int catCol = size / 2;
    private boolean win = false;

    // Constructor sin inicialización del tablero
    public CatchTheCat() {
        setTitle("Catch the Cat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new GridLayout(size, size));
    }

    // Inicializa el tablero cada vez que se llama a play
    private void initializeBoard(PlayerData player) {
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
                            moveCat(player);
                        }
                    }
                });
                grid[i][j] = button;
                add(button);
            }
        }
    }

    private void moveCat(PlayerData player) {
        int[] dirRow = {-1, 1, 0, 0}; // Arriba, Abajo, Izquierda, Derecha
        int[] dirCol = {0, 0, -1, 1}; // Arriba, Abajo, Izquierda, Derecha

        boolean moved = false;

        for (int d = 0; d < 4; d++) {
            int newRow = catRow + dirRow[d];
            int newCol = catCol + dirCol[d];

            if (isInsideGrid(newRow, newCol) && !blocked[newRow][newCol]) {
                catRow = newRow;
                catCol = newCol;
                moved = true;
                break;
            }
        }

        if (moved) {
            updateCatPosition();
            if (isAtEdge(catRow, catCol)) {
                JOptionPane.showMessageDialog(this, "¡El gato escapó! Has perdido.");
                win = false;
                player.setInteractWin(false);
                resetGame(player);
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "¡Has ganado! El gato está atrapado.");
            win = true;
            player.setInteractWin(true);
            resetGame(player);
            dispose();
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

    private void resetGame(PlayerData player) {
        catRow = size / 2;
        catCol = size / 2;

        getContentPane().removeAll(); // Elimina todos los botones actuales del tablero
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blocked[i][j] = false;
            }
        }

        initializeBoard(player); // Inicializar el tablero
        revalidate();
        repaint();
    }

    @Override
    public boolean won() {
        return win;
    }

    // Método para iniciar el juego, crea una nueva instancia del juego y muestra la ventana
    public void play(PlayerData player) {
        // Crear una nueva instancia del juego cada vez
        CatchTheCat game = new CatchTheCat();
        game.setVisible(true); // Mostrar la ventana del juego

        game.resetGame(player); // Inicializar el tablero y comenzar el juego

        // Si el jugador ha ganado, se actualiza su estado
        if (win) {
            player.setInteractWin(true);
        }

        // Hacer invisible la ventana anterior (la ventana principal si es necesario)
        setVisible(false);
    }
}
