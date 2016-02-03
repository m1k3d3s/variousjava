import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class LoadStockPrices {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	java.util.Date date = Calendar.getInstance().getTime();
	java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	String text_date = df.format(date);
;	  
	  public String[] getProperties() {

		  String[] credentials = {"jdbc:mysql://localhost/feedback","sqluser","sqlusermd"};
		  return credentials;
		}
	  
	private void writeStockResults(ResultSet resultSet) throws SQLException {
		  while (resultSet.next()) {
			  String user = resultSet.getString("id");
			  //String date = resultSet.getString("DATUM");
			  String date = text_date;
			  String op = resultSet.getString("OPENINGPRICE");
			  String cp= resultSet.getString("CLOSINGPRICE");
			  String cb = resultSet.getString("BIDPRICE");
			  String co = resultSet.getString("OFFERPRICE");
			  String vol = resultSet.getString("VOLUME");
			  String ccp = resultSet.getString("CCPRICE");
			  System.out.println("ID: " + user);
			  System.out.println("op: " + op);
			  System.out.println("cp: " + cp);
			  System.out.println("cp: " + ccp);
			  System.out.println("cb: " + cb);
			  System.out.println("co: " + co);
			  System.out.println("volume: " + vol);
			  System.out.println("date: " + date);
		  }
	  }
	
	public static ArrayList<String> crunchifyCSVtoArrayList(String crunchifyCSV) {
		ArrayList<String> crunchifyResult = new ArrayList<String>();
		
		if(crunchifyCSV != null){
			String[] splitData = crunchifyCSV.split("\\s*,\\s*");
			for (int i = 0;i < splitData.length; i++) {
				if(!(splitData[i] == null) || !(splitData[i].length() == 0)) {
					crunchifyResult.add(splitData[i].trim());
				}
			}
		}
		
		return crunchifyResult;
	}
	private static java.sql.Timestamp getCurrentTimeStamp() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Timestamp(today.getTime());
	}	
	
	public void readDataBase() throws Exception {
	    try {
	      // This will load the MySQL driver, each DB has its own driver
	    	String[] connects = getProperties();
	      Class.forName("com.mysql.jdbc.Driver").newInstance();
	      // Setup the connection with the DB
	      connect = DriverManager
	          .getConnection(connects[0],connects[1],connects[2]);

	      // Statements allow to issue SQL queries to the database
	      statement = connect.createStatement();
	      // Result set get the result of the SQL query
	      resultSet = statement
	          .executeQuery("select * from feedback.csv_stock_prices");
	      for(int i=0;i<10000;i++) {
	      preparedStatement = connect.prepareStatement("insert into feedback.csv_stock_prices values (default,?,?,?,?,?,?,?)");
	      	//preparedStatement.setString(2, "Test");
	      	preparedStatement.setFloat(2, (float) 999.666666 + i);
	      	preparedStatement.setTimestamp(1, getCurrentTimeStamp());
	      	preparedStatement.setFloat(3,(float) 999.666666 + i);
	      	preparedStatement.setFloat(4,(float) 999.666666 + i);
	      	preparedStatement.setInt(6,1234567 + i);
	      	preparedStatement.setFloat(5,(float) 999.666666 + i);
	      	preparedStatement.setFloat(7,(float) 999.666666 + i);
	      	preparedStatement.executeUpdate();
	      }
	      	writeStockResults(resultSet);
	      System.out.println(resultSet);
	    } catch(Exception e) {
	    	throw e;	
	    } finally {
	    	close();
	    }
	  }
	
	
	
	public static void insertDB(ArrayList<String> arr) throws SQLException{
		PreparedStatement ps = null;
		int sizeofmyarr = 0;
		for(int i = 0; i<arr.size();i++) {
			//System.out.println(arr.get(i) + " ");
			//ps.setInt(1, 100);
			
			sizeofmyarr++;
			if(sizeofmyarr % 7 == 0){
				System.out.println();
				sizeofmyarr = 0;
		}
		
	}
		
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
	
	BufferedReader crunchifyBuffer = null; 
	ArrayList<String> myarr = new ArrayList<String>();
	try {
		String crunchifyLine;
		crunchifyBuffer = new BufferedReader(new FileReader("/home/mikedes/Downloads/table.csv"));
		// How to read file in java line by line?
		while ((crunchifyLine = crunchifyBuffer.readLine()) != null) {
			//System.out.println("Raw CSV data: " + crunchifyLine);
			//System.out.println("Converted ArrayList data: " + crunchifyCSVtoArrayList(crunchifyLine) + "\n");
			myarr.addAll(crunchifyCSVtoArrayList(crunchifyLine));
		}
		
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			if (crunchifyBuffer != null) crunchifyBuffer.close();
		} catch (IOException crunchifyException) {
			crunchifyException.printStackTrace();
		}
	}
	insertDB(myarr);
	
/*	int sizeofmyarr = 0;
	for(int i = 0; i<myarr.size();i++) {
			System.out.println(myarr.get(i) + " ");
			sizeofmyarr++;
			if(sizeofmyarr % 7 == 0){
				System.out.println();
				sizeofmyarr = 0;
		}
		
	}*/
}


private void close() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {
    }
    }
}
