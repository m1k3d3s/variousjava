
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;



public class MySQLAccess {
  private Connection connect = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  java.util.Date date = Calendar.getInstance().getTime();
  java.sql.Date sqlDate = new java.sql.Date(date.getTime()); 

public String[] getProperties() {

  String[] credentials = {"jdbc:mysql://localhost/feedback","sqluser","sqlusermd"};
  return credentials;
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
          .executeQuery("select * from feedback.comments");
      writeResultSet(resultSet);
      for(int i=0;i<10;i++) {
      // PreparedStatements can use variables and are more efficient
      preparedStatement = connect
          .prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
      // "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
      // Parameters start with 1
      	preparedStatement.setString(1, "Test"+i);
      	preparedStatement.setString(2, "TestEmail"+i);
      	preparedStatement.setString(3, "TestWebpage"+i);
      	preparedStatement.setTimestamp(4, getCurrentTimeStamp());
      	preparedStatement.setString(5, "TestSummary"+i);
      	preparedStatement.setString(6, "TestComment"+i);
      	preparedStatement.executeUpdate();
      	writeResultSet(resultSet);
      	writeMetaData(resultSet);
      }
      preparedStatement = connect
          .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
      resultSet = preparedStatement.executeQuery();
      writeResultSet(resultSet);

      // Remove again the insert comment
      preparedStatement = connect
      .prepareStatement("delete from feedback.comments where myuser= ? ; ");
      preparedStatement.setString(1, "Test");
      preparedStatement.executeUpdate();
      
      resultSet = statement
      .executeQuery("select * from feedback.stock_prices");
      writeMetaData(resultSet);
      importData(connect, "/home/mikedes/Downloads/table.csv");
      writeStockResults(resultSet);
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }

  }

  private void writeMetaData(ResultSet resultSet) throws SQLException {
    //   Now get some metadata from the database
    // Result set get the result of the SQL query
    
    System.out.println("The columns in the table are: ");
    
    System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
    for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
      System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
    }
  }

  private void writeResultSet(ResultSet resultSet) throws SQLException {
    // ResultSet is initially before the first data set
    while (resultSet.next()) {
      // It is possible to get the columns via name
      // also possible to get the columns via the column number
      // which starts at 1
      // e.g. resultSet.getSTring(2);
      String user = resultSet.getString("myuser");
      String website = resultSet.getString("webpage");
      String summary = resultSet.getString("summary");
      Date date = resultSet.getDate("datum");
      String comment = resultSet.getString("comments");
      System.out.println("User: " + user);
      System.out.println("Website: " + website);
      System.out.println("Summary: " + summary);
      System.out.println("Date: " + date);
      System.out.println("Comment: " + comment);
    }
  }
  
  
  private void writeStockResults(ResultSet resultSet) throws SQLException {
	  while (resultSet.next()) {
		  String user = resultSet.getString("id");
		  String stock = resultSet.getString("MYSTOCK");
		  String op = resultSet.getString("OPENINGPRICE");
		  String cp= resultSet.getString("CLOSINGPRICE");
		  String cb = resultSet.getString("CURRENT_BID");
		  String co = resultSet.getString("CURRENT_OFFER");
		  String date = resultSet.getString("CURR_DATE");
		  System.out.println("ID: " + user);
		  System.out.println("stock: " + stock);
		  System.out.println("op: " + op);
		  System.out.println("cp: " + cp);
		  System.out.println("cb: " + cb);
		  System.out.println("co: " + co);
		  System.out.println("date: " + date);
	  }
  }
  
  public void importData(Connection conn, String filename) throws Exception{
	  Statement stmt;
	  ResultSet rs;
	  String[] connects = getProperties();
	  conn = DriverManager.getConnection(connects[0],connects[1],connects[2]);
	  try
	  {
		  stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		//  query = "LOAD DATA INFILE '"+filename+
		//		    "' INTO TABLE feedback.stock_prices (text,price);";
		  rs = stmt.executeQuery("select * from stock_prices;");
		  writeStockResults(rs);
	  } catch(Exception e) {
		  e.printStackTrace();
		  stmt = null;
	  }
  }

  // You need to close the resultSet
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
