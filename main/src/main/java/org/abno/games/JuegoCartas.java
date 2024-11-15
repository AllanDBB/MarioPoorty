package org.abno.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class JuegoCartas extends JFrame implements Game {
    private List<Carta> mazo;
    private List<Jugador> jugadores;
    private JPanel panelCartas;
    private JLabel lblGanador;
    private Timer timer;
    private int indiceCarta = 0;
    private Jugador ganador;

    public JuegoCartas() {
        setTitle("Juego de Cartas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        panelCartas = new JPanel();
        panelCartas.setLayout(new GridLayout(1, 1));  // Ajustar dinámicamente según la cantidad de jugadores
        lblGanador = new JLabel("Esperando cartas...", JLabel.CENTER);

        add(panelCartas, BorderLayout.CENTER);
        add(lblGanador, BorderLayout.NORTH);
    }

    // Método para reiniciar el estado del juego
    private void resetGame() {
        // Reiniciar las variables
        mazo = null;
        jugadores = null;
        indiceCarta = 0;
        ganador = null;

        // Limpiar el panel de cartas y restablecer el mensaje del ganador
        panelCartas.removeAll();
        panelCartas.revalidate();
        panelCartas.repaint();

        lblGanador.setText("Esperando cartas...");
    }

    // Método que inicializa el juego y retorna el nombre del ganador

    public String playCards(List<String> players) {
        resetGame();
        setVisible(true);

        // Inicializar jugadores y mazo
        jugadores = new ArrayList<>();
        for (String nombre : players) {
            jugadores.add(new Jugador(nombre));
        }
        inicializarMazo();

        panelCartas.setLayout(new GridLayout(1, jugadores.size()));

        // Crear botones para los jugadores
        for (Jugador jugador : jugadores) {
            JButton btnCarta = new JButton(jugador.getNombre() + ": Esperando...");
            btnCarta.setEnabled(false);
            panelCartas.add(btnCarta);
        }

        // CountDownLatch para sincronizar con el SwingWorker
        final CountDownLatch latch = new CountDownLatch(1);

        // Worker para manejar el juego
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // Iniciar el Timer
                timer = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (indiceCarta < jugadores.size() && !mazo.isEmpty()) {
                            Carta carta = mazo.remove(0);
                            jugadores.get(indiceCarta).seleccionarCarta(carta);

                            JButton btnCarta = (JButton) panelCartas.getComponent(indiceCarta);
                            btnCarta.setText(jugadores.get(indiceCarta).getNombre() + ": " +
                                    carta.getValor() + " " + carta.getEmoji());
                            btnCarta.setEnabled(false);

                            indiceCarta++;

                            if (indiceCarta >= jugadores.size()) {
                                timer.stop();
                                determinarGanador();

                                // Liberar el CountDownLatch para notificar al hilo principal
                                latch.countDown();
                            }
                        }
                    }
                });
                timer.start();

                // Esperar hasta que el juego termine
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        };

        worker.execute();

        // Esperar hasta que el juego termine y se determine el ganador
        try {
            latch.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        // Retornar el nombre del ganador
        return ganador != null ? ganador.getNombre() : "No hay ganador";
    }


    private void inicializarMazo() {
        mazo = new ArrayList<>();
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] palos = {"corazones", "tréboles", "picas", "rombos"};
        String[] emojis = {"❤️", "♣️", "♠️", "♦️"};

        // Inicializar el mazo con las cartas
        for (int i = 0; i < palos.length; i++) {
            String emoji = emojis[i];
            for (String valor : valores) {
                mazo.add(new Carta(valor, palos[i], emoji));
            }
        }
        Collections.shuffle(mazo);
    }

    private void determinarGanador() {
        ganador = jugadores.get(0);

        for (Jugador jugador : jugadores) {
            Carta carta = jugador.getCartaSeleccionada();
            Carta cartaGanador = ganador.getCartaSeleccionada();

            if (carta.obtenerValorNumerico() > cartaGanador.obtenerValorNumerico() ||
                    (carta.obtenerValorNumerico() == cartaGanador.obtenerValorNumerico() &&
                            carta.obtenerPrioridadPalo() > cartaGanador.obtenerPrioridadPalo())) {

                ganador = jugador;
            }
        }
        lblGanador.setText(ganador.getNombre() + " ganó con " +
                ganador.getCartaSeleccionada().getValor() + " " +
                ganador.getCartaSeleccionada().getEmoji() + "!");

        // Retrasar el cierre
        Timer cerrarTimer = new Timer(3000, e -> dispose());
        cerrarTimer.setRepeats(false);
        cerrarTimer.start();
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
}