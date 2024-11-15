package org.abno.players;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class PlayerData {

    private PrintWriter writer;
    private BufferedReader reader;  // Añadido BufferedReader para lectura
    private Token token;
    private boolean ready;

    // Flags:
    int lostTurns;
    boolean rollDiceAgain = false;
    int offset;
    boolean restart;
    boolean interactWin = true;

    public PlayerData(PrintWriter writer, BufferedReader reader,Token token) {
        this.writer = writer;
        this.reader = reader;
        this.token = token;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public Token getToken() {
        return token;
    }

    public void setReady() {
        ready = true;
    }

    public int getLostTurns() {
        return lostTurns;
    }

    public void setLostTurns(int lostTurns) {
        this.lostTurns = lostTurns;
    }

    public boolean isRollDiceAgain() {
        return rollDiceAgain;
    }

    public void setRollDiceAgain(boolean rollDiceAgain) {
        this.rollDiceAgain = rollDiceAgain;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isRestart() {
        return restart;
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }
    public boolean isReady() {
        return ready;
    }

    public void changeInteract(){
        this.interactWin = !interactWin;
    }

    public boolean isInteractWin() {
        return interactWin;
    }

    public void setInteractWin(boolean interactWin) {
        this.interactWin = interactWin;
    }
}
