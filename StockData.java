import java.util.Arrays;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.PrintStream;


public class StockData extends Application {
    
    @Override public void start(Stage stage) {
        final NumberAxis xAxis = new NumberAxis(1,100,1);
        final NumberAxis yAxis = new NumberAxis();
        stage.setTitle("Stock Data");
        final LineChart<Number,Number> bc = 
            new LineChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("Closing Price");
        yAxis.setLabel("Volume");
        bc.setTitle("Stock Data");
        String company = "PG volume";
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series1.setName("Volume");
        series2.setName("Closing Price");

        try{
            readCsv();
            final HashMap<Double,Integer> stockhashmap = readCsv();
            int max = stockhashmap.size();
            double[] series1array = new double[max];
            int[] series2array = new int[max];
            PrintStream writer = new PrintStream(new File("/home/mikedes/stockvalues.txt"));
                //System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
                //series1.getData().add(new XYChart.Data(company,entry.getKey() ));
            for(Map.Entry<Double,Integer> entry : stockhashmap.entrySet()) {
                int i=0;
                //System.out.println(entry.getValue());
                series1array[i] = Double.valueOf(entry.getKey());
                //System.out.print(series1array[i]);
                writer.println(series1array[i]);

                series2array[i] = Integer.valueOf(entry.getValue());
                //System.out.print(series2array[i]);
                writer.println(series1array[i]);
                series1.getData().add(new XYChart.Data(series1array[i],series2array[i]));
                //series2.getData().add(new XYChart.Data(company,series2array[i]));
                i++;
                Arrays.sort(series1array);
            }
            //for (int m=0;m<series1array.length;m++){
            //    System.out.println(series1array[m]);
            //}
            xAxis.setLowerBound(series1array[0]);
            xAxis.setUpperBound(series1array[max-1]);
            xAxis.setTickUnit(2);
        } catch (IOException e) {
            System.out.println("Bad " + e);
        }
       
        Scene scene  = new Scene(bc,800,600);
        //bc.getData().addAll(series1, series2, series3);
        bc.getData().addAll(series1,series2);
        stage.setScene(scene);
        stage.show();
    }
    public static HashMap<Double,Integer> readCsv() throws IOException {
        String csvFile = "/home/mikedes/Downloads/table.csv";
        File file = new File(csvFile);
        if(!file.exists()) {
            System.out.println(file.getName() + " does not exist. Exiting.....");
        } else {
            System.out.println("Processing " + file.getName() + " for stock chart.");
        }
        String line = "";
        String cvsSplitBy = ",";
        HashMap<Double,Integer> maps = new HashMap<Double,Integer>();
        try{
            int START_LINE = 1;
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            int counter=START_LINE;
             while((line = br.readLine()) != null) {
                if(counter>START_LINE) {
                    //System.out.println(line);
                    String[] pgPrices = line.split(cvsSplitBy);
                    System.out.println(Double.valueOf(pgPrices[4])+" "+Integer.valueOf(pgPrices[5]));
                    maps.put(Double.valueOf(String.valueOf(pgPrices[4])), Integer.valueOf(pgPrices[5]));
                }
                counter++;
            }
        //    for (Map.Entry<Double, Integer> entry : maps.entrySet()) {
               //  System.out.println("Close price: " + entry.getKey() + ", volume= " + entry.getValue());
        //    }
        } catch(IOException e) {
            System.out.println("bad" + e);
        }
        return maps;
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
