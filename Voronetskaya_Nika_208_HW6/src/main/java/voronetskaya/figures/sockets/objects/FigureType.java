package voronetskaya.figures.sockets.objects;

import java.util.NoSuchElementException;

public enum FigureType {
    J {
        public Figure getFigure(Direction direction) {
            Figure figure = new Figure();
            Point[] points;

            switch (direction) {
                case NORMAL -> points = new Point[]{new Point(0, 1), new Point(1, 1),
                        new Point(2, 1), new Point(2, 0)};
                case ROTATE_90 -> points = new Point[]{new Point(0, 0), new Point(1, 0),
                        new Point(1, 1), new Point(1, 2)};
                case ROTATE_180 -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(1, 0), new Point(2, 0)};
                case ROTATE_270 -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(0, 2), new Point(1, 2)};
                default -> throw new NoSuchElementException("No such direction");
            }

            figure.setPoints(points);
            return figure;
        }
    },
    SMALL_L {
        public Figure getFigure(Direction direction) {
            Figure figure = new Figure();
            Point[] points;

            switch (direction) {
                case NORMAL -> points = new Point[]{new Point(0, 0), new Point(1, 0),
                        new Point(1, 1)};
                case ROTATE_90 -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(1, 0)};
                case ROTATE_180 -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(1, 1)};
                case ROTATE_270 -> points = new Point[]{new Point(0, 1), new Point(1, 0),
                        new Point(1, 1)};
                default -> throw new NoSuchElementException("No such direction");
            }

            figure.setPoints(points);
            return figure;
        }
    },
    MIDDLE_L {
        public Figure getFigure(Direction direction) {
            Figure figure = new Figure();
            Point[] points;

            switch (direction) {
                case NORMAL -> points = new Point[]{new Point(0, 0), new Point(1, 0),
                        new Point(2, 0), new Point(2, 1)};
                case ROTATE_90 -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(0, 2), new Point(1, 0)};
                case ROTATE_180 -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(1, 1), new Point(2, 1)};
                case ROTATE_270 -> points = new Point[]{new Point(0, 2), new Point(1, 0),
                        new Point(1, 1), new Point(1, 2)};
                default -> throw new NoSuchElementException("No such direction");
            }

            figure.setPoints(points);
            return figure;
        }
    },
    Z {
        public Figure getFigure(Direction direction) {
            Figure figure = new Figure();
            Point[] points;

            switch (direction) {
                case NORMAL -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(1, 1), new Point(1, 2)};
                case ROTATE_90 -> points = new Point[]{new Point(0, 1), new Point(1, 0),
                        new Point(1, 1), new Point(2, 1)};
                case ROTATE_180 -> points = new Point[]{new Point(0, 1), new Point(0, 2),
                        new Point(1, 0), new Point(1, 1)};
                case ROTATE_270 -> points = new Point[]{new Point(0, 0), new Point(1, 0),
                        new Point(1, 1), new Point(2, 1)};
                default -> throw new NoSuchElementException("No such direction");
            }

            figure.setPoints(points);
            return figure;
        }
    },
    BIG_L {
        public Figure getFigure(Direction direction) {
            Figure figure = new Figure();
            Point[] points;

            switch (direction) {
                case NORMAL -> points = new Point[]{new Point(0, 0), new Point(1, 0),
                        new Point(2, 0), new Point(2, 1), new Point(2, 2)};
                case ROTATE_90 -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(0, 2), new Point(1, 0), new Point(2, 0)};
                case ROTATE_180 -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(0, 2), new Point(1, 2), new Point(2, 2)};
                case ROTATE_270 -> points = new Point[]{new Point(0, 2), new Point(1, 2),
                        new Point(2, 0), new Point(2, 1), new Point(2, 2)};
                default -> throw new NoSuchElementException("No such direction");
            }

            figure.setPoints(points);
            return figure;
        }
    },
    T {
        public Figure getFigure(Direction direction) {
            Figure figure = new Figure();
            Point[] points;

            switch (direction) {
                case NORMAL -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(0, 2), new Point(1, 1)};
                case ROTATE_90 -> points = new Point[]{new Point(0, 1), new Point(1, 0),
                        new Point(1, 1), new Point(2, 1)};
                case ROTATE_180 -> points = new Point[]{new Point(0, 1), new Point(1, 0),
                        new Point(1, 1), new Point(1, 2)};
                case ROTATE_270 -> points = new Point[]{new Point(0, 0), new Point(1, 0),
                        new Point(1, 1), new Point(2, 0)};
                default -> throw new NoSuchElementException("No such direction");
            }

            figure.setPoints(points);
            return figure;
        }
    },
    BIG_T {
        public Figure getFigure(Direction direction) {
            Figure figure = new Figure();
            Point[] points;

            switch (direction) {
                case NORMAL -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(0, 2), new Point(1, 1), new Point(2, 1)};
                case ROTATE_90 -> points = new Point[]{new Point(0, 2), new Point(1, 0),
                        new Point(1, 1), new Point(1, 2), new Point(2, 2)};
                case ROTATE_180 -> points = new Point[]{new Point(0, 1), new Point(1, 1),
                        new Point(2, 0), new Point(2, 1), new Point(2, 2)};
                case ROTATE_270 -> points = new Point[]{new Point(0, 0), new Point(1, 0),
                        new Point(1, 1), new Point(1, 2), new Point(2, 0)};
                default -> throw new NoSuchElementException("No such direction");
            }

            figure.setPoints(points);
            return figure;
        }
    },
    LINE {
        public Figure getFigure(Direction direction) {
            Figure figure = new Figure();
            Point[] points;

            switch (direction) {
                case NORMAL -> points = new Point[]{new Point(0, 0), new Point(0, 1),
                        new Point(0, 2)};
                case ROTATE_90 -> points = new Point[]{new Point(0, 0), new Point(1, 0),
                        new Point(2, 0)};
                case ROTATE_180 -> points = new Point[]{new Point(0, 0)};
                default -> throw new NoSuchElementException("No such direction");
            }

            figure.setPoints(points);
            return figure;
        }
    };

    /**
     * @param direction - a degree of rotation of the figure.
     * @return a figure of a certain type.
     */
    public abstract Figure getFigure(Direction direction);
}
