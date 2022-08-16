package voronetskaya.figures.sockets.objects;

public class GameField {
    private final int height;
    private final int width;
    private final boolean[][] field;
    private int moves;
    private int seconds;
    private int maxSeconds;
    private int filledCells;
    private int playersNum;
    private String playerName;

    public GameField(int width, int height) {
        this.height = height;
        this.width = width;
        moves = 0;
        filledCells = 0;
        field = new boolean[height][width];
    }

    /**
     * Tries to place a figure on the field so that it will not intersect with the other ones.
     *
     * @param figure - a figure that is being placed.
     * @return true, if the figure was placed, and false otherwise.
     */
    public boolean tryAddFigure(Figure figure, Point shift) {
        int xCoord;
        int yCoord;

        for (var point : figure.getPoints()) {
            xCoord = point.getX() + shift.getX();
            yCoord = point.getY() + shift.getY();
            if (xCoord >= height || xCoord < 0 || yCoord >= width || yCoord < 0 || field[xCoord][yCoord]) {
                return false;
            }
        }

        for (var point : figure.getPoints()) {
            xCoord = point.getX() + shift.getX();
            yCoord = point.getY() + shift.getY();
            field[xCoord][yCoord] = true;
        }

        ++moves;
        filledCells += figure.getPoints().length;

        return true;
    }

    public int getMoves() {
        return moves;
    }

    /**
     * Increases the number of passed seconds.
     *
     * @return the increased number of seconds.
     */
    public int incrementTime() {
        ++seconds;
        if (seconds > maxSeconds) {
            throw new IllegalArgumentException("Time is up!");
        }
        return seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    /**
     * @return the percentage of the filled cells.
     */
    public int getFullnessDegree() {
        return filledCells * 100 / (height * width);
    }

    public void setPlayersNum(int playersNum) {
        this.playersNum = playersNum;
    }

    public int getPlayersNum() {
        return playersNum;
    }

    public void clear() {
        filledCells = 0;
        seconds = 0;
        moves = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                field[i][j] = false;
            }
        }
    }

    public int getMaxSeconds() {
        return maxSeconds;
    }

    public void setMaxSeconds(int maxSeconds) {
        this.maxSeconds = maxSeconds;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String name) {
        playerName = name;
    }
}
