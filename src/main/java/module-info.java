module htl.steyr.minesweeper_lkreisma {
    requires javafx.controls;
    requires javafx.fxml;


    opens htl.steyr.minesweeper_lkreisma to javafx.fxml;
    exports htl.steyr.minesweeper_lkreisma;
}