package org.abno.server;

import java.io.*;
import java.net.*;
import org.abno.main.Frames.InitFrame;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private static PrintWriter out;

    private static void init() {
        new InitFrame();
    }

    public static void sendValue(String value) {
        if (out != null) {
            out.println(value);
            System.out.println("Valor enviado al servidor: " + value);
        } else {
            System.out.println("Vea mi loco algo pasó, usted compiló está vara y no debería de funcionar XD");
        }
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected.");

            init();

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Mensaje del servidor: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            String userInput;
            while ((userInput = consoleInput.readLine()) != null) {
                sendValue(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
