package htl.steyr.minesweeper_lkreisma;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Field extends HelloController {

    private final Stage stage = new Stage();
    private final GridPane gamegrid = new GridPane();

    Button reset = new Button("-RESET-");

    private final String difficulty;

    Time time = new Time();
    private Label timeLabel = new Label("000");

    private Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                time.oneSecondPassed();
                timeLabel.setText(time.getSeconds());
            }));

    public Field(ChoiceBox difficulty) {
        this.difficulty = difficulty.getValue().toString();
        reset.setOnAction(event -> {
            try {
                resetGame(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        timeLabel.setText(String.valueOf(time));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void createGameField(ActionEvent actionevent) {
        ((Stage) ((Node) actionevent.getSource()).getScene().getWindow()).close();

        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        root.getChildren().add(reset);
        root.getChildren().add(timeLabel);


        switch (this.difficulty) {
            case "Anfänger" -> root.getChildren().add(createGrid(8, 8, 10));
            case null -> root.getChildren().add(createGrid(8, 8, 10));
            case "Fortgeschritten" -> root.getChildren().add(createGrid(16, 16, 40));
            case "Profi" -> root.getChildren().add(createGrid(16, 30, 99));
            default -> throw new IllegalStateException("Unexpected value: " + this.difficulty);
        }


        stage.show();

    }

    public void resetGame(ActionEvent actionevent) throws IOException {
        ((Stage) ((Node) actionevent.getSource()).getScene().getWindow()).close();

        HelloApplication application = new HelloApplication();
        application.start(new Stage());
    }


    protected GridPane createGrid(int rows, int column, int mines) {


        gamegrid.setLayoutY(70);
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            Node[] nodes = new Node[column];
            for (int colIndex = 0; colIndex < column; colIndex++) {
                Button node = new Button("[  ]");
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
            Button button = new Button("[  ]");
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
            }else{
                openCell(((Button) event.getSource()));
            }
            ((Button) event.getSource()).setDisable(true);
        } else if( event.getButton() == MouseButton.SECONDARY) {    //Setting the Flags
            ((Button) event.getSource()).setText("F"); // ((Button) event.getSource()) = the button that was clicked
        }

    }


    public void openCell(Button eventbutton){
        Button button = eventbutton;
        for(Node node : gamegrid.getChildren()) {
            if ((GridPane.getRowIndex(node) == GridPane.getRowIndex(button)) &&
                    (GridPane.getColumnIndex(node) == GridPane.getColumnIndex(button))) {
                int bombCount = 0;
                // Check all adjacent cells
                for (int r = GridPane.getRowIndex(button) - 1; r <= GridPane.getRowIndex(button) + 1; r++) {
                    for (int c = GridPane.getColumnIndex(button) - 1; c <= GridPane.getColumnIndex(button) + 1; c++) {
                        if (r >= 0 && r < gamegrid.getRowCount() && c >= 0 && c < gamegrid.getColumnCount()) {
                            for (Node adjacentNode : gamegrid.getChildren()) {
                                if ((GridPane.getRowIndex(adjacentNode) == r) &&
                                        (GridPane.getColumnIndex(adjacentNode) == c) &&
                                        "bomb".equals(adjacentNode.getId())) {
                                    bombCount++;
                                }
                            }
                        }
                    }
                }
                if (bombCount > 0) {
                    button.setText(String.valueOf(bombCount));
                } else {
                    button.setText("");

                    Integer rowObj = GridPane.getRowIndex(button);
                    Integer colObj = GridPane.getColumnIndex(button);

                    int row = rowObj == null ? 0 : rowObj;
                    int col = colObj == null ? 0 : colObj;

                    for (int r = row - 1; r <= row + 1; ++r) {
                        for (int c = col - 1; c <= col + 1; ++c) {
                            if (r >= 0 && r < gamegrid.getRowCount() && c >= 0 && c < gamegrid.getColumnCount()) { //checks if it's within the grid or out of bounds (very important)
                                for (Node adjacentNode : gamegrid.getChildren()) {      //goes throug every Node (Button) in the gamegrid

                                    if (GridPane.getRowIndex(adjacentNode) != null && GridPane.getColumnIndex(adjacentNode) != null &&
                                            GridPane.getRowIndex(adjacentNode).intValue() == r && GridPane.getColumnIndex(adjacentNode).intValue() == c
                                            && !adjacentNode.isDisable()  //checks if the button is already opened
                                            && !"bomb".equals(adjacentNode.getId())) {
                                        adjacentNode.setDisable(true); // markieren bevor rekursiv aufgerufen wird
                                        openCell((Button) adjacentNode);
                                    }
                                }
                            }
                        }
                    }


                }
            }
        }
    }

}
