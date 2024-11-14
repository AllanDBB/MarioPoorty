package org.abno.board.EvilTiles;

import org.abno.board.Tile;
import org.abno.players.PlayerData;

public class Tail extends Tile {
    public Tail(int id, String img) {
        super(id, img);
    }

    public void jump(PlayerData player, int offset){
        player.setOffset(offset);
    }

}
