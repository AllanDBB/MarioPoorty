package org.abno.board;

public abstract class Tile {
    private int id;
    private String img;

    public Tile(int id, String img) {
        this.id = id;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public abstract void interact();

}
