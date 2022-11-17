package ScrumFaster;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.channels.Pipe;
import java.util.*;

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

public class ScrumboardController {
    @FXML
    private Button AddNewUserStoryButton;
    @FXML
    private Button AddNewTeamMateButton;
    @FXML
    private Button SaveBoardButton;
    @FXML
    private Button LoadBoardButton;
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

    @FXML
    private Button viewBurndownButton;

    // we could use the BigDecimal class because it gives the user complete control
    // over rounding behaviour.
    BigDecimal progress = new BigDecimal(String.format("%.2f", 0.0)); // this is a big decimal constructor, where we
    // could pass in a format string.
    // format the string to be %.2f, and the arguments will be the initial value we
    // will begin with
    // which is set t0 zero.
    // another variable will be use to calulate the percent of work done over the
    // ones that are not complete.

    // ArrayList of all users added to the system.
    public static ArrayList<User> teammates = new ArrayList<User>();

    // List of stories for each section displayed in the board
    ArrayList<UserStory> backlog = new ArrayList<UserStory>();
    ArrayList<UserStory> toDo = new ArrayList<UserStory>();
    ArrayList<UserStory> inProgress = new ArrayList<UserStory>();
    ArrayList<UserStory> done = new ArrayList<UserStory>();

    // List of all users in the system
    private static HBox UsersHBox = new HBox();

    // number of sprints completed
    private static int sprintNumber = 0;

    // total points of all stories
    private static int totalPoints = 0;

    // total points of of stories in the done section
    private static int totalPointsCompleted = 0;

    /**
     * Displays the passed user object on the board as an icon and adds the name to the combo box
     * @param name name of the teammate to display
     * @param colour colour of the teammate to display
     */
    public void displayTeammate(String name, String colour) {
        // add the name to the combo box
        assignToComboBox.getItems().add(name);

        // add user to scrum board by creating icon
        VBox IconNameCombo = new VBox();
        IconNameCombo.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial Bold"));
        Circle icon = new Circle(20.0);
        icon.setFill(Color.valueOf(colour));

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

        // add to list of users
        ScrumboardController.teammates.add(newUser);

        // display new user on the board
        displayTeammate(name, colour.toString());
    }


    /**
     * Create a new user story: obtain all the information filled out by a user,
     * create a new userStory object that will get populated with that info.
     * Save that new userStory object to the stories array.
     * @Precond: all fields must be populated
     * @Postcond: stories array is modified
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

        // add user story points to total points
        totalPoints += newStory.getPriority();

        // scale finalActual points to match new total
        for (int i = 0; i < finalActual.size(); i++) {
            finalActual.set(i, finalActual.get(i) + newStory.getPriority());
        }

        // add new story to the correct list
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
                totalPointsCompleted += newStory.getPriority();
            }
            default -> {
                // if not specified, add to backlog
                backlog.add(newStory);
            }
        }

        // clear all fields in the new user story form
        personaField.clear();
        featureNameField.clear();
        descriptionField.clear();
        assignToComboBox.valueProperty().set(null);
        statusComboBox.valueProperty().set(null);
        priorityComboBox.valueProperty().set(null);

        // redraw the scrum board
        updateBoard();
    }

    /**
     * Updates the scrum board
     */
    public void updateBoard() {

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
     **/
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
    }

