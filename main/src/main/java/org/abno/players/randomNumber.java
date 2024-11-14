package org.abno.players;

import java.util.*;

public class randomNumber {

    public List<String> determineOrder(Map<String, Integer> players) {
        int randomNum = generateRandomNumber();
        System.out.println("Número aleatorio generado: " + randomNum);

        List<String> orderedPlayers = new ArrayList<>(players.keySet());
        orderedPlayers.sort(Comparator.comparingInt(player -> Math.abs(players.get(player) - randomNum)));

        return orderedPlayers;
    }

    private int generateRandomNumber() {
        return new Random().nextInt(1000) + 1;
    }


}
