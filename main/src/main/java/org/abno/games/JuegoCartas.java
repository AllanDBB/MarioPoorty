package org.abno.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JuegoCartas extends JFrame {
    private List<Carta> mazo;
    private List<Jugador> jugadores;
    private JPanel panelCartas;
    private JLabel lblGanador;

    public JuegoCartas() {
        setTitle("Juego de Cartas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        jugadores = new ArrayList<>();
        jugadores.add(new Jugador("Jugador 1"));
        jugadores.add(new Jugador("Jugador 2"));

        inicializarMazo();

        panelCartas = new JPanel();
        panelCartas.setLayout(new GridLayout(1, jugadores.size()));
        lblGanador = new JLabel("Seleccione una carta para cada jugador", JLabel.CENTER);

        for (Jugador jugador : jugadores) {
            JButton btnSeleccionarCarta = new JButton(jugador.getNombre());
            btnSeleccionarCarta.addActionListener(e -> seleccionarCarta(jugador, btnSeleccionarCarta));
            panelCartas.add(btnSeleccionarCarta);
        }

        JButton btnVerGanador = new JButton("Ver Ganador");
        btnVerGanador.addActionListener(e -> determinarGanador());

        add(panelCartas, BorderLayout.CENTER);
        add(lblGanador, BorderLayout.NORTH);
        add(btnVerGanador, BorderLayout.SOUTH);
    }

    private void inicializarMazo() {
        mazo = new ArrayList<>();
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] palos = {"corazones", "tréboles", "picas", "rombos"};
        String[] emojis = {"❤️", "♣️", "♠️", "♦️"};

        for (int i = 0; i < palos.length; i++) {
            String emoji = emojis[i];
            for (String valor : valores) {
                mazo.add(new Carta(valor, palos[i], emoji));
            }
        }
        Collections.shuffle(mazo);
    }

    private void seleccionarCarta(Jugador jugador, JButton boton) {
        if (!mazo.isEmpty()) {
            Carta cartaSeleccionada = mazo.remove(0);
            jugador.seleccionarCarta(cartaSeleccionada);
            boton.setText(cartaSeleccionada.getValor() + " " + cartaSeleccionada.getEmoji());
        } else {
            JOptionPane.showMessageDialog(this, "No quedan cartas en el mazo.");
        }
    }

    private void determinarGanador() {
        Jugador ganador = jugadores.get(0);

        for (Jugador jugador : jugadores) {
            Carta carta = jugador.getCartaSeleccionada();
            Carta cartaGanador = ganador.getCartaSeleccionada();
            if (carta.obtenerValorNumerico() > cartaGanador.obtenerValorNumerico() ||
                    (carta.obtenerValorNumerico() == cartaGanador.obtenerValorNumerico() &&
                            carta.obtenerPrioridadPalo() > cartaGanador.obtenerPrioridadPalo())) {
                ganador = jugador;
            }
        }

        lblGanador.setText("El ganador es " + ganador.getNombre() + " con la carta " +
                ganador.getCartaSeleccionada().getValor() + " " + ganador.getCartaSeleccionada().getEmoji());
    }

    private class Carta {
        private String valor;
        private String palo;
        private String emoji;

        public Carta(String valor, String palo, String emoji) {
            this.valor = valor;
            this.palo = palo;
            this.emoji = emoji;
        }

        public String getValor() {
            return valor;
        }

        public String getPalo() {
            return palo;
        }

        public String getEmoji() {
            return emoji;
        }

        public int obtenerValorNumerico() {
            switch (valor) {
                case "2": return 2;
                case "3": return 3;
                case "4": return 4;
                case "5": return 5;
                case "6": return 6;
                case "7": return 7;
                case "8": return 8;
                case "9": return 9;
                case "10": return 10;
                case "J": return 11;
                case "Q": return 12;
                case "K": return 13;
                case "A": return 14;
                default: return 0;
            }
        }

        public int obtenerPrioridadPalo() {
            switch (palo) {
                case "corazones": return 4;
                case "tréboles": return 3;
                case "picas": return 2;
                case "rombos": return 1;
                default: return 0;
            }
        }
    }

    private class Jugador {
        private String nombre;
        private Carta cartaSeleccionada;

        public Jugador(String nombre) {
            this.nombre = nombre;
        }

        public void seleccionarCarta(Carta carta) {
            this.cartaSeleccionada = carta;
        }

        public Carta getCartaSeleccionada() {
            return cartaSeleccionada;
        }

        public String getNombre() {
            return nombre;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JuegoCartas juego = new JuegoCartas();
            juego.setVisible(true);
        });
    }
}
