package org.abno.games;

import org.abno.players.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MemoryPath extends JFrame implements Game{
    private final int rows = 6;
    private final int cols = 3;
    private JButton[][] buttons;
    private int[] correctPath;
    private int currentAttempt;
    private int errors;
    private final int maxAttempts = 3;
    private boolean win = false;

    public MemoryPath() {
        setTitle("Memory Path Game");
        setSize(400, 400);
        setLayout(new GridLayout(rows, cols));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    private void initialize(){
        getContentPane().removeAll();
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
        revalidate();
        repaint();
    }


    private void generateRandomPath() {
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            correctPath[i] = rand.nextInt(cols);
            System.out.println(i);
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
                    win = true;
                    resetGame();
                }
            } else {

                errors++;
                if (errors >= maxAttempts) {

                    JOptionPane.showMessageDialog(null, "¡Perdiste!");
                    win = false;
                    dispose();

                } else {

                    JOptionPane.showMessageDialog(null, "¡Fallaste! Vuelve a intentarlo.");
                    resetGameWithoutResettingErrors();
                }
            }
        }
    }

    @Override
    public boolean won() {
        return win;
    }

    public void play(PlayerData player) {
        MemoryPath game = new MemoryPath();
        initialize();
        setVisible(true);
        if (win){
            player.setInteractWin(true);
        }

        game.setVisible(false);
        resetGame();
    }
}
