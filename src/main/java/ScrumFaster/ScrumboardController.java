package ScrumFaster;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Collections;

import static java.util.Collections.list;
import static java.util.Collections.sort;

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


    @FXML
    private TextField UsersTextBox;

    @FXML
    private ColorPicker UsersColourPicker;

    @FXML
    private ScrollPane UsersScrollPane;

    @FXML
    private ScrollPane BacklogScrollPane;
    @FXML
    private ScrollPane ToDoScrollPane;
    @FXML
    private ScrollPane InProgressScrollPane;
    @FXML
    private ScrollPane DoneScrollPane;

    @FXML
    private VBox backlogVbox;
    @FXML
    private VBox toDoVbox;
    @FXML
    private VBox inProgressVbox;
    @FXML
    private VBox doneVbox;



    // ArrayList of all users added to the system.
    public static ArrayList<User> teammates = new ArrayList<User>();

    // Priority queues for each section displayed in the board
    ArrayList<UserStory> backlog = new ArrayList<UserStory>();
    ArrayList<UserStory> toDo = new ArrayList<UserStory>();
    ArrayList<UserStory> inProgress = new ArrayList<UserStory>();
    ArrayList<UserStory> done = new ArrayList<UserStory>();

    private static HBox UsersHBox = new HBox();

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
     * Creates a new User object and adds the user icon to the scrum board
     *
     */
    public void newTeamMate() {

        //create new User object
        String name = UsersTextBox.getText();
        Paint colour = UsersColourPicker.getValue();
            if (name.equals("")) {
                int TeammateNumber = ScrumboardController.teammates.size() + 1;
                name = "TeamMate " + TeammateNumber;
            }

        //TODO add error checking and handling (ie no name entered, colour is white)

        User newUser = new User(name, colour.toString());

        //add to list of users and to combo box
        ScrumboardController.teammates.add(newUser);
        assignToComboBox.getItems().add(name);

        // add user to scrum board
        VBox IconNameCombo = new VBox();
        IconNameCombo.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial Bold"));
        Circle icon = new Circle(20.0);
        icon.setFill(colour);

        // set padding for icon and name so that they're spread out in the VBox
        icon.setTranslateX(20);
        icon.setTranslateY(10);
        nameLabel.setTranslateX(20);
        nameLabel.setTranslateY(10);

        // add icon and name to VBox
        IconNameCombo.getChildren().addAll(icon, nameLabel);
        UsersHBox.getChildren().add(IconNameCombo);

        UsersScrollPane.setContent(UsersHBox);
    }

    /*
        Create a new user story: obtain all the information filled out by a user,
        create a new userStory object that will get populated with that info.
        Save that new userStory object to the stories array.
           Precond: all fields must be populated
           Postcond: stories array is modified
           Displays a popUp when user does not provide all the info required
     */
    public void addUserStory() {
        String persona = personaField.getText();
        String featureName = featureNameField.getText();
        String description = descriptionField.getText();

        String userName = assignToComboBox.getValue();
        String status = statusComboBox.getValue();
        String priority = priorityComboBox.getValue();

        if(persona.equals("") || featureName.equals("") || description.equals("") || status == null || priority == null ) {
            // TODO: implement displayPopup() method for error handling
            // displayPopup();
            System.out.println("Error - not all fields are provided");
            return;
        }

        // search existing users to find the user object that matches the name selected in the combo box
        User user = null;
        for (User u : teammates) {
            if (u.getName().equals(userName)) {
                user = u;
            }
        }
        
        // create new user story object
        UserStory newStory = new UserStory(persona, featureName, description, user, status, Integer.parseInt(priority));
        
        // add new user story to the user's assigned stories
        user.addUserStory(newStory);

        switch (status) {
            case "Backlog" -> {
                backlog.add(newStory);
                sort(backlog);
            }
            case "To-do" -> {
                toDo.add(newStory);
                sort(toDo);
            }
            case "In progress" -> {
                inProgress.add(newStory);
                sort(inProgress);
            }
            case "Done" -> {
                done.add(newStory);
                sort(done);
            }
            default -> {
                // if not specified, add to backlog
                backlog.add(newStory);
                sort(backlog);
            }
        }

        updateBoard(newStory);
    }

    public void updateBoard(UserStory newStory) {

        VBox boxToUpdate;
        ArrayList<UserStory> listToIterate;
        ScrollPane paneToUpdate;
        
        switch (newStory.getStatus()) {
            case "Backlog" -> {
                boxToUpdate = backlogVbox;
                listToIterate = backlog;
                paneToUpdate = BacklogScrollPane;
            }
            case "To-do" -> {
                boxToUpdate = toDoVbox;
                listToIterate = toDo;
                paneToUpdate = ToDoScrollPane;
            }
            case "In progress" -> {
                boxToUpdate = inProgressVbox;
                listToIterate = inProgress;
                paneToUpdate = InProgressScrollPane;
            }
            default -> {
                boxToUpdate = doneVbox;
                listToIterate = done;
                paneToUpdate = DoneScrollPane;
            }
        }

        boxToUpdate.getChildren().clear();
        // sort the list of user stories based on their priority with 5 being the highest priority and displayed first
        sort(listToIterate);
        Collections.reverse(listToIterate);

        // redraw the board by adding all the user stories in sorted order
        for(int i = 0; i < listToIterate.size(); i++) {
            VBox newStoryBox = new VBox();
            Pane colorpane = new Pane();
            HBox storyname= new HBox();
            TilePane seemore = new TilePane();

            // put coloured bar on user story
            String colour = listToIterate.get(i).getColor();
            Rectangle colourRec = new Rectangle();
            colourRec.setHeight(25);
            colourRec.setWidth(400);

            Color fillcolour = Color.web(colour);
            colourRec.setFill(fillcolour);

            colorpane.getChildren().add(colourRec);
            newStoryBox.getChildren().add(colorpane);

            // add name to user story
            Label nameLabel = new Label(listToIterate.get(i).getTitle());
            nameLabel.setFont(Font.font("Arial Bold"));
            storyname.getChildren().add(nameLabel);

            Label priorityLabel = new Label(""+ listToIterate.get(i).getPriority());
            storyname.getChildren().add(priorityLabel);
            newStoryBox.getChildren().add(storyname);
            boxToUpdate.getChildren().add(newStoryBox);

        }

        paneToUpdate.setContent(boxToUpdate);

    }

    public void setStatusPriority() {
        String statuses[] = {"Backlog", "To-do", "In progress", "Done"};
        if (statusComboBox.getItems().isEmpty()) {
            for (String status : statuses) {
                statusComboBox.getItems().add(status);
            }
        }
        

        if (priorityComboBox.getItems().isEmpty()) {
            for (int i = 1; i <= 5; i++) {
                priorityComboBox.getItems().add(""+i);
            }
        }
    }

    /**
     * Action listener for button that saves the board, saves the board to a file
     * @throws IOException if fxml file not found
     */
    public void saveBoard() throws IOException {
    }

    public void DisplayStatistics() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("progressbar.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        Scene scene = new Scene(root, 320.0, 240.0);
        Stage stage = new Stage();
        stage.setTitle("Display Stats");
        stage.setHeight(450.0);
        stage.setWidth(450.0);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
