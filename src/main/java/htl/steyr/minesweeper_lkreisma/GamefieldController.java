package htl.steyr.minesweeper_lkreisma;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.concurrent.ThreadLocalRandom;

public class GamefieldController {





    public static void newGameField(ActionEvent actionEvent, ChoiceBox<String> difficulty) {
        System.out.println(difficulty.getValue());
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();

        Stage stage = new Stage();
        AnchorPane playroot = new AnchorPane();
        Scene playscene = new Scene(playroot);

        //Start-Window will be closed
        stage.setScene(playscene);


        //making the gridpane
        switch (difficulty.getValue()) {
            case "Anfänger" -> playroot.getChildren().add(createGrid(8, 8, 10));
            case null -> playroot.getChildren().add(createGrid(8, 8, 10));
            case "Forteschrittener" -> playroot.getChildren().add(createGrid(16, 16, 40));
            case "Profi" -> playroot.getChildren().add(createGrid(16, 30, 99));
            default -> throw new IllegalStateException("Unexpected value: " + difficulty.getValue());
        }


        stage.show();


    }


    //method for creating a new GridPane
    public static GridPane createGrid(int row, int column, int bombs) {
        GridPane gridpane = new GridPane();
        for (int b = 0; b < row; ++b) {
            gridpane.addRow(b);

            for (int i = 0; i < column; ++i) {
                int randomnumber = ThreadLocalRandom.current().nextInt(bombs);
                System.out.println(randomnumber);
                Button button = new Button(i + ":" + b);
                //button.setBackground(null);
                button.setPrefSize(50, 50);
                gridpane.addColumn(i, button);
            }
        }


        while(bombs > 0){
            System.out.println(gridpane.getChildren().get(2));

        }

        gridpane.setBackground(null);

        return gridpane;
    }



}
