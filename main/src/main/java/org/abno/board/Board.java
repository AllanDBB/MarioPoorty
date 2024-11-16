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

        tiles.add(new SpecialTile(0, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(1, "SpecialTile.png", new CatchTheCat()));

        tiles.add(new SpecialTile(2, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(3, "SpecialTile.png", new CatchTheCat()));

        tiles.add(new SpecialTile(4, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(5, "SpecialTile.png", new CatchTheCat()));

        tiles.add(new SpecialTile(6,  "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(7, "SpecialTile.png", new CatchTheCat()));

        tiles.add(new SpecialTile(8, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(9, "SpecialTile.png", new CatchTheCat()));

        tiles.add(new SpecialTile(10, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(11, "SpecialTile.png", new CatchTheCat()));

        //estas son las de sockets pero mientras tanto
        tiles.add(new SpecialTile(12, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(13, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(14, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(15, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(16, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(17, "SpecialTile.png", new CatchTheCat()));
        tiles.add(new SpecialTile(18, "SpecialTile.png", new CatchTheCat()));

        /*for (int i = 0; i < 10; i++) {
            int id = i + 19;
            String imgRoute = "path/to/evil_tile_image_" + id + ".png";
            tiles.add(new EvilTile(id, imgRoute));
        }*/

        tiles.add(new FirstTube(19, "FirstTube.png")); //FirstTube
        tiles.add(new Jail(20, "Jail.png")); //Jail
        tiles.add(new IceFlower(21, "IceFlower.png")); //IceFlower
        tiles.add(new FireFlower(22, "FireFlower.png")); //FireFlower
        tiles.add(new Star(23, "Star.png")); //Star
        tiles.add(new Tail(24, "Tail.png")); //Tail
        tiles.add(new Jail(25, "Jail.png")); //Jail
        tiles.add(new SecondTube(26, "SecondTube.png")); //SecondTube
        tiles.add(new Star(27, "Star.png")); //Star
        tiles.add(new ThirdTube(28, "ThirdTube.png")); //ThirdTube


        for (int i = 0; i < (maxTiles - 18 - 10); i++) {
            int id = i + 29;  // Continuamos el conteo desde 29
            String imgRoute = "NormalTile.png";
            tiles.add(new Tile(id, imgRoute));
        }

        shuffleBoard();

        arrangeSpecialTiles();
    }

    private void shuffleBoard() {
        for (int i = 0; i < tiles.size(); i++) {
            if (i == 19 || i == 26 || i == 28){
                continue;
            }

            int j = random.nextInt(tiles.size());

            while (j == 19 || j == 26 || j ==28){
                j = random.nextInt(tiles.size());
            }

            Tile temp = tiles.get(i);
            tiles.set(i, tiles.get(j));
            tiles.set(j, temp);
        }
    }


    public void arrangeSpecialTiles() {
        placeTileAtPosition(19);
        placeTileAtPosition(26);
        placeTileAtPosition(28);


        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getId() != 19 && tiles.get(i).getId() != 26 && tiles.get(i).getId() != 28) {
                tiles.get(i).setId(i);
            }
        }
    }


    private void placeTileAtPosition(int desiredPosition) {
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getId() == desiredPosition) {

                Tile temp = tiles.get(desiredPosition);
                tiles.set(desiredPosition, tiles.get(i));
                tiles.set(i, temp);
                break;
            }
        }
    }


    public List<Tile> getTiles() {
        return tiles;
    }
}
