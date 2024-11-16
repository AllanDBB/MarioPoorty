package org.abno.board;

import org.abno.board.EvilTiles.*;
import org.abno.games.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Board {

    private List<Tile> tiles;
    private int maxTiles;
    private Random random;

    public Board() {
        this.tiles = new ArrayList<Tile>();
        this.maxTiles = 38;  // Al menos 38 casillas en el tablero
        this.random = new Random();
        fillBoard();

    }

    public void fillBoard() {
        tiles.clear();

        /*for (int i = 0; i < 18; i++) {
            int id = i + 1;
            String imgRoute = "path/to/special_tile_image_" + id + ".png";
            tiles.add(new SpecialTile(id, imgRoute));
        }*/

        tiles.add(new SpecialTile(2, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(5, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(6, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(7, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(8, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(10, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(12,  "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(14, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(16, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(21, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(27, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(29, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(32, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(33, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(34, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(35, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(36, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(37, "SpecialTile.png", new CatchTheCat()));

        tiles.add(new FirstTube(19, "FirstTube.png")); //FirstTube
        tiles.add(new Jail(13, "Jail.png")); //Jail
        tiles.add(new IceFlower(30, "IceFlower.png")); //IceFlower
        tiles.add(new FireFlower(3, "FireFlower.png")); //FireFlower
        tiles.add(new Star(17, "Star.png")); //Star
        tiles.add(new Tail(20, "Tail.png")); //Tail
        tiles.add(new Jail(22, "Jail.png")); //Jail
        tiles.add(new SecondTube(26, "SecondTube.png")); //SecondTube
        tiles.add(new Star(1, "Star.png")); //Star
        tiles.add(new ThirdTube(28, "ThirdTube.png")); //ThirdTube

        tiles.add(new Tile(0, "NormalTile.png"));
        tiles.add(new Tile(4, "NormalTile.png"));
        tiles.add(new Tile(9, "NormalTile.png"));
        tiles.add(new Tile(11, "NormalTile.png"));
        tiles.add(new Tile(15, "NormalTile.png"));
        tiles.add(new Tile(18, "NormalTile.png"));
        tiles.add(new Tile(23, "NormalTile.png"));
        tiles.add(new Tile(24, "NormalTile.png"));
        tiles.add(new Tile(25, "NormalTile.png"));
        tiles.add(new Tile(31, "NormalTile.png"));

        sortTilesById();
    }

    public void sortTilesById() {
        // Iteramos por todas las fichas de la lista
        for (int i = 0; i < tiles.size(); i++) {
            // Encontramos el índice del elemento con el ID más pequeño desde i hasta el final
            int minIndex = i;

            for (int j = i + 1; j < tiles.size(); j++) {
                // Si encontramos un tile con un ID menor, actualizamos minIndex
                if (tiles.get(j).getId() < tiles.get(minIndex).getId()) {
                    minIndex = j;
                }
            }

            // Si minIndex ha cambiado, intercambiamos las fichas
            if (minIndex != i) {
                Tile temp = tiles.get(i);
                tiles.set(i, tiles.get(minIndex));
                tiles.set(minIndex, temp);
            }
        }
    }


    public List<Tile> getTiles() {
        return tiles;
    }
}
