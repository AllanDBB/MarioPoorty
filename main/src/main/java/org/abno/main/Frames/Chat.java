package org.abno.main.Frames;

import org.abno.server.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Chat extends JPanel {

    private JTextArea chatLog;
    private static final Color CHAT_BACKGROUND_COLOR = new Color(30, 14, 60);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font CHAT_FONT = new Font("Arial", Font.PLAIN, 16);
    private static final Dimension CHAT_SIZE = new Dimension(1092, 150);
    private static final Dimension CHAT_LOG_SIZE = new Dimension(1092, 100);
    private static final Dimension CHAT_INPUT_SIZE = new Dimension(1092, 50);

    public Chat(String selectedId) {
        setUpMainPanel();
        add(setUpChatLogPanel(), BorderLayout.NORTH);
        add(setUpChatInputPanel(), BorderLayout.SOUTH);
    }

    private void setUpMainPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(CHAT_SIZE);
    }

    private JPanel setUpChatLogPanel() {
        chatLog = createChatLogArea();
        JScrollPane chatLogScrollPane = new JScrollPane(chatLog);
        setTransparentScrollPane(chatLogScrollPane);

        JPanel chatLogPanel = new JPanel(new BorderLayout());
        chatLogPanel.setPreferredSize(CHAT_LOG_SIZE);
        chatLogPanel.add(chatLogScrollPane, BorderLayout.CENTER);
        chatLogPanel.setBackground(CHAT_BACKGROUND_COLOR);

        return chatLogPanel;
    }

    private JTextArea createChatLogArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(CHAT_FONT);
        textArea.setForeground(TEXT_COLOR);
        textArea.setBackground(CHAT_BACKGROUND_COLOR);

        return textArea;
    }

    private void setTransparentScrollPane(JScrollPane scrollPane) {
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
    }

    private JPanel setUpChatInputPanel() {
        JTextField chatInput = createChatInputField();

        JPanel chatInputPanel = new JPanel(new GridBagLayout());
        chatInputPanel.setPreferredSize(CHAT_INPUT_SIZE);
        chatInputPanel.setBackground(CHAT_BACKGROUND_COLOR);
        chatInputPanel.add(chatInput);

        return chatInputPanel;
    }

    private JTextField createChatInputField() {
        JTextField chatInput = new JTextField();
        chatInput.setPreferredSize(CHAT_INPUT_SIZE);
        chatInput.setFont(CHAT_FONT);
        chatInput.setBackground(Color.WHITE); // Changed to white
        chatInput.setForeground(Color.BLACK); // Changed for contrast with white background
        chatInput.setCaretColor(Color.BLACK); // Caret color for visibility

        // Add action listener to handle Enter key press

        chatInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = chatInput.getText().trim();
                if (!text.isEmpty()) {
                    Client.sendValue(text);
                    chatInput.setText("");
                }
            }
        });

        return chatInput;
    }

    public void externMessage(String message, String user){
        if (!message.isEmpty()){
            chatLog.append(String.format("%s %s%n", user, message));
            chatLog.setCaretPosition(chatLog.getDocument().getLength());
        }
    }

}