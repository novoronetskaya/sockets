package voronetskaya.figures.sockets.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private static final Scanner SCANNER = new Scanner(System.in);
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public static void main(String[] args) {
        ServerSocket socket;
        try {
            socket = new ServerSocket(5000);
        } catch (IOException e) {
            System.out.println("Was not able to create a server socket.");
            return;
        }

        new Server().execute(socket);
    }

    /**
     * Maintains the work of the server.
     * @param socket socket for server
     */
    private void execute(ServerSocket socket) {
        int numberOfPlayers = InputUtils.getNumber(1, 2, "Введите число игроков (1 или 2): ");
        int seconds = InputUtils.getNumber(1, 7200, "Введите длительность игры в секундах (от 1 до 7200): ");
        Executor executor = new Executor(socket, numberOfPlayers, isRunning, seconds);
        Thread acceptor = new Thread(executor);
        acceptor.start();

        while (true) {
            if (SCANNER.nextLine().equalsIgnoreCase("exit")) {
                break;
            }
        }

        System.out.println("Shutting the server down.");
        isRunning.set(false);

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Got exception while closing a socket");
        }
    }
}
