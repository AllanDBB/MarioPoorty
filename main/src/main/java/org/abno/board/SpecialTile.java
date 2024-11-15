package org.abno.board;

import org.abno.players.PlayerData;
import org.abno.games.Game;
import org.abno.players.PlayerData;

public class SpecialTile extends Tile{

    Game game;

    public SpecialTile(int id, String imgRoute, Game game){
        super(id, imgRoute);
        this.game = game;
    }

    public void interact(PlayerData player){
        game.play(player);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
