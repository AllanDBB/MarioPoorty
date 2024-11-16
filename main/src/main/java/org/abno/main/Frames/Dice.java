package org.abno.main.Frames;

import org.abno.players.Dices;
import org.abno.server.Client;
import org.abno.server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

public class Dice extends JPanel {
    private static final int FACE_COUNT = 6;
    private static final int ROLL_DURATION = 1000; // 1000ms or 1 second
    private final String[] faceImages; // Paths to the dice face images
    private final String rollGifPath; // Path to the roll GIF
    private ImageIcon[] faces; // Loaded dice face images
    private ImageIcon rollGif; // Loaded roll GIF
    private ImageIcon[] currentImages; // Currently displayed images for both dice (two images)

    public Dice(String[] faceImages, String rollGifPath) {
        this.faceImages = faceImages;
        this.rollGifPath = rollGifPath;
        loadImages();
        loadGif();
        currentImages = new ImageIcon[]{faces[0], faces[0]}; // Initial images for dice
        setPreferredSize(new Dimension(200, 100)); // Adjust size for two dice
    }

    private void loadImages() {
        faces = new ImageIcon[FACE_COUNT];
        for (int i = 0; i < FACE_COUNT; i++) {
            String fullPath = Paths.get("main/Assets", faceImages[i]).toString();
            faces[i] = new ImageIcon(fullPath);
        }
    }

    private void loadGif() {
        String fullPath = Paths.get("main/Assets", rollGifPath).toString();
        rollGif = new ImageIcon(fullPath);
    }

    public void rollDice(int finalFaceIndex1, int finalFaceIndex2) {
        currentImages[0] = rollGif;
        currentImages[1] = rollGif;
        repaint();

        ActionListener stopGif = new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                currentImages[0] = faces[finalFaceIndex1-1];
                currentImages[1] = faces[finalFaceIndex2-1];
                repaint();
                System.out.println("Dice 1 Final face: " + (finalFaceIndex1));
                System.out.println("Dice 2 Final face: " + (finalFaceIndex2));

            }
        };

        Timer rollTimer = new Timer(ROLL_DURATION, stopGif);
        rollTimer.setRepeats(false); // Execute only once after the duration
        rollTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int diceWidth = getWidth() / 2;
        if (currentImages[0] != null && currentImages[1] != null) {
            g.drawImage(currentImages[0].getImage(),
                    0, 0, diceWidth, getHeight(), this);
            g.drawImage(currentImages[1].getImage(),
                    diceWidth, 0, diceWidth, getHeight(), this);
        } else {
            g.drawString("Loading...", getWidth() / 4, getHeight() / 2);
            g.drawString("Loading...", 3 * getWidth() / 4, getHeight() / 2);
        }
    }

    public static void main(String[] args) {
        // Example usage
        JFrame frame = new JFrame("Dice Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Paths to dice face images located in main/Assets
        String[] diceFaces = {
                "Dice1.png",
                "Dice2.png",
                "Dice3.png",
                "Dice4.png",
                "Dice5.png",
                "Dice6.png"
        };

        // Path to the roll GIF located in main/Assets
        String rollGifPath = "DiceRoll.gif";

        Dice dice = new Dice(diceFaces, rollGifPath);

        JButton rollButton = new JButton("Roll Dice");
        rollButton.addActionListener(e -> {
            // Example: manually setting the final face indices for both dice

            if (Server.getGameStarted()){Client.sendValue("@Roll");}
            System.out.println(Server.getGameStarted());
            Dices dices = new Dices();
            dices.roll();
            int finalFace1= dices.getDice1();
            int finalFace2 = dices.getDice2();
            dice.rollDice(finalFace1, finalFace2);
            Client.sendValue(Integer.toString(finalFace1));
            Client.sendValue(Integer.toString(finalFace2));
        });

        frame.setLayout(new BorderLayout());
        frame.add(dice, BorderLayout.CENTER);
        frame.add(rollButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}