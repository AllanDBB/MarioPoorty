package org.abno.board.EvilTiles;

import org.abno.board.Tile;
import org.abno.players.PlayerData;

public class Star extends Tile {
    public Star(int id, String img) {
        super(id, img);
    }

    public void rollAgain(PlayerData player){
        player.setRollDiceAgain(true);
    }

    @Override
    public void interact(PlayerData player) {
        rollAgain(player);
    }
}
