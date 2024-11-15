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

        tiles.add(new SpecialTile(0, "path/to/evil_tile_image_0.png", new JuegoCartas()));
        tiles.add(new SpecialTile(1, "path/to/evil_tile_image_1.png", new JuegoCartas()));

        tiles.add(new SpecialTile(2, "path/to/evil_tile_image_2.png", new CatchTheCat()));
        tiles.add(new SpecialTile(3, "path/to/evil_tile_image_3.png", new CatchTheCat()));

        tiles.add(new SpecialTile(4, "path/to/evil_tile_image_4.png", new CollectTheCoins()));
        tiles.add(new SpecialTile(5, "path/to/evil_tile_image_5.png", new CollectTheCoins()));

        tiles.add(new SpecialTile(6, "path/to/evil_tile_image_6.png", new GuessTheCharacterGame()));
        tiles.add(new SpecialTile(7, "path/to/evil_tile_image_7.png", new GuessTheCharacterGame()));

        tiles.add(new SpecialTile(8, "path/to/evil_tile_image_8.png", new LetterSoup()));
        tiles.add(new SpecialTile(9, "path/to/evil_tile_image_9.png", new LetterSoup()));

        tiles.add(new SpecialTile(10, "path/to/evil_tile_image_10.png", new MemoryPath()));
        tiles.add(new SpecialTile(11, "path/to/evil_tile_image_11.png", new MemoryPath()));

        //estas son las de sockets pero mientras tanto
        tiles.add(new SpecialTile(12, "path/to/evil_tile_image_12.png", new TreasureHuntGame()));
        tiles.add(new SpecialTile(13, "path/to/evil_tile_image_13.png", new TreasureHuntGame()));

        tiles.add(new SpecialTile(14, "path/to/evil_tile_image_14.png", new LetterSoup()));
        tiles.add(new SpecialTile(15, "path/to/evil_tile_image_15.png", new LetterSoup()));
        tiles.add(new SpecialTile(16, "path/to/evil_tile_image_16.png", new GuessTheCharacterGame()));
        tiles.add(new SpecialTile(17, "path/to/evil_tile_image_17.png", new GuessTheCharacterGame()));
        tiles.add(new SpecialTile(18, "path/to/evil_tile_image_18.png", new JuegoCartas()));

        /*for (int i = 0; i < 10; i++) {
            int id = i + 19;
            String imgRoute = "path/to/evil_tile_image_" + id + ".png";
            tiles.add(new EvilTile(id, imgRoute));
        }*/

        tiles.add(new FirstTube(19, "path/to/evil_tile_image_19.png")); //FirstTube
        tiles.add(new Jail(20, "path/to/evil_tile_image_20.png")); //Jail
        tiles.add(new IceFlower(21, "path/to/evil_tile_image_21.png")); //IceFlower
        tiles.add(new FireFlower(22, "path/to/evil_tile_image_22.png")); //FireFlower
        tiles.add(new Star(23, "path/to/evil_tile_image_23.png")); //Star
        tiles.add(new Tail(24, "path/to/evil_tile_image_24.png")); //Tail
        tiles.add(new Jail(25, "path/to/evil_tile_image_25.png")); //Jail
        tiles.add(new SecondTube(26, "path/to/evil_tile_image_26.png")); //SecondTube
        tiles.add(new Star(27, "path/to/evil_tile_image_27.png")); //Star
        tiles.add(new ThirdTube(28, "path/to/evil_tile_image_28.png")); //ThirdTube


        for (int i = 0; i < (maxTiles - 18 - 10); i++) {
            int id = i + 29;  // Continuamos el conteo desde 29
            String imgRoute = "path/to/normal_tile_image_" + id + ".png";
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
