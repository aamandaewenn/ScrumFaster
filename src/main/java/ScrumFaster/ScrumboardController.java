package ScrumFaster;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ScrumboardController {
    @FXML
    private Button AddNewUserStoryButton;
    @FXML
    private Button AddNewTeamMateButton;
    @FXML
    private Button SaveBoardButton;
    @FXML
    private Button DisplayStatisticsButton;

    private void helloWorld(){
        System.out.println("You pressed the button!");
    }

    public void UserStoryWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateUserStory.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 320, 240);
            Stage stage = new Stage();
            stage.setTitle("Create New User Story");
            stage.setHeight(450);
            stage.setWidth(450);
            //stage.setX(500);
            //stage.setY(500);
            stage.setScene(scene);
            stage.showAndWait();

    }
}
