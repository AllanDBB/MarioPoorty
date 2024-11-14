package org.abno.board.EvilTiles;

import org.abno.board.Tile;
import org.abno.players.PlayerData;

public class IceFlower extends Tile {
    public IceFlower(int id, String img) {
        super(id, img);
    }

    public void freeze(PlayerData player){
        player.setLostTurns(player.getLostTurns()+2);
    }

    @Override
    public void interact(PlayerData player) {
        freeze(player);
    }
}
