package org.abno.main.Frames;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame(){
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Panel y componentes
        JPanel panel = new JPanel();
        JLabel gameLabel = new JLabel("Juego en progreso...");
        panel.add(gameLabel);

        add(panel);
        setVisible(true);
    }
}
