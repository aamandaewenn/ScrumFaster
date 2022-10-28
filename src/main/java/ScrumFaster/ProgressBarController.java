package ScrumFaster;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;


public class ProgressBarController implements Initializable {
    @FXML
    private Button MyButton;

    @FXML
    private Label progresslabel;

    @FXML
    private ProgressBar myprogressbar;

    //we could use the BigDecimal class because it gives the user complete control over rounding behaviour.
    BigDecimal progress = new BigDecimal(String.format("%.2f",0.0)); //this is a big decimal constructor, where we could pass in a format string.
                                                                            //format the string to be %.2f, and the arguments will be the initial value we will begin with
                                                                        //which is set t0 zero.
    //another variable will be use to calulate the percent of work done over the ones that are not complete.

    public void increaseprogress()
    {
        if(progress.doubleValue() < 1)
        {
            progress= new BigDecimal(String.format("%.2f",progress.doubleValue() + 0.1)); //this is to access the value stored in the progress construct
            System.out.println(progress.doubleValue()); //this prints the value to the console.
            myprogressbar.setProgress(progress.doubleValue()); //pass in value of the progress, for this project we will be passing in the
            // the ratio of the work done over the work that is not yet done.

            progresslabel.setText(Integer.toString((int) Math.round(progress.doubleValue() * 100)) + "%"); //cast as in intergar for precison sake
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        myprogressbar.setStyle("-fx-accent: green;");


    }
}

