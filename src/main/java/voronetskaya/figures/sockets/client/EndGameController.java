package voronetskaya.figures.sockets.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import voronetskaya.figures.sockets.objects.GameField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;

public class EndGameController {
    @FXML
    private Label gameResultLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Label timeLabel;

    @FXML
    private Label movesLabel;

    @FXML
    private Label fullnessDegreeLabel;

    private Stage stage;

    private static PrintStream printStream;
    private static ObjectInputStream readerStream;

    private Socket socket;

    /**
     * Modifies the end-of-game window depending on the results of the game.
     *
     * @param field - field of the current game storing all the necessary information.
     */
    public void setGameResult(GameField field) {
        URL url;

        // Choosing the picture depending on whether the user has won.
        if (field.getPlayersNum() == 1) {
            gameResultLabel.setText("У Вас не было соперника, но от этого Ваша игра не стала менее великолепной!");
            url = EndGameController.class.getResource("one_player.jpg");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ожидание завершения игры соперником...");
            alert.show();
            printStream.println("Results");
            printStream.println(field.getMoves());
            printStream.println(field.getSeconds());
            String result;

            try {
                result = (String)readerStream.readObject();
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Невозможно получить результаты из-за внутренней ошибки.");
                errorAlert.showAndWait();
                System.out.println("Details: " + e.getMessage());
                return;
            }

            alert.close();
            if (result.equals("Win")) {
                gameResultLabel.setText("Поздравляем! Вы выиграли.");
                url = EndGameController.class.getResource("win_picture.jpg");
            } else {
                gameResultLabel.setText("Не расстраивайтесь! В следующий раз Вы обязательно выиграете.");
                url = EndGameController.class.getResource("lose_picture.jpg");
            }
        }

        // If everything is alright with the picture file, displays it for the user.
        if (url != null) {
            Image picture = new Image(url.toString());
            imageView.setImage(picture);
        }

        movesLabel.setText(String.valueOf(field.getMoves()));
        timeLabel.setText(Utils.secondsToString(field.getSeconds()));
        fullnessDegreeLabel.setText(String.format("%d%%", field.getFullnessDegree()));
    }

    public void setStage(Stage stage, Socket socket, PrintStream stream, ObjectInputStream reader) {
        this.stage = stage;
        this.socket = socket;
        printStream = stream;
        readerStream = reader;
    }

    /**
     * Restarts the game.
     */
    public void handleRestartButtonClick() {
        Jigsaw jigsaw = new Jigsaw();

        try {
            printStream.println("Restart");
            jigsaw.start(new Stage());
            stage.close();
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Невозможно перезапустить игру, выход.");
            errorAlert.showAndWait();
            System.out.println("Details: " + e.getMessage());

            printStream.println("Exit");

            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error while closing socket.");
                System.out.println("Details: " + ex.getMessage());
            }
        }
    }

    /**
     * Ends the game and closes the window.
     */
    public void handleExitButtonClick() {
        printStream.println("Exit");
        stage.close();
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error while closing socket.");
            System.out.println("Details: " + e.getMessage());
        }
    }
}
