package ScrumFaster;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**

 Cheat Sheet (info that might be useful when coding)

 Steps to display an fxml file:
 load file to a Scene object -> apply Scene object to a Stage object -> use the .show method on the stage object to
 display the Stage on the screen.

 // How to load fxml files to a scene so that they can be displayed.
 Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXML FILE NAME HERE.fxml")));
 Scene scene = new Scene(root, 320, 240);


 */

public class ScrumBoardUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ScrumBoardUI.fxml")));
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Hello!");
        stage.setHeight(687);
        stage.setWidth(1329);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }





}