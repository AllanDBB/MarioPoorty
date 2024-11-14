package org.abno.board;

import org.abno.players.PlayerData;

public class Tile {
    private int id;
    private String img;

    public Tile(int id, String img) {
        this.id = id;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void interact(PlayerData playerData){}

}
