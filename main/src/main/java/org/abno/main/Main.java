package org.abno.main;

import org.abno.main.Frames.InitFrame;
import org.abno.server.Server;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::startServer);
    }

    private static void startServer() {
        new Thread(() -> {
            try {
                System.out.println("Starting the server...");
                Server.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
