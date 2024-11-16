package org.abno.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;

public class MemoryGame extends JFrame implements Game{
    private final int rows = 3;
    private final int cols = 6;
    private final int buttonSize = 100;
    private JButton[][] buttons;
    private Icon[] images;
    private Icon hiddenIcon = resizeIcon(new ImageIcon("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\secret.png"), buttonSize, buttonSize);
    private boolean[][] matched;
    private JButton firstButton = null;
    private JButton secondButton = null;
    private int player1Score = 0;
    private int player2Score = 0;
    private boolean player1Turn = true;
    private String p1;
    private String p2;
    private String winner;

    public MemoryGame() {
        setTitle("Juego de Memoria");
        setSize(600, 400);
        setLayout(new GridLayout(rows, cols));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buttons = new JButton[rows][cols];
        matched = new boolean[rows][cols];
        images = loadImages();
        initializeButtons();

    }


    private static Icon resizeIcon(Icon icon, int width, int height) {
        Image img = ((ImageIcon) icon).getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }


    private Icon[] loadImages() {
        Icon[] icons = new Icon[18];
        for (int i = 0; i < 9; i++) {
            icons[i] = resizeIcon(new ImageIcon("C:\\Users\\adbyb\\OneDrive\\Documentos\\GitHub\\MarioPoorty\\main\\src\\main\\java\\utils\\memory" + (i + 1) + ".png"), buttonSize, buttonSize);
            icons[i + 9] = icons[i];
        }
        Collections.shuffle(java.util.Arrays.asList(icons));
        return icons;
    }


    private void initializeButtons() {
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setIcon(hiddenIcon);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                add(buttons[i][j]);
                matched[i][j] = false;
                buttons[i][j].setActionCommand(String.valueOf(index));
                index++;
            }
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
            if (matched[row][col]) return;

            JButton clickedButton = buttons[row][col];
            int index = Integer.parseInt(clickedButton.getActionCommand());
            clickedButton.setIcon(images[index]);

            if (firstButton == null) {
                firstButton = clickedButton;
            } else {
                secondButton = clickedButton;
                Timer timer = new Timer(500, event -> checkMatch());
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    private void checkMatch() {
        if (firstButton != null && secondButton != null) {
            int firstIndex = Integer.parseInt(firstButton.getActionCommand());
            int secondIndex = Integer.parseInt(secondButton.getActionCommand());

            if (images[firstIndex] == images[secondIndex]) {

                int firstRow = firstIndex / cols;
                int firstCol = firstIndex % cols;
                int secondRow = secondIndex / cols;
                int secondCol = secondIndex % cols;

                matched[firstRow][firstCol] = true;
                matched[secondRow][secondCol] = true;

                if (player1Turn) {
                    player1Score++;

                } else {
                    player2Score++;

                }
            } else {

                firstButton.setIcon(hiddenIcon);
                secondButton.setIcon(hiddenIcon);
                player1Turn = !player1Turn;

                setVisible(false);
                try {
                    sleep(100);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                setVisible(true);
            }



            firstButton = null;
            secondButton = null;

            checkGameOver();
        }
    }


    private void checkGameOver() {
        boolean allMatched = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!matched[i][j]) {
                    allMatched = false;
                    break;
                }
            }
            if (!allMatched) break;
        }

        if (allMatched) {
            String winnerMessage;
            if (player1Score > player2Score) {
                winnerMessage = p1 +" gana con " + player1Score + " pares!";
                winner = p1;
            } else if (player2Score > player1Score) {
                winnerMessage = p2+ " gana con " + player2Score + " pares!";
                winner = p2;
            } else {
                winnerMessage = "Â¡Empate con " + player1Score + " pares cada uno!";
                winner = "no hay ganador";
            }
            JOptionPane.showMessageDialog(this, winnerMessage);

            Timer timer = new Timer(9000, e -> dispose());  // Espera 2 segundos antes de cerrar
            timer.setRepeats(false);
            timer.start();


        }
    }


    private void resetGame() {
        player1Score = 0;
        player2Score = 0;
        player1Turn = true;
        firstButton = null;
        secondButton = null;
        matched = new boolean[rows][cols];
        images = loadImages();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j].setIcon(hiddenIcon);
                buttons[i][j].setActionCommand(String.valueOf(i * cols + j));
            }
        }
    }


    public String playMemory(String p1, String p2){
        MemoryGame memoryGame= new MemoryGame();
        memoryGame.resetGame();
        memoryGame.p1 = p1;
        memoryGame.p2 = p2;
        memoryGame.setVisible(true);
        while (memoryGame.winner == null) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        memoryGame.dispose();


        return memoryGame.winner;
    }
}