    /**
     * Action listener for button that saves the board, saves the board to a file
     *
     * @throws IOException if fxml file not found
     */
    public void saveBoard() throws IOException {
        Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
        map.put("teammates", teammates);
        map.put("backlog", backlog);
        map.put("toDo", toDo);
        map.put("inProgress", inProgress);
        map.put("done", done);

        // allow a user to choose a file by themselves
        Stage stageSave = new Stage();
        FileChooser fileChooser = new FileChooser();
        // user can only save .dat files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Dat files", "*.dat")
        );
        fileChooser.setTitle("Save the board file");
        File saveFile = fileChooser.showSaveDialog(stageSave);

        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(saveFile.getAbsolutePath()));
            output.writeObject(map);
            output.close();
        } catch (IOException ioe) {
            System.out.println("error writing to file");
        }
    }

    /**
     * Action listener for button that loads a save file and updates the board.
     *
     * @throws IOException if fxml file not found
     */
    public void loadBoard() throws IOException {
        Map<String, ArrayList<?>> map = null;

        // allow a user to choose a file by themselves
        Stage stageLoad = new Stage();
        FileChooser fileChooser = new FileChooser();
        // user can only choose from .dat files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Dat files", "*.dat")
        );
        fileChooser.setTitle("Choose the board to load");
        File openedFile = fileChooser.showOpenDialog(stageLoad);
        // TODO: there probably should be an initial directory to navigate from for convenience
        //fileChooser.setInitialDirectory(new File("data"));

        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(openedFile.getAbsolutePath()));
            map = (Map<String, ArrayList<?>>) input.readObject();
        }
        catch (IOException ioe) {
            System.out.println("error reading from file");
        }
        catch (ClassNotFoundException cnfe) {
            System.out.println("class not found");
        }

        // check if the map read from the file is null or not
        if (map != null) {
            // set main arrays to info read from the file
            teammates = (ArrayList<User>) map.get("teammates");
            backlog = (ArrayList<UserStory>) map.get("backlog");
            toDo = (ArrayList<UserStory>) map.get("toDo");
            inProgress = (ArrayList<UserStory>) map.get("inProgress");
            done = (ArrayList<UserStory>) map.get("done");

            // update the board to display the info read from the file regarding all the user stories
            updateBoard();

            // For cases when user clicks 'Load Board' more than once,
            // clear the teammates HBoxand combo box before adding to them to avoid duplicates
            UsersHBox.getChildren().clear();
            assignToComboBox.getItems().clear();

            // update the teammates section to display info read from the file regarding all the teammates
            for (User teammate : teammates) {
                displayTeammate(teammate.getName(), teammate.getColour());
            }
        } else {
            System.out.println("Info was not read from the file");
        }
    }

    // we could use the BigDecimal class because it gives the user complete control
    // over rounding behaviour.
    //BigDecimal progress = new BigDecimal(String.format("%.2f", 0.0)); // this is a big decimal constructor, where we
                                                                      // could pass in a format string.
    // format the string to be %.2f, and the arguments will be the initial value we
    // will begin with
    // which is set t0 zero.
    // another variable will be use to calulate the percent of work done over the
    // ones that are not complete.

    public void updateProgress() {

        // find total number of user stories in every section
        Double total_stories = Double.valueOf(backlog.size() + toDo.size() + inProgress.size() + done.size());

        // ratio of stories that are done to total number of stories
        progress = new BigDecimal(String.format("%.2f", done.size() / (total_stories)));
        myprogressbar.setProgress(progress.doubleValue());

        // display the progress as a percentage
        progresslabel.setText(Integer.toString((int) Math.round(progress.doubleValue() * 100)) + "%");

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        myprogressbar.setStyle("-fx-accent: blue;");
    }

    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();

    // list of points of actual team velocity
    // index will be x value, value will be y value
    ArrayList<Integer> finalActual = new ArrayList<Integer>();

    }

    /**
     * Initialize the scrumBoard progress bar and assign to user combo box
     * @param url ?
     * @param resourceBundle ?
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set colour of progress bar
        myprogressbar.setStyle("-fx-accent: blue;");

        // add no user option to assign to user combo box
        assignToComboBox.getItems().add("");

    }

    /**
     * Updates board to reflect next spring when button is pressed
     * @postcondition: user stories in inProgress and to-do are moved to backlog
     * @postcondition: number of sprint updates
     */
    public void goToNextSprint() {

        ArrayList<UserStory> incompleteStories = new ArrayList<>();

        // move all incomplete stories to the backlog
        incompleteStories.addAll(toDo);
        toDo.clear();
        incompleteStories.addAll(inProgress);
        inProgress.clear();

        for (UserStory task : incompleteStories) {
            task.setStatus("Backlog");
            backlog.add(task);
            updateBoard();
        }

        sprintNumber++; // keep count of how many sprints we have finished

        // update the chart with the new sprint
        if (finalActual.size() < sprintNumber) {
            // add y value at x index if it does not exist
            finalActual.add(totalPoints - totalPointsCompleted);
        } else {
            // otherwise, overwrite the value at the index
            finalActual.set(sprintNumber - 1, totalPoints - totalPointsCompleted);
        }
    }

    /*
     * This function is called when the user clicks on the "View Burndown" button.
     * It will open a new window with the burndown chart depicting team velocity.
     */
    public void BurndownChartWindow() throws IOException {

        // draw the burndown chart
        LineChart<Number, Number> burndownChart = new LineChart<Number, Number>(xAxis, yAxis);
        burndownChart.setTitle("Burndown Chart");

        xAxis.setLabel("Sprints Completed");
        yAxis.setLabel("Remaining Effort");

        // map ideal burndown to chart
        final XYChart.Series ideal = new XYChart.Series();
        final XYChart.Series actual = new XYChart.Series();

        ideal.setName("Ideal Burndown");
        actual.setName("Actual Burndown");

        actual.getData().add(new XYChart.Data(0, totalPoints));
        for (int i = 0; i < finalActual.size(); i++) {
            // plot the effort remaining for each sprint prior to the current one
            actual.getData().add(new XYChart.Data(i + 1, finalActual.get(i)));
        }

        // points will correspond to the number of sprints needed to complete the story
        // calculate number of sprints needed to complete entire projet based on average story points
        Double averagePoints = Double.valueOf(totalPoints)
                / (backlog.size() + toDo.size() + inProgress.size() + done.size());
        Double decrement = totalPoints / averagePoints;

        // map ideal burndown to chart
        for (int i = 0; i <= decrement; i++) {
            if (totalPoints - i * averagePoints >= 0) {
                ideal.getData().add(new XYChart.Data(i, totalPoints - i * averagePoints));
            } else {
                ideal.getData().add(new XYChart.Data(i, 0));
                break;
            }
        }

        Scene scene = new Scene(burndownChart, 800, 600);
        burndownChart.getData().addAll(ideal, actual);

        Stage stage = new Stage();
        stage.setTitle("Burndown Chart");
        stage.setHeight(450);
        stage.setWidth(450);
        stage.setScene(scene);
        stage.showAndWait();
          */
    }
}
