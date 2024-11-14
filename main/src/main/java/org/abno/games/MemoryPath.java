package org.abno.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MemoryPath extends JFrame {
    private final int rows = 6;
    private final int cols = 3;
    private JButton[][] buttons;
    private int[] correctPath;
    private int currentAttempt;
    private int errors;
    private final int maxAttempts = 3;

    public MemoryPath() {
        setTitle("Memory Path Game");
        setSize(400, 400);
        setLayout(new GridLayout(rows, cols));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buttons = new JButton[rows][cols];
        correctPath = new int[rows];
        currentAttempt = 0;
        errors = 0;


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                add(buttons[i][j]);
            }
        }


        generateRandomPath();

        setVisible(true);
    }


    private void generateRandomPath() {
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            correctPath[i] = rand.nextInt(cols);
        }
    }


    private void resetGameWithoutResettingErrors() {
        currentAttempt = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }
        generateRandomPath();
    }


    private void resetGame() {
        currentAttempt = 0;
        errors = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }
        generateRandomPath();
    }


    private boolean checkPath(int row, int col) {
        if (col == correctPath[row]) {
            buttons[row][col].setBackground(Color.GREEN);
            return true;
        } else {
            buttons[row][col].setBackground(Color.RED);
            return false;
        }
    }


    private class ButtonClickListener implements ActionListener {
        private final int row;
        private final int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (checkPath(row, col)) {

                currentAttempt++;
                if (currentAttempt == rows) {

                    JOptionPane.showMessageDialog(null, "¡Ganaste!");
                    resetGame();
                }
            } else {

                errors++;
                if (errors >= maxAttempts) {

                    JOptionPane.showMessageDialog(null, "¡Perdiste!");
                    System.exit(0);
                } else {

                    JOptionPane.showMessageDialog(null, "¡Fallaste! Vuelve a intentarlo.");
                    resetGameWithoutResettingErrors();
                }
            }
        }
    }

    public static void main(String[] args) {
        new MemoryPath();
    }
}
