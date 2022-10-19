package ScrumFaster;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class CreateUserController {

    private String[] colours = {"red", "green"};

    @FXML
    private TextField NameTextField;

    @FXML
    private ColorPicker TaskColourPicker;

    // TODO: Implement This Method
    private void createUser()
    {
        //create new User object
        User newUser = new User(NameTextField.getText(), TaskColourPicker.getValue().toString());
        
        //add to list of users
        ScrumboardController.teammates.add(newUser);

        //close window


    }
}


