package htl.steyr.minesweeper_lkreisma;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Field extends HelloController{

    private Stage stage = new Stage();
    private GridPane gamegrid;
    Button reset = new Button();

    private String difficulty;

    public Field(ChoiceBox difficulty) {
        this.difficulty= difficulty.getValue().toString();
    }

    public void initialize(){
        reset.setOnAction(event->resetGame());
    }

    public void createGameField(ActionEvent actionevent){
        ((Stage) ((Node) actionevent.getSource()).getScene().getWindow()).close();

        AnchorPane root = new AnchorPane();

        switch(this.difficulty){
            case "Anfänger" -> root.getChildren().add(createGrid(8,8,10));
            case null -> root.getChildren().add(createGrid(8,8,10));
            case "Fortgeschritten" -> root.getChildren().add(createGrid(16,16,40));
            case "Profi" -> root.getChildren().add(createGrid(30,16,99));
            default -> throw new IllegalStateException("Unexpected value: " + this.difficulty);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);


        stage.show();

    }


    protected GridPane createGrid(int rows, int column, int mines){


        gamegrid = new GridPane();
        for(int rowIndex = 0; rowIndex < rows ; rowIndex++) {
            //an array to hold buttons of one row
            Node[] nodes = new Node[column];
            for(int colIndex = 0; colIndex < column ; colIndex++) {
                Button node= new Button(rowIndex+""+colIndex);
                node.setOnAction(e->buttonsaidsomething(node)); //add action listener
                nodes[colIndex]= node;
            }
            gamegrid.addRow(rowIndex, nodes);
        }
        return gamegrid;
    }

    public void buttonsaidsomething(Button button){
        System.out.println(GridPane.getRowIndex(button)+" : "+ GridPane.getColumnIndex(button));
        if(GridPane.getColumnIndex(button).equals(5) && GridPane.getRowIndex(button).equals(2)){
            System.out.println("THERE WAS A BOMB!!!!!!");
        }

    }

    public void resetGame(){

    }
}
