package ScrumFaster;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/*
 * This class is used to create a burndown chart from scrum data.
 */
public class BurndownChart {

    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private LineChart<Number, Number> burndownChart = new LineChart<Number, Number>(xAxis, yAxis);

    // map ideal burndown to chart
    final XYChart.Series ideal = new XYChart.Series();
    final XYChart.Series actual = new XYChart.Series();

    // constructor
    public BurndownChart() {

        burndownChart.setTitle("Burndown Chart");
        xAxis.setLabel("Sprints Completed");
        yAxis.setLabel("Remaining Effort");


        ideal.setName("Ideal Burndown");
        actual.setName("Actual Burndown");
    }

    /*
     * Calculate and map ideal burndown to chart
     */
    public void mapIdealBurndown(int totalPoints, int num_stories) {
        Double averagePoints = Double.valueOf(totalPoints) / num_stories;
        Double decrement = totalPoints / averagePoints;

        // map ideal burndown to chart
        for (int i = 0; i <= decrement; i++) {
            if (totalPoints - i * averagePoints >= 0) {
                ideal.getData().add(new XYChart.Data(i, totalPoints - i * averagePoints));
            } else {
                // prevent negative values for y axis
                ideal.getData().add(new XYChart.Data(i, 0));
                break;
            }
        }

        burndownChart.getData().add(ideal);
    }

    /*
     * Calculate and map actual burndown to chart
     */
    public void mapActualBurndown(ArrayList<Integer> finalActual, int totalPoints) {
        
        // intially, remaining effort is total points
        actual.getData().add(new XYChart.Data(0, totalPoints));

        for (int i = 0; i < finalActual.size(); i++) {
            // plot the effort remaining for each sprint prior to the current one
            actual.getData().add(new XYChart.Data(i + 1, finalActual.get(i)));
        }
        burndownChart.getData().add(actual);
    }

    /*
     * Display burndown chart in new window
     * 
     * @precondition: burndown chart has been created
     * @postcondition: burndown chart is displayed in new window
     */
    public void displayBurndownChart() {
        Scene scene = new Scene(burndownChart, 800, 600);
        burndownChart.getData().addAll(ideal, actual);

        Stage stage = new Stage();
        stage.setTitle("Burndown Chart");
        stage.setHeight(450);
        stage.setWidth(450);
        stage.setScene(scene);
        stage.showAndWait();
    }
}