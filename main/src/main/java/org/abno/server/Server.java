package org.abno.server;

import java.io.*;
import java.net.*;
import java.util.*;

import org.abno.players.PlayerData;
import org.abno.players.Token;

public class Server {

    //Server configuration:
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 6;
    private static Map<String, PlayerData> clientData = new HashMap<>();
    private static int activePlayers = 0;

    //Token arr
    private static Set<Token> availableTokens = new HashSet<>(Arrays.asList(
            new Token("A", "A"),
            new Token("B", "B"),
            new Token("C", "C"),
            new Token("D", "D"),
            new Token("E", "E"),
            new Token("F", "F"),
            new Token("G", "G"),
            new Token("H", "H"),
            new Token("I", "I"),
            new Token("J", "J")
    ));

    // Game
    private static boolean gameStarted = false;
    private static List<String> playersQueue = new ArrayList<>();
    private static int currentPlayerIndex = 0;

    public static void main(String[] args) {
        System.out.println("Server starting...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (activePlayers < MAX_PLAYERS) {
                new ClientHandler(serverSocket.accept()).start();
            }

            System.out.println("Server started on port " + PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientId;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Please enter your ID:");
                clientId = in.readLine();

                out.println("Please select your ficha:");
                String ficha = in.readLine();

                synchronized (availableTokens) {
                    Token selectedToken = null;

                    Iterator<Token> iterator = availableTokens.iterator();

                    do {
                        out.println("Please select a token:");
                        while (iterator.hasNext()) {
                            Token token = iterator.next();
                            if (token.getName().equals(ficha)) {
                                selectedToken = token;
                                iterator.remove();
                                break;
                            }
                        }

                        if (selectedToken == null) {
                            out.println("Please select a valid token.");
                            ficha = in.readLine();
                            iterator = availableTokens.iterator();
                        }
                    } while (selectedToken == null);

                    clientData.put(clientId, new PlayerData(out, selectedToken));
                    activePlayers++;
                    System.out.println("Client " + clientId + " connected with ficha: " + selectedToken.getName());
                    sendToAll("A new player has joined: " + clientId + " with ficha: " + selectedToken.getName());
                }


                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Message from " + clientId + ": " + message);
                    processMessage(clientId, message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (clientId != null) {
                    synchronized (clientData) {
                        clientData.remove(clientId);
                        activePlayers--;
                        System.out.println("Client " + clientId + " disconnected");

                        sendToAll("Player " + clientId + " has left.");
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void processMessage(String senderId, String message) {

            if (message.equals("@Ready")){
                getReady(senderId);
            }

            if (message.startsWith("@")) {
                int spaceIndex = message.indexOf(" ");

                if (spaceIndex != -1) {
                    String recipientId = message.substring(1, spaceIndex);
                    String actualMessage = message.substring(spaceIndex + 1);
                    sendToClient(recipientId, "From " + senderId + ": " + actualMessage);
                }
            } else {
                sendToAll(senderId + ": " + message);
            }
        }

        private void getReady(String playerReady){
            PlayerData recipientData = clientData.get(playerReady);
            if(recipientData != null) {
                recipientData.setReady();
                playersQueue.add(playerReady);
                sendToAll("Player " + playerReady + " has ready to start.");
            } else {
                sendToAll("Client " + playerReady + " has left the game.");
            }
        }

        private void sendToClient(String clientId, String message) {
            PlayerData recipientData = clientData.get(clientId);
            if (recipientData != null) {
                recipientData.getWriter().println(message);
            } else {
                out.println("Client " + clientId + " is not connected.");
            }
        }

        private void sendToAll(String message) {
            synchronized (clientData) {
                for (PlayerData data : clientData.values()) {
                    data.getWriter().println(message);
                }
            }
        }
    }

    public Set<Token> getAvailableTokens() {
        return availableTokens;
    }

}
