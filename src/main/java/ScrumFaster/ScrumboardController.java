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
import java.util.ArrayList;
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

    // ArrayList of all users added to the system.
    public static ArrayList<User> teammates;

    // ArrayList of all user stories added to the system.
    public static ArrayList<UserStory> stories;

    // Class constructor.
    public ScrumboardController() {
        teammates = new ArrayList<User>();
        stories = new ArrayList<UserStory>();
    }

    public void UserStoryWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateUserStory.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 320, 240);
            Stage stage = new Stage();
            stage.setTitle("Create New User Story");
            stage.setHeight(450);
            stage.setWidth(450);
            stage.setScene(scene);
            stage.showAndWait();

    }

    /**
     * Action listener for button that adds new team mate, opens up a window for new team mate creation
     * note: may want to make this just a scene change on same window as lucas suggested
     * @throws IOException if fxml file not found
     */
    public void newTeamMateWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newTeamMate.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 320, 240);
        Stage stage = new Stage();
        stage.setTitle("Add New Teammate");
        stage.setHeight(450);
        stage.setWidth(450);
        stage.setScene(scene);
        stage.showAndWait();

    }

    /**
     * Action listener for button that saves the board, saves the board to a file
     * @throws IOException if fxml file not found
     */
    public void saveBoard() throws IOException {
    }
}
