package ScrumFaster;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
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

///////////////////////////importing files for the progress bar///////////////////////////
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.*;

public class ScrumboardController implements Initializable {
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
    private Label taskupdate;

    @FXML
    private Button nextSprintButton;

    @FXML
    private Button viewBurndownButton;

    // Use BigDecimal class to control rounding behaviour, initialize to 0
    BigDecimal progress = new BigDecimal(String.format("%.2f", 0.0));

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
        if (name.equals("") || name.equals("No User Assigned")) {
            int TeammateNumber = ScrumboardController.teammates.size() + 1;
            name = "TeamMate " + TeammateNumber;
        }


        // if colour is white don't create a new teammate and inform user
        if (colour.toString().equals("0xffffffff")) {
            Popup WhitePopup = new Popup("Colour cannot be set to white");
            WhitePopup.displayPopup();
            return;
        }

        for (User teamMate : ScrumboardController.teammates) {
            // if colour is already chosen for another teammate, do not create teammate and
            // inform user
            if (teamMate.getColour().equals(colour.toString())) {
                Popup DuplicatePopup = new Popup("Cannot have same colour as another user");
                DuplicatePopup.displayPopup();
                return;
            }
            if (teamMate.getName().equals(name))
            {
                String popupMessage = "Cannot have users with same name. You could try ";
                popupMessage =  popupMessage.concat(name).concat("2 instead");
                Popup DuplicatePopup = new Popup(popupMessage);
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

        //update board so that combo box of all user stories is updated
        updateBoard();
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
                UserStory currentStory = listToIterate.get(i);
                // iterate through the list of user stories in reverse order
                // so that the highest priority is at the top
                VBox newStoryBox = new VBox();
                Pane colorpane = new Pane();
                Pane blankSpacePane = new Pane();
                HBox storyname = new HBox();
                TitledPane seeMorePane = new TitledPane();
                newStoryBox.setMaxWidth(258);

                //create hbox display for user story in see more pane.


                // create drop down menus for editing the stories
                ComboBox<String> asigneeChoice = new ComboBox<>();
                asigneeChoice.getItems().addAll(this.assignToComboBox.getItems());
                ComboBox<String> priorityChoice = new ComboBox<>();
                String[] priorities = { "1", "2", "3", "4", "5" };
                priorityChoice.getItems().addAll(priorities);
                ComboBox<String> statusChoice = new ComboBox<>();
                String[] statuses = { "Backlog", "To-do", "In progress", "Done" };
                statusChoice.getItems().addAll(statuses);

                // create two buttons for editing and deleting
                Button editButton= new Button("Save");
                editButton.setPrefSize(50,50);
                editButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        editStory(currentStory,asigneeChoice.getValue(), priorityChoice.getValue(), statusChoice.getValue() );
                    }
                });
                
                Button deleteButton= new Button("Delete");
                deleteButton.setPrefSize(60,50);
                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        deleteStory(currentStory);
                    }
                });


                // create pane to put inside see more
                Pane droppedDownPane = new Pane();
                //construct see more items for persona, feature name and description.
                HBox personaRow= new HBox();
                HBox featureRow= new HBox();
                HBox descriptionRow=new HBox();
                HBox assignRow=new HBox();
                HBox statusRRow=new HBox();
                HBox priorityRRow= new HBox();
                HBox blancspaceRow=new HBox();

                Label Persona = new Label("As:"+" "+listToIterate.get(i).getPersona());
                Label feature= new Label("I want: %s".formatted(listToIterate.get(i).getTitle()));
                Label description=new Label("So that:"+" "+listToIterate.get(i).getDescription());

                Label assignto = new Label();

                try{
                    assignto.setText("Assigned to: "+teammates.get(i).getName());
                }
                catch (Exception e){
                    System.out.println("Error");
                }


                Label status= new Label("Status: " + listToIterate.get(i).getStatus());
                Label priority= new Label("Priority: " + listToIterate.get(i).getPriority());
                Label blancspace=new Label("                       ");

                personaRow.getChildren().add(Persona);
                featureRow.getChildren().add(feature);
                descriptionRow.getChildren().add(description);
                assignRow.getChildren().add(assignto);
                statusRRow.getChildren().add(status);
                priorityRRow.getChildren().add(priority);
                blancspaceRow.getChildren().add(blancspace);

                //construct items to put on pane
                HBox assigneeRow = new HBox();
                Label changeUserLabel = new Label("Select User");
                assigneeRow.getChildren().add(changeUserLabel);
                assigneeRow.getChildren().add(asigneeChoice);

                HBox priorityRow = new HBox();
                Label changePriorityLabel = new Label("Select Priority");
                priorityRow.getChildren().add(changePriorityLabel);
                priorityRow.getChildren().add(priorityChoice);

                HBox statusRow = new HBox();
                Label changeStatusLabel = new Label("Select Status");
                statusRow.getChildren().add(changeStatusLabel);
                statusRow.getChildren().add(statusChoice);

                HBox blancspace2Row= new HBox();
                Label blankspace2= new Label("                                              ");
                blancspace2Row.getChildren().add(blankspace2);



                HBox buttonRow = new HBox();
                buttonRow.getChildren().add(editButton);
                buttonRow.getChildren().add(deleteButton);

                VBox rows = new VBox();
                rows.getChildren().add(personaRow);
                rows.getChildren().add(featureRow);
                rows.getChildren().add(descriptionRow);
                rows.getChildren().add(assignRow);
                rows.getChildren().add(statusRRow);
                rows.getChildren().add(priorityRRow);
                rows.getChildren().add(blancspace);
                rows.getChildren().add(assigneeRow);
                rows.getChildren().add(priorityRow);
                rows.getChildren().add(statusRow);
                rows.getChildren().add(blancspace2Row);
                rows.getChildren().add(buttonRow);

                // add all content to the edit story drop down
                seeMorePane.setContent(rows);
                seeMorePane.setText("See More");
                seeMorePane.setExpanded(false);


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
                newStoryBox.getChildren().add(seeMorePane);

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

        ArrayList<Integer> numbersArray = new ArrayList<>();
        numbersArray.add(sprintNumber);
        numbersArray.add(totalPoints);
        numbersArray.add(totalPointsCompleted);

        map.put("numbersArray", numbersArray);

        map.put("finalActual", finalActual);

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


            ArrayList<Integer> numbersArray;
            numbersArray = (ArrayList<Integer>) map.get("numbersArray");

            sprintNumber = numbersArray.get(0);
            totalPoints = numbersArray.get(1);
            totalPointsCompleted = numbersArray.get(2);

            finalActual = (ArrayList<Integer>) map.get("finalActual");

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

    /*
     * Update the progress bar to reflect current progress
     */
    public void updateProgress() {

        double progress = Double.valueOf(totalPointsCompleted)/totalPoints;
        myprogressbar.setProgress(progress);

        // display the progress as a percentage
        progresslabel.setText(Integer.toString((int) Math.round(progress * 100)) + "%");

        //display how many points are done over the total number of points
        taskupdate.setText(totalPointsCompleted + " / " + totalPoints + " Points Completed");
    }

    // list of points of actual team velocity
    // index will be x value, value will be y value
    ArrayList<Integer> finalActual = new ArrayList<Integer>();


    /**
     * Initialize the scrumBoard progress bar and assign to user combo box
     * @param url ?
     * @param resourceBundle ?
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set colour of progress bar
        myprogressbar.setStyle("-fx-accent: blue;");

        // add no user option to assign to user combo box
        assignToComboBox.getItems().add("No User Assigned");
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
     * 
     * @postcondition: the user has clicked on the "View Burndown" button
     * @postcondition: a new window will open with the burndown chart
     */
    public void BurndownChartWindow() {
        
        // create a new burndown chart
        BurndownChart burndownChart = new BurndownChart();

        // add ideal and actual team velocity to the burndown chart
        burndownChart.mapIdealBurndown(totalPoints, backlog.size()+toDo.size()+inProgress.size()+done.size());
        burndownChart.mapActualBurndown(finalActual, totalPoints);
        
        // display to user
        burndownChart.displayBurndownChart();

    }

    /**
     * Delete a user story from the board
     * 
     * @precondition the story must exist and be assigned a status
     * @postcondition: story icon is removed from board, if assigned to user it is removed from user's list of stories
     * @postcondition:  and is removed from its corresponding status' list
     * @param story the story you wish to delete
     */
    protected void deleteStory(UserStory story)
    {
        // remove from user
        User user = story.getUser();
        try {
            user.removeUserStory(story);
        }
        catch (Exception e)
        {
            // user story is not assigned to a user, do nothing
        }
        // from list"Backlog", "To-do", "In progress", "Done"
        String status = story.getStatus();
        if (status.equals("Backlog")) {
            this.backlog.remove(story);
        }
        else if (status.equals("To-do"))
            this.toDo.remove(story);
        else if (status.equals("In progress"))
            this.inProgress.remove(story);
        else if (status.equals("Done")) {
            this.done.remove(story);
            totalPointsCompleted = totalPointsCompleted - story.getPriority();
            System.out.println(totalPointsCompleted);
        }

        // decrement total points to omit deleted story
        totalPoints = totalPoints - story.getPriority();
        
        updateBoard();
    }

    /**
     * Change the attributes of a user story when edit is selected
     * 
     * @param story the story to be edited
     * @param user a user to assign the story to (can be null if no change wanted)
     * @param priority the new priority you want the story to be (can be null if no change wanted)
     * @param status the new status you want the story to have (can be null if no change wanted)
     * @precondition story is on board and edit button is selected
     * 
     * @postcondition: story attributes change, board updates, user's stories may change if reassigning,
     * status list may change if moving story around board, burndown chart may change if changing priority
     */
    protected void editStory(UserStory story, String user, String priority, String status) {

        // update priority by difference between old and new priority
        if (priority != null) {
            totalPoints = totalPoints - (story.getPriority() - Integer.parseInt(priority));

            if (story.getStatus().equals("Done"))
            {
                totalPointsCompleted = totalPointsCompleted - (story.getPriority() - Integer.parseInt(priority));

            }
            story.setPriority(Integer.parseInt(priority));

        } else {
            Popup unfilledPopup = new Popup("Please fill in all required fields");
            unfilledPopup.displayPopup();
            return;
        }
        
        // change status
        if ((status != null)) {
            if (!status.equals(story.getStatus())) {
                // remove from old status list
                String oldStatus = story.getStatus();
                if (oldStatus.equals("Backlog")) {
                    this.backlog.remove(story);
                } else if (oldStatus.equals("To-do"))
                    this.toDo.remove(story);
                else if (oldStatus.equals("In progress"))
                    this.inProgress.remove(story);
                else if (oldStatus.equals("Done")) {
                    this.done.remove(story);
                    totalPointsCompleted = totalPointsCompleted - story.getPriority();
                }

                // add to new status list
                story.setStatus(status);
                if (status.equals("Backlog")) {
                    this.backlog.add(story);
                } else if (status.equals("To-do"))
                    this.toDo.add(story);
                else if (status.equals("In progress"))
                    this.inProgress.add(story);
                else if (status.equals("Done")) {
                    this.done.add(story);
                    totalPointsCompleted = totalPointsCompleted + story.getPriority();
                }
            }
        }

        // change user
        if (user != null) {
            // find user that was selected
            User newUser = null;
            for (User u : teammates) {
                if (u.getName().equals(user)) {
                    newUser = u;
                }
            }
            User oldUser = story.getUser();
            if (newUser == null || oldUser == null) {
                if (oldUser != null) {
                    try {
                        oldUser.removeUserStory(story);
                        // we now know that newUser is null so just set attribute to null
                        story.setUser(null);
                        story.setColour("White");
                    } catch (Exception e) {
                        // this should never happen as we check if user is null before removing
                    }
                }
                else if (newUser != null) {
                    // oldUser is null, new user is not
                    newUser.addUserStory(story);
                    story.setColour(newUser.getColour());
                    story.setUser(newUser);
                }
                // if both are null, nothing to change
            } else if (!(newUser.equals(oldUser))) {
                try {
                    oldUser.removeUserStory(story);
                } catch (Exception e) {
                    // we have already checked if user is null so this never happens
                }
                newUser.addUserStory(story);
                story.setUser(newUser);
                story.setColour(newUser.getColour());
            }
        }
        updateBoard();

    }
}
