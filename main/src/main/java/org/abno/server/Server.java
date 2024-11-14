package org.abno.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 6;
    private static List<PrintWriter> playerOutputStreams = new ArrayList<>();
    private static int activePlayers = 0;

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

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Add client to server
                synchronized (playerOutputStreams) {
                    playerOutputStreams.add(out);
                    activePlayers++;
                    System.out.println("Client " + socket.getRemoteSocketAddress() + " connected");
                }

                sendToAll("A new player has joined.");

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Message from " + socket.getRemoteSocketAddress() + ": " + message);
                    sendToAll(message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    synchronized (playerOutputStreams) {
                        if (out != null) {
                            playerOutputStreams.remove(out);
                        }
                        activePlayers--;
                        System.out.println("Client " + socket.getRemoteSocketAddress() + " disconnected");

                        if (activePlayers == 0) {
                            sendToAll("All players have disconnected.");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (socket != null && !socket.isClosed()) {
                            socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void sendToAll(String message) {
            synchronized (playerOutputStreams) {
                for (PrintWriter pw : playerOutputStreams) {
                    pw.println(message);
                }
            }
        }
    }
}
