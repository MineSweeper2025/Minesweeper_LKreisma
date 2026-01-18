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
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Field extends HelloController {

    private int i = 1;

    private final Stage stage = new Stage();
    private final GridPane gamegrid = new GridPane();
    private AnchorPane root = new AnchorPane();
    Scene scene = new Scene(root);




    private Label mineCounterLabel = new Label("-Mines-");
    private int mineCounter;
    private int CellsToOpened; //the amount of cells without the bombs -> for the end of the game
    private boolean isrecursiveCall = false;


    private Button reset = new Button("-RESET-");

    private final String difficulty;
    private final Label winnerLabel = new Label("You won!");

    private int seconds = 0;


    public String getSeconds() {

        if(this.seconds > 9){
            if(this.seconds > 99){
                return String.valueOf(this.seconds);
            }else{
                return "0" + this.seconds;
            }
        }else{
            return "00" + this.seconds;
        }
    }

    private Label timeLabel = new Label();

    private Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                oneSecondPassed();
                timeLabel.setText(String.valueOf(getSeconds()));
            }));



    public Field(ChoiceBox difficulty) {
        gamegrid.setId("gamegrid");
        root.setId("root");
        root.setLayoutX(gamegrid.getPrefWidth());

        mineCounterLabel.setId("mineCounterLabel");
        timeLabel.setId("timeLabel");
        reset.setId("resetButton");
        winnerLabel.setId("winnerLabel");

        winnerLabel.setVisible(false);

        scene.getStylesheets().add(Objects.requireNonNull(Field.class.getResource("stylesheets/Field.css")).toExternalForm());


        this.difficulty = difficulty.getValue().toString();

        reset.setOnAction(event -> {
            try {
                resetGame(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });



        mineCounterLabel.setText("Mines: " + mineCounter);


        timeLabel.setText(String.valueOf(getSeconds()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        reset.setLayoutX(10);
        reset.setLayoutY(10);

        timeLabel.setLayoutX(130);
        timeLabel.setLayoutY(15);

        mineCounterLabel.setLayoutX(250);
        mineCounterLabel.setLayoutY(15);


        AnchorPane.setRightAnchor(gamegrid, 0.0);
        AnchorPane.setLeftAnchor(gamegrid, 0.0);
        AnchorPane.setBottomAnchor(gamegrid, 0.0);
        AnchorPane.setTopAnchor(gamegrid, 70.0);




    }



    public void createGameField(ActionEvent actionevent) {
        ((Stage) ((Node) actionevent.getSource()).getScene().getWindow()).close();
        //System.out.println(this.difficulty);

        stage.setScene(scene);
        root.getChildren().addAll(reset, timeLabel, mineCounterLabel, winnerLabel);
        stage.setResizable(false);


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
        mineCounter = mines;
        mineCounterLabel.setText("Mines: " + mineCounter);
        CellsToOpened = rows * column - mines;



        gamegrid.setLayoutY(70);
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            Node[] nodes = new Node[column];
            for (int colIndex = 0; colIndex < column; colIndex++) {
                Button node = new Button(" ");
                node.setId("cell");
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

        System.out.println(i + " - " + ROW + " : " + COL);

        if (grid.getChildren().removeIf(node ->
                (GridPane.getRowIndex(node) == ROW) && (GridPane.getColumnIndex(node) == COL && !"bomb".equals(node.getId()))
        )) {
            ++this.i;
            // 3. Add the new button
            Button button = new Button(" ");
            button.setPrefSize(40, 40);
            button.setId("bomb");
            button.setOnMouseClicked(this::markButton);
            grid.add(button, COL, ROW);
        } else {
            setBomb(grid);
        }


    }


    public void markButton(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            if ("bomb".equals(((Button) event.getSource()).getId())) {
                System.out.println("BOOM! Game Over!");
                // Disable all buttons

                timeline.stop();
                for (Node node : gamegrid.getChildren()) {
                    if("bomb".equals(node.getId())){
                        ((Button) node).setText("💣");
                    }
                    node.setDisable(true); //Disables every button after bomb was clicked
                }
                return;
            } else {

                System.out.println("Opens Cell markButton(): " + CellsToOpened);
                isrecursiveCall = false;
                openCell(((Button) event.getSource()));
            }
            ((Button) event.getSource()).setDisable(true);
        } else if (event.getButton() == MouseButton.SECONDARY) {//Setting the Flags
            if ("🚩".equals(((Button) event.getSource()).getText())) {
                ((Button) event.getSource()).setText("");
                ++mineCounter;
                ((Button) event.getSource()).getStyleClass().add("cell");

            } else {
                ((Button) event.getSource()).setText("🚩"); // ((Button) event.getSource()) = the button that was clicked
                --mineCounter;
                ((Button) event.getSource()).getStyleClass().add("flagged");
            }
            mineCounterLabel.setText("Mines: " + mineCounter);
        }

    }


    public void openCell(Button eventbutton) {


        if (CellsToOpened == 0) {
            timeline.stop();
            winnerLabel.setVisible(true);
            System.out.println("Congratulations! You've cleared the minefield!");

            for (Node node : gamegrid.getChildren()) {
                if("bomb".equals(node.getId())){
                    ((Button) node).setText("💣");
                }

                node.setDisable(true);//Disables every button after winning

            }
            return;
        }

        Button button = eventbutton;
        for (Node node : gamegrid.getChildren()) {
            if ((GridPane.getRowIndex(node).equals(GridPane.getRowIndex(button))) &&
                    (GridPane.getColumnIndex(node).equals(GridPane.getColumnIndex(button)))) {
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

                    if (!isrecursiveCall) { // to avoid opening a cell twice in a recursive call
                        --CellsToOpened;
                        System.out.println("1Opens Cell openCell(): " + CellsToOpened + "--" + GridPane.getRowIndex(button) + "*" + GridPane.getColumnIndex(button));
                    }
                    isrecursiveCall = true;
                    if (CellsToOpened == 0) {
                        openCell(button);

                    }
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
                                        --CellsToOpened;
                                        System.out.println("2Opens Cell openCell(): " + CellsToOpened + "--" + GridPane.getRowIndex(adjacentNode) + "*" + GridPane.getColumnIndex(adjacentNode));
                                        isrecursiveCall = true;
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

    public void oneSecondPassed(){
        this.seconds++;
    }

}
