package org.abno.games;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LetterSoup {

    private static final int SIZE = 10;
    private char[][] board = new char[SIZE][SIZE];
    private boolean[][] occupied = new boolean[SIZE][SIZE];
    private List<String> words = new ArrayList<>();
    private List<String> selectedWords = new ArrayList<>();
    private List<String> foundWords = new ArrayList<>();
    private JFrame frame;
    private JPanel panel;
    private JTextField wordInputField;

    public LetterSoup() {

        loadWordsFromFile("C:\\Users\\adbyb\\OneDrive\\Documentos\\GitHub\\MarioPoorty\\main\\src\\main\\java\\utils\\words");


        selectRandomWords(4);


        fillBoardWithRandomLetters();


        placeWords();


        createAndShowGUI();
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
                case 0:
                    c += i;
                    break;
                case 1:
                    r += i;
                    break;
                case 2:
                    r += i;
                    c += i;
                    break;
                case 3:
                    r -= i;
                    c += i;
                    break;
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
                case 0:
                    c += i;
                    break;
                case 1:
                    r += i;
                    break;
                case 2:
                    r += i;
                    c += i;
                    break;
                case 3:
                    r -= i;
                    c += i;
                    break;
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                    System.exit(0);
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LetterSoup());
    }
}
