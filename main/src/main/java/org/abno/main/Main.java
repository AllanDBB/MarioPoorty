package org.abno.main;

import org.abno.board.Board;
import org.abno.board.Tile;
import org.abno.main.Frames.InitFrame;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::init);
    }

    private static void init() {
        new InitFrame();
    }

}
