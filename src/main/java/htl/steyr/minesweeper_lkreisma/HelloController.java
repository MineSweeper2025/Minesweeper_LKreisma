package htl.steyr.minesweeper_lkreisma;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.beans.JavaBean;


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
    public void StartingGameClicked(ActionEvent actionEvent) {
        GamefieldController.newGameField(actionEvent, getChosenDifficulty());
    }

    public void QuitGame(ActionEvent actionEvent) {
        Platform.exit();
    }
}
