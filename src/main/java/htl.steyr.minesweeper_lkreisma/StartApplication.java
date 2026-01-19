package htl.steyr.minesweeper_lkreisma;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, NullPointerException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(StartApplication.class.getResource("./stylesheets/Settings.css")).toExternalForm());
        stage.setTitle("Minesweeper-Settings");

        Image image = new Image("htl/steyr/minesweeper_lkreisma/icons/Logo_Minesweeper.png");
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
