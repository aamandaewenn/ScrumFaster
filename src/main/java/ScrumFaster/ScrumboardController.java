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
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Collections;
import java.util.IllegalFormatWidthException;

import static java.util.Collections.list;
import static java.util.Collections.sort;

///////////////////////////importing files for the progress bar///////////////////////////
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ScrumboardController implements Initializable {
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

    @FXML
    private Label progresslabel;

    @FXML
    private ProgressBar myprogressbar;

    @FXML
    private Button nextSprintButton;

    // ArrayList of all users added to the system.
    public static ArrayList<User> teammates = new ArrayList<User>();

    // Priority queues for each section displayed in the board
    ArrayList<UserStory> backlog = new ArrayList<UserStory>();
    ArrayList<UserStory> toDo = new ArrayList<UserStory>();
    ArrayList<UserStory> inProgress = new ArrayList<UserStory>();
    ArrayList<UserStory> done = new ArrayList<UserStory>();

    private static HBox UsersHBox = new HBox();

    // the number of sprints we have finished
    private static int sprintNumber = 0;

    // public void UserStoryWindow() throws IOException {
    // FXMLLoader fxmlLoader = new
    // FXMLLoader(getClass().getResource("CreateUserStory.fxml"));
    // Parent root = fxmlLoader.load();
    // Scene scene = new Scene(root, 320, 240);
    // Stage stage = new Stage();
    // stage.setTitle("Create New User Story");
    // stage.setHeight(450);
    // stage.setWidth(450);
    // stage.setScene(scene);
    // stage.showAndWait();
    //
    // }

    // private User addUser(String name, String colour) {
    // User user = new User(name, colour);
    // teammates.add(user);
    // return user;
    // }

    /**
     * Creates a new User object and adds the user icon to the scrum board
     */
    public void newTeamMate() {

        // get values from board
        String name = UsersTextBox.getText();
        Paint colour = UsersColourPicker.getValue();

        // if name is not entered, create team mate called "TeamMate #"
        if (name.equals("")) {
            int TeammateNumber = ScrumboardController.teammates.size() + 1;
            name = "TeamMate " + TeammateNumber;
        }

        // if colour is white don't create a new teammate and inform user
        if (colour.toString().equals("0xffffffff")) {
            Popup WhitePopup = new Popup("Colour cannot be set to white");
            WhitePopup.displayPopup();
            return;
        }

        // if colour is already chosen for another teammate, do not create teammate and
        // inform user
        for (User teamMate : ScrumboardController.teammates) {
            if (teamMate.getColour().equals(colour.toString())) {
                Popup DuplicatePopup = new Popup("Cannot have same colour as another user");
                DuplicatePopup.displayPopup();
                return;
            }
        }

        // create user object
        User newUser = new User(name, colour.toString());

        // add to list of users and to combo box
        ScrumboardController.teammates.add(newUser);
        assignToComboBox.getItems().add(name);

        // add user to scrum board by creating icon
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

        // add icon's vbox to the board
        UsersScrollPane.setContent(UsersHBox);
    }


    /*
     * Create a new user story: obtain all the information filled out by a user,
     * create a new userStory object that will get populated with that info.
     * Save that new userStory object to the stories array.
     * Precond: all fields must be populated
     * Postcond: stories array is modified
     * Displays a popUp when user does not provide all the info required
     */
    public void addUserStory() {
        String persona = personaField.getText();
        String featureName = featureNameField.getText();
        String description = descriptionField.getText();

        String userName = assignToComboBox.getValue();
        String status = statusComboBox.getValue();
        String priority = priorityComboBox.getValue();

        if (persona.equals("") || featureName.equals("") || description.equals("") || status == null
                || priority == null) {
            Popup unfilledPopup = new Popup("Please fill in all required fields");
            unfilledPopup.displayPopup();
            return;
        }

        // do not allow two user stories with same name to be created
        ArrayList<ArrayList<UserStory>> storyLists = new ArrayList<ArrayList<UserStory>>();
        storyLists.add(backlog);
        storyLists.add(toDo);
        storyLists.add(inProgress);
        storyLists.add(done);
        for (ArrayList<UserStory> list : storyLists) {
            for (UserStory story : list) {
                if (story.getTitle().equals(featureName)) {
                    Popup RepeatName = new Popup("Two user stories cannot have the same name. Please try again");
                    RepeatName.displayPopup();
                    return;
                }
            }
        }

        // search existing users to find the user object that matches the name selected
        // in the combo box
        User user = null;
        for (User u : teammates) {
            if (u.getName().equals(userName)) {
                user = u;
            }
        }

        // create new user story object
        UserStory newStory;

        if (user == null) {
            // create when no user chosen
            newStory = new UserStory(persona, featureName, description, status, Integer.parseInt(priority));
        } else {
            // create when user is chosen
            newStory = new UserStory(persona, featureName, description, user, status, Integer.parseInt(priority));

            // add new user story to the user's assigned stories
            user.addUserStory(newStory);
        }

        switch (status) {
            case "Backlog" -> {
                backlog.add(newStory);
            }
            case "To-do" -> {
                toDo.add(newStory);
            }
            case "In progress" -> {
                inProgress.add(newStory);
            }
            case "Done" -> {
                done.add(newStory);
            }
            default -> {
                // if not specified, add to backlog
                backlog.add(newStory);
            }
        }
        // redraw the scrum board
        updateBoard(newStory);
    }

    /**
     * Updates the scrum board by adding the new user story to the correct section
     * 
     * @param newStory the new user story to be added to the scrum board
     */
    public void updateBoard(UserStory newStory) {

        // create cases so that whole board updates
        String[] cases = { "Backlog", "To-do", "In progress", "Done" };
        VBox boxToUpdate;
        ArrayList<UserStory> listToIterate;
        ScrollPane paneToUpdate;

        for (int j = 0; j <= 3; j++) {
            // choose which section of board is updating this iteration
            String whichSection = cases[j];

            // determine which section of the scrum board to update
            switch (whichSection) {
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

            // reset the VBox to be updated
            boxToUpdate.getChildren().clear();

            // sort the list of user stories based on their
            // priority with 5 being the highest priority
            sort(listToIterate);

            // redraw the board by adding all the user stories in sorted order
            for (int i = listToIterate.size() - 1; i >= 0; i--) {
                // iterate through the list of user stories in reverse order
                // so that the highest priority is at the top
                VBox newStoryBox = new VBox();
                Pane colorpane = new Pane();
                Pane blankSpacePane = new Pane();
                HBox storyname = new HBox();

                newStoryBox.setMaxWidth(258);

                TilePane seemore = new TilePane();

                // put coloured bar on user story
                String colour = listToIterate.get(i).getColour();
                Rectangle colourRec = new Rectangle();
                colourRec.setHeight(15);
                colourRec.setWidth(400);

                Color fillcolour = Color.web(colour);
                colourRec.setFill(fillcolour);

                // Blank white rectangle for spacing between tasks
                Rectangle blankRec = new Rectangle();
                blankRec.setHeight(25);
                blankRec.setWidth(400);

                Color blankWhiteColour = Color.web("transparent");
                blankRec.setFill(blankWhiteColour);

                colorpane.getChildren().add(colourRec);
                newStoryBox.getChildren().add(colorpane);

                // add name to user story
                Label nameLabel = new Label(listToIterate.get(i).getTitle());
                nameLabel.setMaxWidth(210);
                nameLabel.setWrapText(true);

                nameLabel.setFont(Font.font("Arial Bold"));
                storyname.getChildren().add(nameLabel);

                Label priorityLabel = new Label("  " + listToIterate.get(i).getPriority());

                storyname.getChildren().add(priorityLabel);
                newStoryBox.getChildren().add(storyname);
                newStoryBox.getChildren().add(blankRec);

                boxToUpdate.getChildren().add(newStoryBox);

            }

            paneToUpdate.setContent(boxToUpdate);

            updateProgress();
        }
    }

    /**
     * Add statuses and priorities to combo box as well as a null/non-assigned
     * option for assignee combo box
     */
    public void setStatusPriority() {
        String statuses[] = { "Backlog", "To-do", "In progress", "Done" };
        if (statusComboBox.getItems().isEmpty()) {
            System.out.println("this executes");
            for (String status : statuses) {
                statusComboBox.getItems().add(status);
            }
        }

        if (priorityComboBox.getItems().isEmpty()) {
            for (int i = 1; i <= 5; i++) {
                priorityComboBox.getItems().add("" + i);
            }
        }

        // TODO Figure out how to make this execute exactly once even if box is not
        // empty since user can be added first
        // right now it only executes if a drop down menu is selected before a user is
        // added
        if (assignToComboBox.getItems().isEmpty()) {
            assignToComboBox.getItems().add("");
        }
    }

    /**
     * Action listener for button that saves the board, saves the board to a file
     * 
     * @throws IOException if fxml file not found
     */
    public void saveBoard() throws IOException {
    }

    // we could use the BigDecimal class because it gives the user complete control
    // over rounding behaviour.
    BigDecimal progress = new BigDecimal(String.format("%.2f", 0.0)); // this is a big decimal constructor, where we
                                                                      // could pass in a format string.
    // format the string to be %.2f, and the arguments will be the initial value we
    // will begin with
    // which is set t0 zero.
    // another variable will be use to calulate the percent of work done over the
    // ones that are not complete.

    public void updateProgress() {

        if (progress.doubleValue() < 1) {
            // find total number of user stories in every section
            Double total_stories = Double.valueOf(backlog.size() + toDo.size() + inProgress.size() + done.size());
            
            // ratio of stories that are done to total number of stories
            progress = new BigDecimal(String.format("%.2f", done.size()/(total_stories)));
            myprogressbar.setProgress(progress.doubleValue());

            // display the progress as a percentage
            progresslabel.setText(Integer.toString((int) Math.round(progress.doubleValue() * 100)) + "%");
        }

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        myprogressbar.setStyle("-fx-accent: blue;");

    }

    public void goToNextSprint() {

        ArrayList<UserStory> incompleteStories = new ArrayList<>();
        
        // move all incomplete stories to the backlog
        incompleteStories.addAll(toDo); toDo.clear();
        incompleteStories.addAll(inProgress); inProgress.clear();

        for (UserStory task : incompleteStories) {
            task.setStatus("Backlog");
            backlog.add(task);
            updateBoard(task);
        }

        // add together points from done
        int pointsCompleted = 0;
        for (UserStory completedStory : done) {
            pointsCompleted = pointsCompleted + completedStory.getPriority();
        }
        System.out.println(pointsCompleted);

        // keep count of how many sprints we have finished
        sprintNumber++;

        // pass the points and sprint count to function to make graph
        // TODO write classes and functions to make burndown chart
    }

}
