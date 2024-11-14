package org.abno.players;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class PlayerData {

    private PrintWriter writer;
    private BufferedReader reader;  // Añadido BufferedReader para lectura
    private Token token;
    private boolean ready;

    // Flags:
    private int lostTurns;

    public PlayerData(PrintWriter writer, BufferedReader reader, Token token) {
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

    public boolean isReady() {
        return ready;
    }

}
