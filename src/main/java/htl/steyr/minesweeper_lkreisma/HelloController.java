package htl.steyr.minesweeper_lkreisma;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HelloController {


    @FXML
    protected void StartingGameClicked() {
        Stage stage = new Stage();
        AnchorPane playroot = new AnchorPane();
        Scene playscene = new Scene(playroot, 500, 500);


        //making the gridpane
        playroot.getChildren().add(createGrid(10));



        stage.setScene(playscene);
        stage.show();

    }


    //method for creating a new GridPane

    public GridPane createGrid(int rowcolumn){
        GridPane gridpane = new GridPane();

        for(int b=0; b<rowcolumn;++b){
            gridpane.addRow(b);

            for(int i=0; i<rowcolumn; ++i){
                Button button = new Button(i+":"+b);
                gridpane.addColumn(i, button);
            }
        }

        return gridpane;
    }
}
