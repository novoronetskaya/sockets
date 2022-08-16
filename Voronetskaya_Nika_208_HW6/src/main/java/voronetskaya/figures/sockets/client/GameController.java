package voronetskaya.figures.sockets.client;

import voronetskaya.figures.sockets.objects.GameField;
import voronetskaya.figures.sockets.objects.Figure;
import voronetskaya.figures.sockets.objects.Point;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;

public class GameController {
    private static final DataFormat FIGURE_FORMAT = new DataFormat("Figure");
    private static final int FIELD_SIZE = 9;

    @FXML
    private GridPane fieldGrid;

    @FXML
    private GridPane figureGrid;

    @FXML
    private Label timerLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label competitorNameLabel;

    @FXML
    private Label competitorLabel;

    @FXML
    private Label maxTimeLabel;

    @FXML
    private ColorPicker colorPicker;

    private Stage stage;
    private static Socket socket;

    // Represents a figure that is currently displayed.
    private Figure currentFigure;

    // A point representing the cell, which the dragging started from.
    private Point startDragPoint = new Point(0, 0);

    private static GameField field = new GameField(FIELD_SIZE, FIELD_SIZE);

    private static PrintStream printStream;
    private static ObjectInputStream reader;

    private Timeline timeline;

    private int handleTimeIncreased() {
        try {
            return field.incrementTime();
        } catch (IllegalArgumentException ex) {
            timeline.stop();
            Alert alert = new Alert(Alert.AlertType.WARNING, "Время вышло.");
            alert.show();
            alert.setOnHidden(e -> handleExitGameButtonClick());
            return 0;
        }
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000),
                e -> timerLabel.setText(Utils.secondsToString(handleTimeIncreased()))));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void setSocketStreams(Socket playerSocket) throws IOException, ClassNotFoundException {
        if (socket == null) {
            socket = playerSocket;
        }
        if (printStream == null) {
            printStream = new PrintStream(new BufferedOutputStream(playerSocket.getOutputStream()), true);
        }
        if (reader == null) {
            reader = new ObjectInputStream(playerSocket.getInputStream());

            setPlayerInfo();
        }
        setCompetitorInfo();
    }

    public void setPlayerInfo() throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(Jigsaw.class.getResource("enter-name.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 391, 140);
        } catch (IOException ex) {
            Utils.showMessage(ex.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        EnterNameController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        controller.setStage(stage);

        while (controller.getName() == null || controller.getName().isBlank()) {
            stage.showAndWait();
        }
        field.setPlayerName(controller.getName());
        printStream.println(field.getPlayerName());

        int seconds = (int) reader.readObject();
        field.setMaxSeconds(seconds);
        int players = (int) reader.readObject();
        field.setPlayersNum(players);
    }

    private void setCompetitorInfo() throws IOException, ClassNotFoundException {
        if (field.getPlayersNum() == 1) {
            competitorLabel.setText("");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.show();
        alert.setTitle("Ожидание подключения второго игрока...");
        competitorNameLabel.setText((String) reader.readObject());
        alert.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Performs actions in the beginning of the game.
     */
    private void initializeField() {
        field.clear();
        nameLabel.setText(field.getPlayerName());
        maxTimeLabel.setText(Utils.secondsToString(field.getMaxSeconds()));
        // Adding rectangles to the main field allowing them to be recoloured later on.
        for (int i = 0; i < FIELD_SIZE; ++i) {
            for (int j = 0; j < FIELD_SIZE; ++j) {
                fieldGrid.add(new Rectangle(43, 35.6, Color.TRANSPARENT), i, j);
            }
        }

        startTimer();
    }

    /**
     * Gets the next random figure and displays it on the figure GridPane.
     */
    public void displayNewFigure() throws IOException, ClassNotFoundException {
        printStream.println("New figure");
        if (currentFigure == null) {
            initializeField();
        }

        fieldGrid.setGridLinesVisible(true);
        figureGrid.getChildren().clear();

        Object obj = reader.readObject();
        if (obj instanceof String) {
            String input = (String) obj;
            if (input.equals("Exit")) {
                Utils.showMessage("Сервер прекратил работу, конец игры.", Alert.AlertType.WARNING);
                socket.close();
                stage.close();
                return;
            }
        }
        Figure figure = (Figure)obj;
        colorPicker.setValue(Utils.getRandomColor());
        for (var point : figure.getPoints()) {
            figureGrid.add(new Rectangle(35, 35, colorPicker.getValue()), point.getY(), point.getX());
        }

        currentFigure = figure;
    }

    public void handleDragDetected(MouseEvent event) {
        Dragboard db = figureGrid.startDragAndDrop(TransferMode.ANY);

        ClipboardContent content = new ClipboardContent();
        content.put(FIGURE_FORMAT, currentFigure);
        db.setContent(content);

        Node node = event.getPickResult().getIntersectedNode();
        if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null) {
            startDragPoint = new Point(GridPane.getRowIndex(node), GridPane.getColumnIndex(node));
        }

        event.consume();
    }

    public void handleDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();

        if (dragboard.hasContent(FIGURE_FORMAT)) {
            event.acceptTransferModes(TransferMode.COPY);
        }

        event.consume();
    }

    public void handleDragDropped(DragEvent event) {
        Dragboard board = event.getDragboard();

        Node node = event.getPickResult().getIntersectedNode();
        if (GridPane.getRowIndex(node) == null || GridPane.getColumnIndex(node) == null) {
            event.consume();
            return;
        }

        Point endDragPoint = new Point(GridPane.getRowIndex(node), GridPane.getColumnIndex(node));

        if (board.hasContent(FIGURE_FORMAT)) {
            Figure figure = (Figure) board.getContent(FIGURE_FORMAT);

            Point shift = new Point(endDragPoint.getX() - startDragPoint.getX(),
                    endDragPoint.getY() - startDragPoint.getY());

            if (field.tryAddFigure(figure, shift)) {
                insertFigureInField(endDragPoint);
                try {
                    displayNewFigure();
                } catch (Exception e) {
                    Utils.showMessage("Невозможно расположить новую фигуру.", Alert.AlertType.ERROR);
                    handleExitGameButtonClick();
                }
                event.setDropCompleted(true);
            }
        }

        event.consume();
    }

    /**
     * Displays the current figure on the game field.
     *
     * @param endDragPoint - point representing the cell, which a figure was dragged to.
     */
    private void insertFigureInField(Point endDragPoint) {
        int xCoord;
        int yCoord;

        for (var point : currentFigure.getPoints()) {
            xCoord = endDragPoint.getX() + point.getX() - startDragPoint.getX();
            yCoord = endDragPoint.getY() + point.getY() - startDragPoint.getY();
            ((Rectangle) (fieldGrid.getChildren().get(xCoord + yCoord * 9))).setFill(colorPicker.getValue());
        }
    }

    /**
     * Displays end-of-game window.
     */
    public void handleExitGameButtonClick() {
        timeline.stop();
        FXMLLoader fxmlLoader = new FXMLLoader(EndGameController.class.getResource("end-game.fxml"));
        Scene scene;

        try {
            scene = new Scene(fxmlLoader.load(), 464, 427);
        } catch (IOException e) {
            Utils.showMessage("Не удалось отобразить окно завершения, игра будет окончена.", Alert.AlertType.ERROR);

            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Ошибка во время закрітия сокета.");
            }

            stage.close();
            return;
        }

        EndGameController controller = fxmlLoader.getController();
        Stage endGameStage = new Stage();
        endGameStage.setTitle("Завершение игры");
        endGameStage.setScene(scene);
        endGameStage.setResizable(false);
        stage.close();
        controller.setStage(endGameStage, socket, printStream, reader);
        controller.setGameResult(field);

        endGameStage.show();
    }

    /**
     * Changes the colour of the current figure.
     */
    public void handleColorPickerClick() {
        for (var rectangle : figureGrid.getChildren()) {
            ((Rectangle) rectangle).setFill(colorPicker.getValue());
        }
    }

    /**
     * Displays the rules of the game.
     */
    public void handleRulesButtonClick() {
        String message = "Механика игры проста: Вам нужно лишь зажать фигуру и начать её перетаскивать " +
                "в подходящее место так, чтобы она не накладывалась на уже расположенные фигуры. Обращайте " +
                "внимание на клетку, с которой Вы начинаете перетаскивание и на которой заканчиваете его, " +
                "так как это тоже имеет значение. Приятной игры!";
        Utils.showMessage(message, Alert.AlertType.INFORMATION);
    }
}
