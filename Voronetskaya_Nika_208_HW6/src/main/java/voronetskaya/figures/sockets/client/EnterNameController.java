package voronetskaya.figures.sockets.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EnterNameController {
    private String name;

    private Stage stage;

    @FXML
    private TextField nameField;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void readyButtonClick() {
        if (nameField.getText().isBlank()) {
            Utils.showMessage("Имя не может быть пустым!", Alert.AlertType.ERROR);
        } else {
            name = nameField.getText();
            stage.close();
        }
    }

    public String getName() {
        return name;
    }
}
