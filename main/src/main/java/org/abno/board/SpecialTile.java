package org.abno.board;

import org.abno.players.PlayerData;
import org.abno.games.Game;
import org.abno.players.PlayerData;

public class SpecialTile extends Tile{


    public SpecialTile(int id, String imgRoute){
        super(id, imgRoute);
    }

    public void interact(PlayerData player){;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
