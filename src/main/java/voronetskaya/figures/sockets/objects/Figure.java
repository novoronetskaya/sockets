package voronetskaya.figures.sockets.objects;

import java.io.Serializable;
import java.util.Random;

public class Figure implements Serializable {
    private static final Random RANDOM = new Random();
    private static final int FIGURES_NUM = 31;
    private static final int DIRECTIONS_NUM = 4;

    private Point[] points;

    public void setPoints(Point[] points) {
        this.points = points;
    }

    public Point[] getPoints() {
        return points;
    }

    /**
     * @return a figure of random type
     */
    public static Figure generate() {
        int number = RANDOM.nextInt(FIGURES_NUM);
        FigureType type = FigureType.values()[number / DIRECTIONS_NUM];
        return type.getFigure(Direction.values()[number % DIRECTIONS_NUM]);
    }
}
