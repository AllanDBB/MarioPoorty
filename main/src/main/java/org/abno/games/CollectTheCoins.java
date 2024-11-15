package org.abno.games;

import org.abno.players.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class CollectTheCoins extends JFrame implements Game{
    private static final int GRID_SIZE = 25;
    private static final int TOTAL_CELLS = GRID_SIZE * GRID_SIZE;
    private JButton[][] gridButtons;
    private int[][] coinValues;
    private int score;
    private Timer timer;
    private int timeLeft;
    private Image goodCoinImage;
    private Image badCoinImage;
    private Image unknownCoinImage;
    private JLabel scoreLabel;
    private boolean win;

    public CollectTheCoins() {
        super("Collect the Coins");
        score = 0;
        setLayout(new BorderLayout());
        gridButtons = new JButton[GRID_SIZE][GRID_SIZE];
        coinValues = new int[GRID_SIZE][GRID_SIZE];

    }

    private void initialize(PlayerData player){
        try {

            goodCoinImage = ImageIO.read(new File("main/src/main/java/utils/coin.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            badCoinImage = ImageIO.read(new File("main/src/main/java/utils/redCoin.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            unknownCoinImage = ImageIO.read(new File("main/src/main/java/utils/unknown.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println("Error al cargar las imágenes: " + e.getMessage());
        }

        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        initializeGrid(gridPanel);


        int[] timeOptions = {30, 45, 60};
        timeLeft = timeOptions[new Random().nextInt(timeOptions.length)];

        JPanel infoPanel = new JPanel();
        JLabel timerLabel = new JLabel("Time left: " + timeLeft + "s");
        scoreLabel = new JLabel("Score: 0");
        infoPanel.add(timerLabel);
        infoPanel.add(scoreLabel);

        add(gridPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft + "s");
            if (timeLeft <= 0) {
                timer.stop();
                checkWin(scoreLabel, player);
            }
        });
        timer.start();

        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    private void initializeGrid(JPanel gridPanel) {
        Random rand = new Random();
        int halfCells = TOTAL_CELLS / 2;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(20, 20));
                gridButtons[i][j] = button;

                // Asignar valores a las monedas (positivos y negativos)
                int coinValue = rand.nextInt(10) + 1; // Valor entre 1 y 10
                coinValue *= (halfCells-- > 0) ? 1 : -1; // Mitad positivas, mitad negativas
                coinValues[i][j] = coinValue;

                // Inicializar con la imagen desconocida
                button.setIcon(new ImageIcon(unknownCoinImage));

                int finalI = i;
                int finalJ = j;

                // Acción al hacer clic en un botón
                button.addActionListener(e -> {
                    score += coinValues[finalI][finalJ]; // Actualizar el puntaje
                    button.setEnabled(false); // Desactivar el botón después de hacer clic

                    // Cambiar la imagen según el valor
                    if (coinValues[finalI][finalJ] > 0) {
                        button.setIcon(new ImageIcon(goodCoinImage));
                    } else {
                        button.setIcon(new ImageIcon(badCoinImage));
                    }

                    // Actualizar el puntaje en tiempo real
                    scoreLabel.setText("Score: " + score);
                });

                gridPanel.add(button);
            }
        }
    }


    private void checkWin(JLabel scoreLabel, PlayerData player) {
        scoreLabel.setText("Final Score: " + score);
        String message = score > 0
                ? "Congratulations, you won with a score of " + score + "!"
                : "Sorry, you lost with a score of " + score + ".";
        JOptionPane.showMessageDialog(this, message);

        win = score > 0; // Determinar si se gana según el puntaje final
        if (win) {
            player.setInteractWin(true);
        } else {
            player.setInteractWin(false);
        }
        dispose();
    }

    @Override
    public boolean won() {
        return win;
    }

    @Override
    public void play(PlayerData player) {
        CollectTheCoins game = new CollectTheCoins();
        game.initialize(player);
        game.setVisible(true);

    }

}
