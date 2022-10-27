package ScrumFaster;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    @FXML
    private TextField personaField;
    @FXML
    private TextField featureNameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private ComboBox<String> assignToComboBox;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private ComboBox<String> priorityComboBox;


    // ArrayList of all users added to the system.
    public static ArrayList<User> teammates = new ArrayList<User>();

    // ArrayList of all user stories added to the system.
    public static ArrayList<UserStory> stories = new ArrayList<UserStory>();

    // Class constructor.
    public ScrumboardController() {
        teammates = new ArrayList<User>();
        stories = new ArrayList<UserStory>();
    }

//    public void UserStoryWindow() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateUserStory.fxml"));
//            Parent root = fxmlLoader.load();
//            Scene scene = new Scene(root, 320, 240);
//            Stage stage = new Stage();
//            stage.setTitle("Create New User Story");
//            stage.setHeight(450);
//            stage.setWidth(450);
//            stage.setScene(scene);
//            stage.showAndWait();
//
//    }

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

    /*
        Create a new user story: obtain all the information filled out by a user,
        create a new userStory object that will get populated with that info.
        Save that new userStory object to the stories array.
           Precond: all fields must be populated
           Postcond: stories array is modified
           Throws an error when a user does not provide all the information
     */
    public void addUserStory() throws IOException {
        // TODO: implement the method
        String persona = personaField.getText();
        String featureName = featureNameField.getText();
        String description = descriptionField.getText();

        String assignT = assignToComboBox.getAccessibleText(); // don't know if it is the correct method
        String status = statusComboBox.getAccessibleText();
        String priority = priorityComboBox.getAccessibleText();

        // TODO: create a new userStory object and add it to the stories array

        // TODO: implement error handling
    }

    /**
     * Action listener for button that saves the board, saves the board to a file
     * @throws IOException if fxml file not found
     */
    public void saveBoard() throws IOException {
    }

    /**
     * action listener for the button, displays the statistics or progress of a userstory. a pop up window will display
     * the statistics.
     * @throws IOException if the fxml file is not found
     */

    public void DisplayStatisticsWindow() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("displaystats.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root,320,240);
        Stage stage = new Stage();
        stage.setTitle("Display Stats.");
        stage.setHeight(450);
        stage.setWidth(450);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
