package htl.steyr.minesweeper_lkreisma;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class SettingsController {

    public AnchorPane settingsAnchorpane;
    public Button quitgame;
    public Button newGameButton;
    public Label minesweeper_Label;

    @FXML
    private ChoiceBox<String> difficultyChoiceBox;

    public ChoiceBox<String> getDifficultyChoiceBox(){
        return this.difficultyChoiceBox;
    }

    public void initialize(){
        minesweeper_Label.getStyleClass().add("Minesweeper-Label-style");
        quitgame.getStyleClass().add("button-style");
        newGameButton.getStyleClass().add("button-style");


        settingsAnchorpane.setId("settings-anchorpane");
        getDifficultyChoiceBox().getStyleClass().add("choice-box-styled");
        getDifficultyChoiceBox().setValue("Anfänger");
       getDifficultyChoiceBox().getItems().addAll("Anfänger","Fortgeschritten","Profi");
    }
    public void createnewGame(ActionEvent actionEvent) {
        Field field = new Field(difficultyChoiceBox);
        field.createGameField(actionEvent);
    }


    public void quitGame(ActionEvent actionEvent) {
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }
}
