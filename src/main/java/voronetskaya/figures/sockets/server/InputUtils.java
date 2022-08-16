package voronetskaya.figures.sockets.server;

import java.util.Scanner;

public class InputUtils {
    private final static Scanner SCANNER = new Scanner(System.in);

    /**
     * Checks if a string consists only of digits
     *
     * @param input a string to check
     * @return true if a string consists only of digits and false otherwise
     */
    private static boolean isStringDigit(String input) {
        if (input.length() == 0) {
            return false;
        }
        for (var symbol : input.toCharArray()) {
            if (!Character.isDigit(symbol)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tries to turn input into a number
     *
     * @param leftBorder minimal value for a string
     * @param input      a string to try to get a number from
     * @return a value if input was valid and leftBorder - 1 otherwise
     */
    private static int tryGetValidNumber(int leftBorder, String input) {
        if (!isStringDigit(input)) {
            return leftBorder - 1;
        }
        int result;
        try {
            result = Integer.parseInt(input);
        } catch (Exception ex) {
            return leftBorder - 1;
        }
        return result;
    }

    /**
     * Demands user to enter a number until the input is valid
     *
     * @param leftBorder  a minimal number that may be entered by a user
     * @param rightBorder a maximal number that may be entered by a user
     * @param message     a message that describes the data that should be entered
     * @return a valid number entered by a user
     */
    public static int getNumber(int leftBorder, int rightBorder, String message) {
        System.out.print(message);
        int result;
        String input = SCANNER.nextLine();
        result = tryGetValidNumber(leftBorder, input);
        while (result < leftBorder || result > rightBorder) {
            System.out.print("Incorrect data has been entered. Please, try again: ");
            input = SCANNER.nextLine();
            result = tryGetValidNumber(leftBorder, input);
        }
        return result;
    }
}
