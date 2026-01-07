package htl.steyr.minesweeper_lkreisma;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;


public class HelloController {


    public ChoiceBox<String> difficultyChoiceBox;


    public ChoiceBox<String> getChosenDifficulty() {
        return difficultyChoiceBox;
    }

    public void initialize() {
        ObservableList<String> difficulties = FXCollections.observableArrayList("Anfänger", "Forteschrittener", "Profi");
        difficultyChoiceBox.setItems(difficulties);
    }

    @FXML
    protected void StartingGameClicked(ActionEvent actionEvent) {
        GamefieldController.newGameField(actionEvent, difficultyChoiceBox);
    }

    public void QuitGame(ActionEvent actionEvent) {
        Platform.exit();
    }
}
