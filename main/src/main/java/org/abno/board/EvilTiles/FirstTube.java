package org.abno.board.EvilTiles;
import org.abno.board.Tile;
import org.abno.players.PlayerData;
import org.abno.board.Board;

public class FirstTube extends Tile implements Tubes {
    public FirstTube(int id, String img) {
        super(id, img);
    }

    @Override
    public void moveToNextTube(PlayerData player) {
        player.setOffset(7); //de acá hasta el tube 2
    }

    @Override
    public void interact(PlayerData player) {
        moveToNextTube(player);
    }

}
