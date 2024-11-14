package org.abno.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.abno.players.PlayerData;
import org.abno.players.Token;
import org.abno.players.Dices;
import org.abno.players.randomNumber;
import org.abno.board.Board;

import org.abno.games.TicTacToe;

public class Server {

    // Server configuration:
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 6;
    private static Map<String, PlayerData> clientData = new HashMap<>();
    private static int activePlayers = 0;

    // Tokens
    private static Set<Token> availableTokens = new HashSet<>();

    static {
        Map<String, String> tokenImageMapping = Stream.of(new Object[][] {
                { "Mario", "Mario.png" },
                { "Luigi", "Luigi.png" },
                { "Peach", "Peach.png" },
                { "Rosalina", "Rosalina.png" },
                { "Wario", "Wario.png" },
                { "Waluigi", "Waluigi.png" },
                { "Yoshi", "Yoshi.png" },
                { "Donkey Kong", "DonkeyKong.png" },
                { "Monty Mole", "MontyMole.png" },
                { "Boo", "Boo.png" },
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        for (Map.Entry<String, String> entry : tokenImageMapping.entrySet()) {
            availableTokens.add(new Token(entry.getKey(), entry.getValue()));
        }
    }

    public static Token getTokenByName(String name) {
        return availableTokens.stream().filter(token -> token.getName().equals(name)).findFirst().orElse(null);
    }

    // Method to get all available tokens
    public static Set<Token> getAvailableTokens() {
        return Collections.unmodifiableSet(availableTokens);
    }


    // Game
    private static boolean gameStarted = false;
    private static List<String> playersQueue = new ArrayList<>();
    private static int currentPlayerIndex = 0;
    private static Board board = new Board();

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

    private static synchronized void startGame() {
        if (playersQueue.size() == activePlayers && !gameStarted && playersQueue.size() > 1) {
            // Determina el orden de juego antes de comenzar
            determinePlayerOrder();

            gameStarted = true;
            sendToAll("The game is starting!");
            sendToAll("Order of play: " + playersQueue);
            new Thread(() -> gameLoop()).start();
        }
    }

    private static void determinePlayerOrder() {
        Random random = new Random();
        int option = random.nextInt(2); // 0 o 1

        if (option == 0) {
            // Opción 1: Los jugadores eligen un número y se compara con un aleatorio
            randomNumber randomNumberGenerator = new randomNumber();
            Map<String, Integer> playerNumbers = new HashMap<>();

            // Pide a cada jugador que seleccione un número entre 1 y 1000
            for (String playerId : playersQueue) {
                PlayerData playerData = clientData.get(playerId);
                playerData.getWriter().println("Please enter a number between 1 and 1000:");
                try {
                    int chosenNumber = Integer.parseInt(playerData.getReader().readLine());
                    playerNumbers.put(playerId, chosenNumber);
                } catch (IOException | NumberFormatException e) {
                    playerNumbers.put(playerId, random.nextInt(1000) + 1); // Default en caso de error
                }
            }

            // Determina el orden de juego usando `randomNumber`
            playersQueue = randomNumberGenerator.determineOrder(playerNumbers);

        } else {
            // Opción 2: Cada jugador lanza 2 dados y se determina el orden según el valor total
            Map<String, Integer> diceResults = new HashMap<>();

            for (String playerId : playersQueue) {
                Dices dices = new Dices();
                dices.roll();
                int total = dices.getTotal();
                diceResults.put(playerId, total);
                ClientHandler.sendToClient(playerId, "You rolled " + dices.getDice1() + " and " + dices.getDice2() + " (Total: " + total + ")");
            }

            // Ordena la lista de jugadores según el resultado de los dados
            playersQueue.sort((p1, p2) -> Integer.compare(diceResults.get(p2), diceResults.get(p1)));
        }
    }

    private static void gameLoop() {
        while (gameStarted) {
            String currentPlayer = playersQueue.get(currentPlayerIndex);
            sendToAll("It's " + currentPlayer + "'s turn.");

            synchronized (Server.class) {
                try {
                    Server.class.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Game loop interrupted: " + e.getMessage());
                }
            }
        }
    }
    
    private static synchronized void sendToAll(String message) {
        for (PlayerData data : clientData.values()) {
            data.getWriter().println(message);
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

                    selectedToken.setTile(board.getTiles().getFirst());

                    clientData.put(clientId, new PlayerData(out, in, selectedToken));
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

            if (message.equals("@Roll")){
                handleRoll(senderId);
            }

            if (message.equals("@EndTurn") && Server.playersQueue.get(Server.currentPlayerIndex).equals(senderId)) {
                endTurn();
            }

            if (message.startsWith("@")) {
                int spaceIndex = message.indexOf(" ");

                if (spaceIndex != -1) {
                    String recipientId = message.substring(1, spaceIndex);
                    String actualMessage = message.substring(spaceIndex + 1);
                    sendToClient(recipientId, "From " + senderId + ": " + actualMessage);
                }
            } else {
                Server.sendToAll(senderId + ": " + message);
            }
        }

        private void getReady(String playerReady){
            PlayerData recipientData = clientData.get(playerReady);
            if(recipientData != null) {
                recipientData.setReady();
                playersQueue.add(playerReady);
                Server.sendToAll("Player " + playerReady + " is ready to start.");
                Server.startGame();
            } else {
                Server.sendToAll("Client " + playerReady + " has left the game.");
            }
        }

        private static void sendToClient(String clientId, String message) {
            PlayerData recipientData = clientData.get(clientId);
            if (recipientData != null) {
                recipientData.getWriter().println(message);
            } else {
                // Suponiendo que 'out' es un PrintWriter accesible, si no, ajusta el manejo
                System.out.println("Client " + clientId + " is not connected.");
            }
        }

        private void handleRoll(String playerId){
            if (Server.playersQueue.get(Server.currentPlayerIndex).equals(playerId)){
                Dices dices = new Dices();
                dices.roll();

                int dice1 = dices.getDice1();
                int dice2 = dices.getDice2();
                int total = dices.getTotal();

                String rollMessage = "Player " + playerId + " rolled the dice: "
                        + dice1 + " and " + dice2 + " (Total: " + total + ")";

                Server.sendToAll(rollMessage);
                sendToClient(playerId, "You rolled a " + total + ". Complete your turn with @EndTurn when done.");
            }
        }

        private void handleMove(String playerId){}

        private void endTurn() {
            // Cambiar al siguiente turno
            Server.currentPlayerIndex = (Server.currentPlayerIndex + 1) % Server.playersQueue.size();
            Server.sendToAll("Turn has ended. Next player's turn.");
        }

    }

}
