package org.abno.players;


import org.abno.board.Tile;

public class Token {

    private String name;
    private String img;
    private Tile tile;

    public Token(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
