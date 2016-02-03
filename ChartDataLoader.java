package com.mpd.app;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ChartDataLoader extends Application {
	
	static GetProperties gp = new GetProperties();
	static ResultSet resultSet = null;
	static ResultSet availableCharts = null;
	String date,stock;
	int volume;
	float open,close,high,low,adjclose;
	
	public static void doDataLoad() throws SQLException {
		Connection connect = null;
		String stock = gp.selectStock();
		String sql = "select * from historicalprices where stock ="+"'"+stock+"'";
		final Logger LOGGER = Logger.getLogger(DirWatcher.class.getName());
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection(gp.selectJdbcDriver(),gp.selectMysqlUser(),gp.selectMysqlPasswd());
			PreparedStatement pst = (PreparedStatement) connect.prepareStatement(sql);
			resultSet = pst.executeQuery(sql);
		} catch (InstantiationException e) {
			LOGGER.log(Level.WARNING, "Could not instantiate connection.", e);
		} catch(IllegalAccessException e) {
			LOGGER.log(Level.WARNING, "Illegal Access Exception.", e);
        } catch(ClassNotFoundException e1) {
			LOGGER.log(Level.WARNING, "Class not found.", e1);
        }
    }
	
	static void writeMetaData(ResultSet resultSet ) throws SQLException {
		System.out.println("The columns in the table are: ");
	    
	    System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
	    for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
	      System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
	    }
	}
	
	public String GetResultSetDate() throws SQLException {
		date = resultSet.getString(1);
		return date;
	}

	public float GetResultsSetHigh() throws SQLException {
		high = resultSet.getInt(3);
		return high;
		
	}
	
	public float GetResultsSetLow() throws SQLException {
		low = resultSet.getInt(4);
		return low;
	}
	
	public String GetResultSetStock() throws SQLException {
		stock = resultSet.getString(8);
		return stock;
	}
	
	public int GetResultSetVolume() throws SQLException {
		volume = resultSet.getInt(6);
		return volume;
	}

	public float GetResultSetOpen() throws SQLException {
		open = resultSet.getInt(2);
		return open;
	}
	
	public float GetResultSetClose() throws SQLException {
		close = resultSet.getInt(5);
		return close;
	}
	
	public float GetResultSetAdjClose() throws SQLException {
		adjclose = resultSet.getInt(7);
		return adjclose;
	}
	
	public static void createXMLFromDatabase() {
		
	}
	
	
	public static void showAvailableTickerCharts() throws SQLException {
		Connection connect1 = null;
		String sql_stocks = "SELECT DISTINCT(stock) from historicalprices";
		Logger LOGGER = Logger.getLogger(DirWatcher.class.getName());
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect1 = DriverManager.getConnection(gp.selectJdbcDriver(),gp.selectMysqlUser(),gp.selectMysqlPasswd());
			PreparedStatement pst1 = (PreparedStatement) connect1.prepareStatement(sql_stocks);
			availableCharts = pst1.executeQuery(sql_stocks);
		} catch(ClassNotFoundException e1) {
			LOGGER.log(Level.WARNING, "Class not found.", e1);
		} catch(IllegalAccessException e) {
			LOGGER.log(Level.WARNING, "Illegal Access Exception.", e);
		} catch(InstantiationException e) {
			LOGGER.log(Level.WARNING, "Could not instantiate connection.", e);
		}
		
		while (availableCharts.next()){
			//String stock = availableCharts.getString(1);
			//System.out.println(stock);
		}
	}
	
	public void start(Stage stage) throws SQLException{
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		final LineChart<String, Number> lineChart = new LineChart<String,Number>(xAxis, yAxis);
		
		lineChart.setTitle("Daily volume - " + gp.selectStock());
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
	
		series.setName("Volume Data");
		
		while(resultSet.next()){
			series.getData().add(new XYChart.Data<String, Number>(GetResultSetDate(), GetResultSetVolume()));
		}
		
		Scene scene = new Scene(lineChart, 800,900);
		lineChart.getData().add(series);
		
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args){
		try {
			doDataLoad();
			showAvailableTickerCharts();
			launch(args);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
