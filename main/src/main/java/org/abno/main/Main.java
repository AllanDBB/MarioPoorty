package org.abno.main;

import org.abno.main.Frames.InitFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::init);
    }

    private static void init() {
        new InitFrame();
    }

}
