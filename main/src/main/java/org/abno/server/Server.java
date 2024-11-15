package org.abno.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.abno.board.EvilTiles.FireFlower;
import org.abno.board.EvilTiles.IceFlower;
import org.abno.board.EvilTiles.Jail;
import org.abno.board.EvilTiles.Tail;
import org.abno.board.Tile;
import org.abno.players.PlayerData;
import org.abno.players.Token;
import org.abno.players.Dices;
import org.abno.players.randomNumber;
import org.abno.board.Board;

import org.abno.games.TicTacToe;

public class Server {

    // Server configuration:
    private static final int PORT = 12346;
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
    private static List<String> playersQueue = new CopyOnWriteArrayList<>();
    private static int currentPlayerIndex = 0;
    private static Board board = new Board();
    private static boolean gameEnded = false;


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
        while (gameStarted && !gameEnded) {
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

                    selectedToken.setTile(board.getTiles().get(0));

                    clientData.put(clientId, new PlayerData(out, in, selectedToken));
                    activePlayers++;
                    System.out.println("Client " + clientId + " connected with ficha: " + selectedToken.getName());
                    sendToAll("A new player has joined: " + clientId + " with ficha: " + selectedToken.getName());
                }


                String message;
                while ((message = in.readLine()) != null) {
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

        private void processMessage(String senderId, String message) throws IOException {

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
                Server.playersQueue.add(playerReady);
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

        private void handleRoll(String playerId) throws IOException {
            if (Server.playersQueue.get(Server.currentPlayerIndex).equals(playerId)){
                PlayerData data = clientData.get(clientId);
                if (data.getLostTurns() > 0){
                    data.setLostTurns(data.getLostTurns()-1);
                    sendToClient(playerId, String.valueOf(data.getLostTurns())+" turnos restantes sin jugar");
                    endTurn();
                    return;
                }
                if (data.isRestart()){
                    data.getToken().setTile(board.getTiles().get(0));
                    data.setRestart(false);
                    sendToClient(playerId, " de vuelta al inicio");
                    endTurn();
                    return;
                }



                Dices dices = new Dices();
                dices.roll();

                int dice1 = dices.getDice1();
                int dice2 = dices.getDice2();
                int total = dices.getTotal();

                String rollMessage = "Player " + playerId + " rolled the dice: "
                        + dice1 + " and " + dice2 + " (Total: " + total + ")";

                Server.sendToAll(rollMessage);

                if (clientData.get(playerId).getToken().getTile().getId()+total >= 38){
                    gameEnded = true;

                    sendToAll("FIN YA GANARON");
                    gameStarted = false;
                    return;
                } else{
                    handleMove(playerId, total);
                    sendToClient(playerId, String.valueOf(data.getToken().getTile().getId())+" es el cuadro actual ");

                    if (data.isRollDiceAgain()){
                        handleRoll(playerId);
                    }

                    if (data.getOffset()>0){
                        if (data.getToken().getTile().getId()+data.getOffset()>=38){
                            gameEnded = true;
                        } else if (data.getToken().getTile().getId()+data.getOffset()<=0){
                            data.getToken().setTile(board.getTiles().get(0));
                            endTurn();
                        }
                        else{
                            handleMove(playerId, data.getOffset());
                            sendToClient(playerId, String.valueOf(data.getToken().getTile().getId())+" es el cuadro actual ");
                            data.setOffset(0);
                        }
                    }
                }
                sendToClient(playerId, "You rolled a " + total + ". Complete your turn with @EndTurn when done.");
            }
        }

        private void handleMove(String playerId, int spaces) throws IOException {

            int actualTile = clientData.get(playerId).getToken().getTile().getId();
            clientData.get(playerId).getToken().setTile(board.getTiles().get(actualTile+spaces));
            PlayerData data = clientData.get(playerId);

            for (Tile t : board.getTiles()){
                if (t.getId() == clientData.get(playerId).getToken().getTile().getId()){
                    if (clientData.get(playerId).getToken().getTile().getClass() == IceFlower.class || clientData.get(playerId).getToken().getTile().getClass() == FireFlower.class){
                        out.println("Escoja a quien atacar");
                        out.println(playersQueue.toArray().toString());
                        String p = in.readLine();
                        for (String player: playersQueue){
                            out.println(player);
                            out.println(p);
                            if (Objects.equals(p, player)){
                                out.println("encontrado");
                                t.interact(clientData.get(player));
                                out.println(clientData.get(player).getToken().getTile().getId());
                                break;
                            }                        }
                    } else if (clientData.get(playerId).getToken().getTile().getClass() == Tail.class){
                        out.println("Escoja cuánto moverse (-3 a +3)");
                        String p = in.readLine();
                        ((Tail) t).jump(clientData.get(playerId), Integer.parseInt(p));
                    }
                    else{
                    out.println("Entraste a una casilla WUJUU! " + t.getClass());
                    t.interact(data);
                    }
                }

            }
        }

        private void endTurn() {
            // Cambiar al siguiente turno
            Server.currentPlayerIndex = (Server.currentPlayerIndex + 1) % Server.playersQueue.size();
            Server.sendToAll("Turn has ended. Next player's turn.");
        }

    }

}
