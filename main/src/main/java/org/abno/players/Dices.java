package org.abno.players;

import java.util.Random;

public class Dices {
    private int dice1;
    private int dice2;
    private Random random;

    public Dices() {
        this.random = new Random();
        roll();
    }

    public void roll() {
        this.dice1 = random.nextInt(6) + 1; // Valor entre 1 y 6
        this.dice2 = random.nextInt(6) + 1; // Valor entre 1 y 6
    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    // Método para obtener la suma de los dos dados
    public int getTotal() {
        return dice1 + dice2;
    }

    @Override
    public String toString() {
        return "Dice 1: " + dice1 + ", Dice 2: " + dice2 + ", Total: " + getTotal();
    }
}
