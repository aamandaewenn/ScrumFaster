package ScrumFaster;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;

public class CreateUserController {

    private String[] colours = {"red", "green"};

    @FXML
    private TextField NameTextField;

    @FXML
    private ColorPicker TaskColourPicker;

    @FXML
    private Button CreateUserButton;
}


