package ScrumFaster;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class BurndownChartController extends Application {

    int totalPoints = 3;  // total number of stories
    int numStories = 2;
    Double averagePoints;  // average number of points per story

    @Override
    public void start(Stage stage) {
        stage.setTitle("Burndown Chart");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Sprints");
        yAxis.setLabel("Story Points");

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Burndown Chart");

        XYChart.Series ideal = new XYChart.Series();
        ideal.setName("Ideal Burndown");

        Double averagePoints = Double.valueOf(totalPoints) / numStories;
        Double decrement = totalPoints / averagePoints;
        System.out.println("decrement: " + decrement);

        // map ideal burndown to chart
        for (int i = 0; i <= decrement; i++) {
            System.out.println("x: " + i + " y: " + (totalPoints - (i * averagePoints)));

            if (totalPoints - i * averagePoints >= 0) {
                ideal.getData().add(new XYChart.Data(i, (totalPoints - i * averagePoints)));
            } else {
                ideal.getData().add(new XYChart.Data(i, 0));
            }
        }

        XYChart.Series actual = new XYChart.Series();
        actual.setName("Actual Burndown");
        actual.getData().add(new XYChart.Data(1, 5));
        actual.getData().add(new XYChart.Data(2, 4));
        actual.getData().add(new XYChart.Data(3, 3));

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().addAll(ideal, actual);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
