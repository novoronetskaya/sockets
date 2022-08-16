package voronetskaya.figures.sockets.client;

import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.Scanner;

public class Utils {
    private static final Random RANDOM = new Random();
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Displays an Alert window.
     *
     * @param message - text that should be placed on the displayed window.
     */
    public static void showMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }

    /**
     * Converts given number of seconds to text.
     *
     * @param seconds - number of seconds to convert.
     * @return the text representation of the number of seconds.
     * @throws IllegalArgumentException - thrown if the number of seconds if negative.
     */
    public static String secondsToString(int seconds) throws IllegalArgumentException {
        if (seconds < 0) {
            throw new IllegalArgumentException("The value cannot be negative");
        }

        int hours = seconds / 60 / 60;
        int minutes = seconds / 60 % 60;
        int curSeconds = seconds % 60;

        String result;
        if (hours < 10) {
            result = String.format("0%d", hours);
        } else {
            result = String.format("%d", hours);
        }

        if (minutes < 10) {
            result = String.format("%s:0%d", result, minutes);
        } else {
            result = String.format("%s:%d", result, minutes);
        }

        if (curSeconds < 10) {
            result = String.format("%s:0%d", result, curSeconds);
        } else {
            result = String.format("%s:%d", result, curSeconds);
        }

        return result;
    }

    public static Color getRandomColor() {
        return Color.rgb(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
    }

    public static String readString() {
        return SCANNER.nextLine();
    }
}
