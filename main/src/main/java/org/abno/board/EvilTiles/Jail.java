package org.abno.board.EvilTiles;

import org.abno.board.Tile;
import org.abno.players.PlayerData;

public class Jail extends Tile {
    public Jail(int id, String img) {
        super(id, img);
    }

    public void goToJail(PlayerData player){
        player.setLostTurns(player.getLostTurns()+2); //se acumulan, cada vez se suman dos
    }

    @Override
    public void interact(PlayerData player) {
        goToJail(player);
    }

}
