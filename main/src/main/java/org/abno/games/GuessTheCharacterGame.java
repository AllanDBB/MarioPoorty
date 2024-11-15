package org.abno.games;

import org.abno.players.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GuessTheCharacterGame extends JFrame implements Game {

    private int turnosRestantes;
    private String textoCorrecto;
    private boolean win = false;

    public GuessTheCharacterGame() {
        setTitle("Guess Who");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initialize(PlayerData player) {
        Random random = new Random();
        turnosRestantes = random.nextInt(5) + 4;
        System.out.println("Turnos disponibles: " + turnosRestantes);


        Map<String, String> imageMap = new HashMap<>();
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character1.jpg", "yoshi");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character2.jpg", "toad");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character3.jpg", "mario");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character4.jpg", "luigi");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character5.jpg", "princesa peach");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character6.jpg", "bowser");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character7.jpg", "wario");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character8.jpg", "donkey kong");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character9.jpg", "princesa daisy");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character10.jpg", "blooper");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character11.png", "waluigi");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character12.jpg", "koopa troopa");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character13.jpg", "goomba");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character14.jpg", "shy guy");
        imageMap.put("C:\\Users\\natal\\Desktop\\sage\\MarioPoorty\\main\\src\\main\\java\\utils\\Character15.jpg", "bob omb");

        String[] imagePaths = imageMap.keySet().toArray(new String[0]);
        String selectedImagePath = imagePaths[random.nextInt(imagePaths.length)];
        textoCorrecto = imageMap.get(selectedImagePath);
        System.out.println("Texto de verificación: " + textoCorrecto);


        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());


        ImageIcon originalIcon = new ImageIcon(selectedImagePath);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        panel.add(imageLabel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(10, 10));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 100; i++) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(40, 30));
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (turnosRestantes > 0) {
                        button.setContentAreaFilled(false);
                        button.setBorderPainted(false);
                        turnosRestantes--;
                        System.out.println("Turnos restantes: " + turnosRestantes);
                        if (turnosRestantes == 0) {
                            disableAllButtons(buttonPanel);
                            JOptionPane.showMessageDialog(panel, "Se han agotado los turnos", "Información", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            });

            buttonPanel.add(button);
        }

        imageLabel.setLayout(new BorderLayout());
        imageLabel.add(buttonPanel, BorderLayout.CENTER);


        JTextField textField = new JTextField(20);
        JButton verifyButton = new JButton("Verificar");

        JPanel inputPanel = new JPanel();
        inputPanel.add(textField);
        inputPanel.add(verifyButton);
        panel.add(inputPanel, BorderLayout.SOUTH);

        verifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = textField.getText();
                if (textoCorrecto.equalsIgnoreCase(inputText)) {
                    win = true;
                    player.setInteractWin(true);
                    JOptionPane.showMessageDialog(panel, "Verificación exitosa", "Resultado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    win = false;
                    player.setInteractWin(false);
                    JOptionPane.showMessageDialog(panel, "Verificación fallida", "Resultado", JOptionPane.ERROR_MESSAGE);
                }

                GuessTheCharacterGame.this.setVisible(false);
                dispose();

            }
        });



        add(panel);
        setVisible(true);
    }

    private void disableAllButtons(JPanel buttonPanel) {
        Component[] components = buttonPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                component.setEnabled(false);
            }
        }
    }

    @Override
    public boolean won() {
        return win;
    }

    @Override
    public void play(PlayerData player) {
        GuessTheCharacterGame game = new GuessTheCharacterGame();

        game.initialize(player);



    }



}
