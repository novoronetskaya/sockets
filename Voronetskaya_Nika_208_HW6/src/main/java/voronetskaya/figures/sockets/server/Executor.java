package voronetskaya.figures.sockets.server;

import voronetskaya.figures.sockets.objects.Figure;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Executor implements Runnable {
    private volatile AtomicBoolean isRunning;
    private volatile AtomicInteger readyPlayers = new AtomicInteger(0);
    private final int numOfPlayers;
    private volatile AtomicInteger curNumOfPlayers = new AtomicInteger(0);
    private volatile AtomicInteger finishedGamePlayers = new AtomicInteger(0);
    private final List<Player> players = new ArrayList<>();
    private final ServerSocket server;
    private final int seconds;

    /**
     * Creates a new instance of the class.
     *
     * @param server       socket on which server is running
     * @param numOfPlayers number of players on the server
     * @param isRunning    flag that signals that server is still running
     * @param seconds      max length of the game
     */
    public Executor(ServerSocket server, int numOfPlayers, AtomicBoolean isRunning, int seconds) {
        this.server = server;
        this.numOfPlayers = numOfPlayers;
        this.isRunning = isRunning;
        this.seconds = seconds;
    }

    public void run() {
        try {
            while (isRunning.get()) {
                if (finishedGamePlayers.get() == numOfPlayers && numOfPlayers > 1) {
                    declareWinner();
                    finishedGamePlayers.set(0);
                } else if (curNumOfPlayers.get() == numOfPlayers && readyPlayers.get() == 0) {
                    continue;
                }

                while (curNumOfPlayers.get() < numOfPlayers) {
                    Player player = new Player(server.accept(), isRunning, this);
                    player.sendMessage(seconds);
                    player.sendMessage(numOfPlayers);
                    players.add(player);

                    curNumOfPlayers.incrementAndGet();
                    readyPlayers.incrementAndGet();
                }

                if (readyPlayers.get() != numOfPlayers) {
                    continue;
                }
                readyPlayers.set(0);


                if (numOfPlayers > 1) {
                    players.get(0).sendMessage(players.get(1).getName());
                    players.get(1).sendMessage(players.get(0).getName());
                }

                for (var player : players) {
                    player.clearFigures();
                }

                for (var player : players) {
                    Thread playerThread = new Thread(player);
                    playerThread.start();
                }
            }

            for (var player : players) {
                player.sendMessage("Exit");
                player.closeStreams();
            }
        } catch (IOException ex) {
            System.out.println("Got problems while accepting player or sending messages to him.");
            System.out.println("Details: " + ex.getMessage());
        }
    }

    private void declareWinner() throws IOException {
        int firstMoves = players.get(0).getMoves();
        int secondMoves = players.get(1).getMoves();

        if (firstMoves > secondMoves) {
            players.get(0).sendMessage("Win");
            players.get(1).sendMessage("Loss");
        } else if (firstMoves == secondMoves) {
            int firstSeconds = players.get(0).getSeconds();
            int secondSeconds = players.get(1).getSeconds();

            if (firstSeconds > secondSeconds) {
                players.get(1).sendMessage("Win");
                players.get(0).sendMessage("Loss");
            } else if (firstSeconds == secondSeconds) {
                players.get(0).sendMessage("Win");
                players.get(1).sendMessage("Win");
            } else {
                players.get(0).sendMessage("Win");
                players.get(1).sendMessage("Loss");
            }
        } else {
            players.get(1).sendMessage("Win");
            players.get(2).sendMessage("Loss");
        }

        finishedGamePlayers.set(0);
    }

    public synchronized void sendNewFigure() {
        Figure newFigure = Figure.generate();
        for (var player : players) {
            player.addFigure(newFigure);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        curNumOfPlayers.decrementAndGet();
    }

    /**
     * Signals that there are users who finished the game.
     */
    public void incrementFinishedGamePlayers() {
        finishedGamePlayers.incrementAndGet();
    }

    /**
     * Signals that there are players who wait for a new game to begin.
     */
    public void askForReset() {
        readyPlayers.incrementAndGet();
    }
}
