package voronetskaya.figures.sockets.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class Jigsaw extends Application {
    private static Socket socket;

    @Override
    public void start(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(Jigsaw.class.getResource("game.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), 671, 492);
        } catch (IOException ex) {
            Utils.showMessage("Не удалось запустить приложение, попробуйте снова позднее.", Alert.AlertType.ERROR);
            System.exit(-1);
        }

        GameController controller = fxmlLoader.getController();
        try {
            if (socket == null) {
                socket = new Socket("localhost", 5000);
            }
        } catch (Exception ex) {
            System.out.println("Could not create a socket.");
            System.out.println("Details: " + ex.getMessage());
            System.exit(-1);
        }

        try {
            controller.setSocketStreams(socket);
            controller.displayNewFigure();
        } catch (Exception ex) {
            System.out.println("Got problems while configuring the game field.");
            System.out.println("Details: " + ex.getMessage());
            System.exit(-1);
        }

        stage.setTitle("Jigsaw");
        stage.setScene(scene);
        stage.setResizable(false);
        controller.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
