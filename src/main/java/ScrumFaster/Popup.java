package ScrumFaster;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * A class that creates a popup window to display a message to user
 */
public class Popup{

    /**
     * the message to display to user
     */
    String message;

    /**
     * A method to create a Popup object
     * @param errorMessage the message to display to the usr
     * @author Amanda
     */
    public Popup(String errorMessage)
    {
        message = errorMessage;
    }

    /**
     * method to show a created popup and display its associated message\
     * returns nothing but shows popup on users screen
     */
    public void displayPopup() {
        // window for nodes to go in
        VBox pane = new VBox();
        Stage stage = new Stage();
        Scene scene = new Scene(pane, 500, 100);

        //label to display the message
        Label label = new Label(this.message);
        pane.getChildren().add(label);
        label.setFont(Font.font("Arial Bold"));

        //button to close the window
        Button button = new Button();
        button.setText("OK");
        button.setOnAction(e ->
        {
            stage.close();
        });
        pane.getChildren().add(button);

        // format the window and add everything to it
        pane.setAlignment(Pos.CENTER);
        stage.setTitle("Error");
        stage.setScene(scene);
        stage.show();
    }

}
