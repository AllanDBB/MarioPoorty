package org.abno.games;

import org.abno.players.PlayerData;

public interface Game {
    public default void play(PlayerData player){};
}
