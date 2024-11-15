package org.abno.games;

import org.abno.players.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LetterSoup implements Game {

    private static final int SIZE = 10;
    private char[][] board = new char[SIZE][SIZE];
    private boolean[][] occupied = new boolean[SIZE][SIZE];
    private List<String> words = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private List<String> foundWords = new ArrayList<>();
    private JFrame frame;
    private JPanel panel;
    private JTextField wordInputField;
    private boolean win = false;
    private Timer timer;
    private int TIME_LIMIT = 120;

    public LetterSoup() {

    }

    private void initialize() {
        words.clear();
        selectedWords.clear();
        foundWords.clear();
        board = new char[SIZE][SIZE];
        occupied = new boolean[SIZE][SIZE];
        TIME_LIMIT = 120;

        loadWordsFromFile("main/src/main/java/utils/words");
        selectRandomWords(4);
        fillBoardWithRandomLetters();
        placeWords();
    }

    private void loadWordsFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim().toUpperCase());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectRandomWords(int n) {
        Random random = new Random();
        List<String> selectedWordsTemp = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String word;
            do {
                word = words.get(random.nextInt(words.size()));
            } while (selectedWordsTemp.contains(word));
            selectedWordsTemp.add(word);
            System.out.println(word);
        }
        selectedWords = selectedWordsTemp;
    }

    private void fillBoardWithRandomLetters() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = (char) ('A' + (Math.random() * 26));
            }
        }
    }

    private boolean canPlaceWord(String word, int row, int col, int direction) {
        for (int i = 0; i < word.length(); i++) {
            int r = row;
            int c = col;
            switch (direction) {
                case 0 -> c += i;
                case 1 -> r += i;
                case 2 -> {
                    r += i;
                    c += i;
                }
                case 3 -> {
                    r -= i;
                    c += i;
                }
            }
            if (r < 0 || r >= SIZE || c < 0 || c >= SIZE || occupied[r][c]) {
                return false;
            }
        }
        return true;
    }

    private void placeWord(String word, int row, int col, int direction) {
        for (int i = 0; i < word.length(); i++) {
            int r = row;
            int c = col;
            switch (direction) {
                case 0 -> c += i;
                case 1 -> r += i;
                case 2 -> {
                    r += i;
                    c += i;
                }
                case 3 -> {
                    r -= i;
                    c += i;
                }
            }
            board[r][c] = word.charAt(i);
            occupied[r][c] = true;
        }
    }

    private void placeWords() {
        Random random = new Random();
        for (String word : selectedWords) {
            boolean placed = false;
            while (!placed) {
                int direction = random.nextInt(4);
                int row = random.nextInt(SIZE);
                int col = random.nextInt(SIZE);
                if (canPlaceWord(word, row, col, direction)) {
                    placeWord(word, row, col, direction);
                    placed = true;
                }
            }
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("Letter Soup");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                JButton button = new JButton(String.valueOf(board[i][j]));
                button.setFont(new Font("Arial", Font.PLAIN, 20));
                button.setEnabled(false);
                boardPanel.add(button);
            }
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JLabel foundWordsLabel = new JLabel("Palabras encontradas:");
        bottomPanel.add(foundWordsLabel);

        JTextArea foundWordsArea = new JTextArea(5, 20);
        foundWordsArea.setEditable(false);
        bottomPanel.add(new JScrollPane(foundWordsArea));

        wordInputField = new JTextField(15);
        JButton checkButton = new JButton("Verificar palabra");

        checkButton.addActionListener(e -> {
            String enteredWord = wordInputField.getText().toUpperCase();
            if (selectedWords.contains(enteredWord) && !foundWords.contains(enteredWord)) {
                foundWords.add(enteredWord);
                foundWordsArea.append(enteredWord + "\n");
                wordInputField.setText("");
                if (foundWords.size() == selectedWords.size()) {
                    JOptionPane.showMessageDialog(frame, "¡Felicidades, has encontrado todas las palabras!");
                    win = true;
                    timer.stop();
                    frame.dispose();
                }
            }
        });

        bottomPanel.add(wordInputField);
        bottomPanel.add(checkButton);

        panel.add(boardPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startTimer();
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            if (--TIME_LIMIT <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(frame, "Se acabó el tiempo. ¡Perdiste!");
                win = false;
                frame.dispose();
            }
        });
        timer.start();
    }
    @Override
    public boolean won() {
        System.out.println(win);
        return win;

    }

    public void play(PlayerData player) {
        LetterSoup game = new LetterSoup();
        initialize();
        createAndShowGUI();

        if (win) {
            player.setInteractWin(true);
        }



    }
}
