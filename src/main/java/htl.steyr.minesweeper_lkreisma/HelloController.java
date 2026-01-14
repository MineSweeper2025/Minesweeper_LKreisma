package htl.steyr.minesweeper_lkreisma;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;


public class HelloController{

    @FXML
    private ChoiceBox<String> difficultyChoiceBox;

    public ChoiceBox<String> getDifficultyChoiceBox(){
        return this.difficultyChoiceBox;
    }

    public void initialize(){
       getDifficultyChoiceBox().getItems().addAll("Anfänger","Fortgeschritten","Profi");
    }
    public void createGameStart(ActionEvent actionEvent) {
        Field field = new Field(difficultyChoiceBox);
        field.createGameField(actionEvent);
    }


}
