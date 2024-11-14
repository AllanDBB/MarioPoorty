package org.abno.players;

import java.io.PrintWriter;

public class PlayerData {

    PrintWriter writer;
    Token token;
    boolean ready;

    public PlayerData(PrintWriter writer, Token token) {
        this.writer = writer;
        this.token = token;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public Token getToken() {
        return token;
    }

    public void setReady(){
        ready = true;
    }

}
