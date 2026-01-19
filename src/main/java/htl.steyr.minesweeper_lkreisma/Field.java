package htl.steyr.minesweeper_lkreisma;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Field extends HelloController {


    //variables for creating the Game Field
    private final Stage stage = new Stage();
    private final GridPane gamegrid = new GridPane();   //the grid where all Cells (with or without bombs) are placed
    private final AnchorPane root = new AnchorPane();     //Gridpane is placed on this Anchorpane
    Scene scene = new Scene(root);
    private final StackPane overlay;
    private final HBox header;


    private final Label mineCounterLabel = new Label("-Mines-");  //shows the User the amount of bombs that are left
    private int mineCounter;    //for the mineCounterLabel

    private int CellsToOpened; //the amount of cells without the bombs -> for the end of the game
    private boolean isrecursiveCall = false;    //for checking if the player has revealed every cell without a bomb (winner-function)


    private final Button reset = new Button("-RESET-");   //for resetting the game (you get back to the settings-Window)

    private final String difficulty;
    private final Label winnerLabel = new Label("You won!");    //gets Visible when the player wins the game

    private int seconds = 0;   //Value for the timer
    private final Label timeLabel = new Label();  //sits on the top of the Anchorpane

    // Timeline for the timer that updates every second
    private final Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), _ -> {
                oneSecondPassed();
                timeLabel.setText(String.valueOf(getSeconds()));
            }));


    /**
     * Constructor for every Value/Object that needs to be initialized + setting ids for CSS
     *
     * @param difficulty - ChoiceBox from the HelloController (to get the chosen difficulty)
     */
    public Field(ChoiceBox difficulty) {


        stage.setTitle("Minesweeper-Game");

        Image image = new Image("htl/steyr/minesweeper_lkreisma/icons/Logo_Minesweeper.png");
        stage.getIcons().add(image);
        gamegrid.setId("gamegrid");
        root.setId("root");


        mineCounterLabel.setId("mineCounterLabel");
        timeLabel.setId("timeLabel");
        reset.setId("resetButton");

        mineCounterLabel.getStyleClass().add("header-label");
        timeLabel.getStyleClass().add("header-label");
        reset.getStyleClass().add("header-button");

        winnerLabel.setId("winnerLabel");
        winnerLabel.setVisible(true);

        overlay = new StackPane(winnerLabel);
        overlay.setPickOnBounds(false);

        AnchorPane.setTopAnchor(overlay, 0.0);
        AnchorPane.setBottomAnchor(overlay, 0.0);
        AnchorPane.setLeftAnchor(overlay, 0.0);
        AnchorPane.setRightAnchor(overlay, 0.0);

        header = new HBox(mineCounterLabel, reset, timeLabel);
        header.setId("header");
        header.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(header, 2.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);


        AnchorPane.setRightAnchor(gamegrid, 0.0);
        AnchorPane.setLeftAnchor(gamegrid, 0.0);
        AnchorPane.setBottomAnchor(gamegrid, 0.0);
        AnchorPane.setTopAnchor(gamegrid, 70.0);


        this.difficulty = difficulty.getValue().toString();

        // ActionEvent for the reset-Button
        reset.setOnAction(event -> {
            try {
                resetGame(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //adding the CSS stylesheet to the scene
        scene.getStylesheets().add(Objects.requireNonNull(Field.class.getResource("stylesheets/Field.css")).toExternalForm());


        mineCounterLabel.setText("Mines: " + mineCounter);


        timeLabel.setText(String.valueOf(getSeconds()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }


    /**
     * @description:
     *      first thing (after the constructor) that gets called from the HelloController.createnewGame()-method
     *      the stage and scene get set
     *      the gamegrid gets created depending on the difficulty chosen
     *      stage.show() - shows  the game-window
     *
     * @param actionevent   - for closing the settings-window
     */

    public void createGameField(ActionEvent actionevent) {
        ((Stage) ((Node) actionevent.getSource()).getScene().getWindow()).close();
        stage.setResizable(false);


        stage.setScene(scene);
        root.getChildren().addAll(header, overlay);
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

    /**
     * @param actionevent - for getting the source of the event (Window from reset-button)
     * @throws IOException - if something goes wrong with loading the HelloApplication
     */
    public void resetGame(ActionEvent actionevent) throws IOException {
        ((Stage) ((Node) actionevent.getSource()).getScene().getWindow()).close();

        HelloApplication application = new HelloApplication();
        application.start(new Stage());
    }


    /**
     * @description:
     *
     * gets called in the method createGameField - gets all values for parameters from there
     * @param rows  - number of rows
     * @param column - number of columns
     * @param mines - number of mines
     * @return  - the created GridPane (gamegrid) with buttons will be returned
     */

    protected GridPane createGrid(int rows, int column, int mines) {
        mineCounter = mines;
        mineCounterLabel.setText("Mines: " + mineCounter);
        CellsToOpened = rows * column - mines;  // calculating the number of cells without bombs (for winning the game)

        gamegrid.setLayoutY(70);    //positioning the grid below the header (mineCounterLabel, timeLabel, reset-button)


        // Creating the Buttons (cells) and adding them to the grid

        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            Node[] nodes = new Node[column];
            for (int colIndex = 0; colIndex < column; colIndex++) {
                Button node = new Button(" ");
                node.setId("cell");
                node.setPrefSize(45, 45);
                node.setOnMouseClicked(this::markButton);
                nodes[colIndex] = node;
            }

            gamegrid.addRow(rowIndex, nodes);
        }
        for (int i = 0; i < mines; ++i) {
            setBomb(gamegrid);
        }


        return gamegrid;    //returning the created GridPane with all buttons set
    }


    /**
     *
     * @param grid
     *
     * uses two random numbers to set a bomb on a random position in the grid
     * -> if a bomb was already placed on that position, the method calls itself again for another position
     * -> if no bomb was there, the button gets replaced with a new button with the id "bomb"
     * -> gets called in the createGrid()-method
     *
     * int ROW - chooses a random row
     * int COL - chooses a random column
     */
    public void setBomb(GridPane grid) {
        int ROW = ThreadLocalRandom.current().nextInt(0, grid.getRowCount());
        int COL = ThreadLocalRandom.current().nextInt(0, grid.getColumnCount());


        if (grid.getChildren().removeIf(node ->
                (GridPane.getRowIndex(node) == ROW) && (GridPane.getColumnIndex(node) == COL && !"bomb".equals(node.getId()))
        )) {

            // 3. Add the new button
            Button button = new Button(" ");
            button.setPrefSize(45, 45);
            button.setId("bomb");
            button.setOnMouseClicked(this::markButton);
            grid.add(button, COL, ROW);
        } else {
            setBomb(grid);
        }


    }


    /**
     * @param event - MouseEvent for checking if PRIMARY (Left) or SECONDARY (Right) button was Clicked
     */
    public void markButton(MouseEvent event) {

        if (event.getButton() == MouseButton.PRIMARY) {

            if ("bomb".equals(((Button) event.getSource()).getId())) { //if a bomb was clicked every button gets disabled - game over
                overlay.toFront();
                winnerLabel.setText("You Lost - (" + mineCounter + " Mines left)");
                winnerLabel.setVisible(true);
                timeline.stop();

                // Disable all buttons
                for (Node node : gamegrid.getChildren()) {

                    if ("bomb".equals(node.getId())) {
                        ((Button) node).setText("💣");
                    }
                    node.setDisable(true); //Disables every button after bomb was clicked
                }
                return;


                /**
                 * opening the cell with the openCell-method
                 * (will check for adjacent bombs - if none are there it will open adjacent cells too)
                 * -> recursive method
                 */
            } else {
                ((Button) event.getSource()).getStyleClass().remove("cell-flagged");
                isrecursiveCall = false;
                openCell(((Button) event.getSource()));
            }
            ((Button) event.getSource()).setDisable(true);

        } else if (event.getButton() == MouseButton.SECONDARY) { //Setting / Removing a flag on right-click

            if ("🚩".equals(((Button) event.getSource()).getText())) {
                ((Button) event.getSource()).setText("");

                ++mineCounter; //if a flag is removed, the mineCounter gets incremented

                ((Button) event.getSource()).getStyleClass().remove("cell-flagged");

            } else {

                ((Button) event.getSource()).setText("🚩"); // ((Button) event.getSource()) = the button that was clicked

                --mineCounter; //if a flag is set, the mineCounter gets decremented

                ((Button) event.getSource()).getStyleClass().add("cell-flagged");
            }


            mineCounterLabel.setText("Mines: " + mineCounter);
        }

    }


    /**
     * @description:
     * opens a cell when clicked (left-click)
     * checks for adjacent bombs
     * if there are adjacent bombs -> shows the number of bombs
     * if there are no adjacent bombs -> opens adjacent cells too (recursive)
     * @param eventbutton - the button that was clicked (source for the eventbutton is passed from the markButton()-method)
     * @throws NullPointerException - if the value is out of bounds from the gridpane (important for the edges of the grid)
     */
    public void openCell(Button eventbutton) throws NullPointerException {

        gamewon();


        for (Node node : gamegrid.getChildren()) {
            if ((GridPane.getRowIndex(node).equals(GridPane.getRowIndex(eventbutton))) &&
                    (GridPane.getColumnIndex(node).equals(GridPane.getColumnIndex(eventbutton)))) {

                int bombCount = 0;

                // Check all adjacent cells for bombs and counts them
                for (int r = GridPane.getRowIndex(eventbutton) - 1; r <= GridPane.getRowIndex(eventbutton) + 1; r++) {
                    for (int c = GridPane.getColumnIndex(eventbutton) - 1; c <= GridPane.getColumnIndex(eventbutton) + 1; c++) {
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
                    eventbutton.setText(String.valueOf(bombCount));

                    if (!isrecursiveCall) { // to avoid opening a cell twice in a recursive call
                        --CellsToOpened;
                    }
                    isrecursiveCall = true;
                    if (CellsToOpened == 0) {
                        openCell(eventbutton);

                    }
                } else {    //if there is no adjacent bomb -> opens adjacent cells too (recursive)

                    eventbutton.setText("");

                    Integer rowObj = GridPane.getRowIndex(eventbutton);
                    Integer colObj = GridPane.getColumnIndex(eventbutton);

                    //if the row or col is null, it gets set to 0 (important for the edges of the grid)
                    //prevents NullPointerException
                    int row = rowObj == null ? 0 : rowObj;
                    int col = colObj == null ? 0 : colObj;

                    for (int r = row - 1; r <= row + 1; ++r) {
                        for (int c = col - 1; c <= col + 1; ++c) {
                            if (r >= 0 && r < gamegrid.getRowCount() && c >= 0 && c < gamegrid.getColumnCount()) { //checks if it's within the grid or out of bounds (very important)
                                for (Node adjacentNode : gamegrid.getChildren()) {      //goes throug every Node (Button) in the gamegrid

                                    if (GridPane.getRowIndex(adjacentNode) != null && GridPane.getColumnIndex(adjacentNode) != null &&
                                            GridPane.getRowIndex(adjacentNode) == r && GridPane.getColumnIndex(adjacentNode) == c
                                            && !adjacentNode.isDisable()  //checks if the button is already opened
                                            && !"bomb".equals(adjacentNode.getId())) {
                                        adjacentNode.setDisable(true); // markieren bevor rekursiv aufgerufen wird
                                        --CellsToOpened;
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

    /**
     * @description:
     * if the player has opened every cell without a bomb, he wins the game
     *
     * -> timeline stops,
     * -> winnerLabel gets visible,
     * -> every bomb is shown
     * -> every button gets disabled
     */
    public void gamewon() {
        if (CellsToOpened == 0) {
            overlay.toFront();
            timeline.stop();
            winnerLabel.setText("You Won - (" + getSeconds() + "s)");
            winnerLabel.setVisible(true);
            System.out.println("Congratulations! You've cleared the minefield!");

            for (Node node : gamegrid.getChildren()) {
                if ("bomb".equals(node.getId())) {
                    ((Button) node).setText("💣");
                }

                node.setDisable(true);//Disables every button after winning

            }

        }
    }


    public void oneSecondPassed() {
        this.seconds++;
    }   //increments the seconds with the timeline-keyframe-function

    /**
     * @description:
     * @return - the seconds in a formatted way (with 3 digits -> 001, 011, 111,... 999) -> timeLabel shows it like this
     */
    public String getSeconds() {

        if (this.seconds > 9) {
            if (this.seconds > 99) {
                return String.valueOf(this.seconds);
            } else {
                return "0" + this.seconds;
            }
        } else {
            return "00" + this.seconds;
        }
    }


}
