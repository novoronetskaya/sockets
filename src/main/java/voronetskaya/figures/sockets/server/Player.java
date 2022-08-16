package voronetskaya.figures.sockets.server;

import voronetskaya.figures.sockets.objects.Figure;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player implements Runnable {
    private volatile AtomicBoolean isRunning;
    private final List<Figure> figures = new ArrayList<>();
    private final String name;
    private final Executor executor;
    private int seconds;
    private int moves;

    private final BufferedReader reader;
    private final ObjectOutputStream writer;

    /**
     * Creates a new Player instance.
     *
     * @param player    socket for player
     * @param isRunning flag if server is running
     * @throws IOException if has problems while communicating with a client
     */
    public Player(Socket player, AtomicBoolean isRunning, Executor executor) throws IOException {
        this.isRunning = isRunning;
        this.reader = new BufferedReader(new InputStreamReader(player.getInputStream()));
        this.writer = new ObjectOutputStream(player.getOutputStream());
        this.executor = executor;

        name = reader.readLine();
    }

    public void run() {
        try {
            while (isRunning.get()) {
                String clientString = reader.readLine();

                if (clientString.equals("Results")) {
                    moves = Integer.parseInt(reader.readLine());
                    seconds = Integer.parseInt(reader.readLine());
                    executor.incrementFinishedGamePlayers();
                    clientString = reader.readLine();
                }

                if (clientString.equals("Exit")) {
                    executor.removePlayer(this);
                    break;
                }

                if (clientString.equals("Restart")) {
                    executor.askForReset();
                    break;
                }


                if (figures.isEmpty()) {
                    executor.sendNewFigure();
                }

                writer.writeObject(figures.get(0));
                figures.remove(0);
            }
        } catch (IOException ex) {
            System.out.println("Was not able to communicate with player" + name);
            System.out.println("Details: " + ex.getMessage());
            executor.removePlayer(this);
        }
    }

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public void sendMessage(Object message) throws IOException {
        writer.writeObject(message);
    }

    public void clearFigures() {
        figures.clear();
    }

    public String getName() {
        return name;
    }

    public int getMoves() {
        return moves;
    }

    public int getSeconds() {
        return seconds;
    }

    public void closeStreams() {
        try {
            reader.close();
            writer.close();
        } catch (IOException ex) {
            System.out.println("Could not close streams for player" + name);
            System.out.println("Details: " + ex.getMessage());
        }
    }
}
