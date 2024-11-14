package org.abno.board.EvilTiles;

import org.abno.board.Tile;
import org.abno.players.PlayerData;

public class SecondTube extends Tile implements Tubes{
    public SecondTube(int id, String img) {
        super(id, img);
    }

    @Override
    public void moveToNextTube(PlayerData player) {
        player.setOffset(2); //de acá hasta el tube 3
    }

    @Override
    public void interact(PlayerData player) {
        moveToNextTube(player);
    }
}
