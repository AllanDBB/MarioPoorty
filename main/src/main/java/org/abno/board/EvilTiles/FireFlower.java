package org.abno.board.EvilTiles;

import org.abno.board.Tile;
import org.abno.players.PlayerData;

public class FireFlower extends Tile {
    public FireFlower(int id, String img) {
        super(id, img);
    }

    public void restart(PlayerData player){ //en este caso no es al que le sale, sino a quien elija
        player.setRestart(true);
    }

    @Override
    public void interact(PlayerData player) {
        restart(player);
    }
}
