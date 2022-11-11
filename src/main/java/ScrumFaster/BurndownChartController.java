package ScrumFaster;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
 
 
public class BurndownChartController extends Application {
 
    @Override public void start(Stage stage) {
        stage.setTitle("Burndown Chart");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Sprints");
        yAxis.setLabel("Story Points");

        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
       
        lineChart.setTitle("Burndown Chart");
                          
        XYChart.Series ideal = new XYChart.Series();
        ideal.setName("Ideal Burndown");
        
        ideal.getData().add(new XYChart.Data(1, 65));
        ideal.getData().add(new XYChart.Data(2, 60));
        ideal.getData().add(new XYChart.Data(3, 55));
        ideal.getData().add(new XYChart.Data(4, 50));
        ideal.getData().add(new XYChart.Data(5, 45));
        ideal.getData().add(new XYChart.Data(6, 40));
        ideal.getData().add(new XYChart.Data(7, 35));
        ideal.getData().add(new XYChart.Data(8, 30));
        ideal.getData().add(new XYChart.Data(9, 25));
        ideal.getData().add(new XYChart.Data(10, 20));
        ideal.getData().add(new XYChart.Data(11, 15));
        ideal.getData().add(new XYChart.Data(12, 10));
        ideal.getData().add(new XYChart.Data(13, 5));
        ideal.getData().add(new XYChart.Data(14, 0));

        
        XYChart.Series actual = new XYChart.Series();
        actual.setName("Actual Burndown");
        actual.getData().add(new XYChart.Data(1, 65));
        actual.getData().add(new XYChart.Data(2, 50));
        actual.getData().add(new XYChart.Data(3, 75));
        actual.getData().add(new XYChart.Data(4, 29));
        actual.getData().add(new XYChart.Data(5, 45));
        actual.getData().add(new XYChart.Data(6, 20));
        actual.getData().add(new XYChart.Data(7, 35));
        actual.getData().add(new XYChart.Data(8, 45));
        actual.getData().add(new XYChart.Data(9, 55));
        actual.getData().add(new XYChart.Data(10, 7));
        actual.getData().add(new XYChart.Data(11, 15));
        actual.getData().add(new XYChart.Data(12, 15));
        actual.getData().add(new XYChart.Data(13, 10));
        actual.getData().add(new XYChart.Data(14, 0));
        
        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().addAll(ideal, actual);
       
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
