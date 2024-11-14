package org.abno.board.EvilTiles;

import org.abno.board.Board;
import org.abno.board.Tile;
import org.abno.players.PlayerData;

public class ThirdTube extends Tile implements Tubes{
    public ThirdTube(int id, String img) {
        super(id, img);
    }

    @Override
    public void moveToNextTube(PlayerData player) {
        player.setOffset(19-28);//de acá hasta el tube 1
    }
    @Override
    public void interact(PlayerData player) {
        moveToNextTube(player);
    }
}
