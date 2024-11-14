package org.abno.board;

import java.util.ArrayList;
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
    }

    public void fillBoard() {
        tiles.clear();

        for (int i = 0; i < 18; i++) {
            int id = i + 1;
            String imgRoute = "path/to/special_tile_image_" + id + ".png";
            tiles.add(new SpecialTile(id, imgRoute));
        }

        for (int i = 0; i < 10; i++) {
            int id = i + 19;
            String imgRoute = "path/to/evil_tile_image_" + id + ".png";
            tiles.add(new EvilTile(id, imgRoute));
        }

        for (int i = 0; i < (maxTiles - 18 - 10); i++) {
            int id = i + 29;  // Continuamos el conteo desde 29
            String imgRoute = "path/to/normal_tile_image_" + id + ".png";
            tiles.add(new Tile(id, imgRoute));
        }

        shuffleBoard();
    }

    private void shuffleBoard() {
        for (int i = 0; i < tiles.size(); i++) {
            int j = random.nextInt(tiles.size());
            Tile temp = tiles.get(i);
            tiles.set(i, tiles.get(j));
            tiles.set(j, temp);
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}
