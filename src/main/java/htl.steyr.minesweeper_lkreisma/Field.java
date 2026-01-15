package htl.steyr.minesweeper_lkreisma;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.concurrent.ThreadLocalRandom;

public class Field extends HelloController {

    private Stage stage = new Stage();
    private GridPane gamegrid;
    Button reset = new Button("-RESET-");

    private String difficulty;

    public Field(ChoiceBox difficulty) {
        this.difficulty = difficulty.getValue().toString();
        reset.setOnAction(e -> {
            ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
        });
    }

    public void createGameField(ActionEvent actionevent) {
        ((Stage) ((Node) actionevent.getSource()).getScene().getWindow()).close();

        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        root.getChildren().add(reset);


        switch (this.difficulty) {
            case "Anfänger" -> root.getChildren().add(createGrid(8, 8, 10));
            case null -> root.getChildren().add(createGrid(8, 8, 10));
            case "Fortgeschritten" -> root.getChildren().add(createGrid(16, 16, 40));
            case "Profi" -> root.getChildren().add(createGrid(30, 16, 99));
            default -> throw new IllegalStateException("Unexpected value: " + this.difficulty);
        }


        stage.show();

    }


    protected GridPane createGrid(int rows, int column, int mines) {


        gamegrid = new GridPane();
        gamegrid.setLayoutY(70);
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            Node[] nodes = new Node[column];
            for (int colIndex = 0; colIndex < column; colIndex++) {
                Button node = new Button(rowIndex + "" + colIndex);
                node.setPrefSize(40, 40);
                node.setOnMouseClicked(this::markButton);
                nodes[colIndex] = node;
            }

            gamegrid.addRow(rowIndex, nodes);
        }
        for (int i = 0; i < mines; ++i) {
            setBomb(gamegrid);
        }
        return gamegrid;
    }

    public void setBomb(GridPane grid) {
        int ROW = ThreadLocalRandom.current().nextInt(0, grid.getRowCount());
        int COL = ThreadLocalRandom.current().nextInt(0, grid.getColumnCount());

        System.out.println(ROW + " : " + COL);

        if (grid.getChildren().removeIf(node ->
                (GridPane.getRowIndex(node) == ROW) && (GridPane.getColumnIndex(node) == COL && !"bomb".equals(node.getId()))
        )) {

            // 3. Add the new button
            Button button = new Button("bomb");
            button.setPrefSize(40, 40);
            button.setId("bomb");
            button.setOnMouseClicked(this::markButton);
            grid.add(button, COL, ROW);
        } else {
            setBomb(grid);
        }


    }


    public void markButton(MouseEvent event) {
        if( event.getButton() == MouseButton.PRIMARY) {
            if("bomb".equals(((Button) event.getSource()).getId())) {
                System.out.println("BOOM! Game Over!");
                // Disable all buttons
                for (Node node : gamegrid.getChildren()) {          //Disables every button after bomb was clicked
                    node.setDisable(true);
                }
                return;
            }
            ((Button) event.getSource()).setDisable(true);
        } else if( event.getButton() == MouseButton.SECONDARY) {    //Setting the Flags
            ((Button) event.getSource()).setText("F"); // ((Button) event.getSource()) = the button that was clicked
        }

    }


    public void openCell(){

    }
}
