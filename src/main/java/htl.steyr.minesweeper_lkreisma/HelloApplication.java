package htl.steyr.minesweeper_lkreisma;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(HelloApplication.class.getResource("./stylesheets/Settings.css").toExternalForm());
        stage.setTitle("Minesweeper-Settings");

        Image image = new Image("htl/steyr/minesweeper_lkreisma/icons/Logo_Minesweeper.png");
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }
}
