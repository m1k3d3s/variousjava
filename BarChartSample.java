import java.util.Arrays;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
 
public class BarChartSample extends Application {
    final static String austria = "Austria";
    final static String brazil = "Brazil";
    final static String france = "France";
    final static String italy = "Italy";
    final static String usa = "USA";
    int max = 6;
 
    @Override public void start(Stage stage) {
        stage.setTitle("Bar Chart Sample");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = 
            new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Country Summary");
        xAxis.setLabel("Country");       
        yAxis.setLabel("Value");
        int[] series1array = new int[max];
        Random rgen1 = new Random();
        for(int i=0;i<=5;i++) {
              series1array[i]=rgen1.nextInt(100000);
        }        
        
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2003");       
        series1.getData().add(new XYChart.Data(austria, series1array[0]));
        series1.getData().add(new XYChart.Data(brazil, series1array[1]));
        series1.getData().add(new XYChart.Data(france, series1array[2]));
        series1.getData().add(new XYChart.Data(italy, series1array[3]));
        series1.getData().add(new XYChart.Data(usa, series1array[4]));      
        
        int[] series2array = new int[max];
        Random rgen2 = new Random();
        for(int j=0;j<=5;++j) {
            series2array[j]=rgen2.nextInt(100000);
        }

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("2004");
        series2.getData().add(new XYChart.Data(austria, series2array[0]));
        series2.getData().add(new XYChart.Data(brazil, series2array[1]));
        series2.getData().add(new XYChart.Data(france, series2array[2]));
        series2.getData().add(new XYChart.Data(italy, series2array[3]));
        series2.getData().add(new XYChart.Data(usa, series2array[4]));  
        
        int[] series3array = new int[max];
        Random rgen3 = new Random();
        for(int k=0;k<=5;++k) {
            series3array[k]=rgen2.nextInt(100000);
        }

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("2005");
        series3.getData().add(new XYChart.Data(austria, series3array[0]));
        series3.getData().add(new XYChart.Data(brazil, series3array[1]));
        series3.getData().add(new XYChart.Data(france, series3array[2]));
        series3.getData().add(new XYChart.Data(italy, series3array[3]));
        series3.getData().add(new XYChart.Data(usa, series3array[4]));  
        
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1, series2, series3);
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
