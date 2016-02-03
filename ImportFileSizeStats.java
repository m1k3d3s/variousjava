
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class ImportFileSizeStats{
	private static Connection connect = null;
	static ResultSet resultSet = null;

	public static void main(String[] args) throws Exception{
		loadDataBase();
		findLargeFiles();
		writeMetaData(resultSet);
	}
	
	public static void loadDataBase() throws Exception {
		String sql = "LOAD DATA LOCAL INFILE '/tmp/logfileinfo20160106.csv' INTO TABLE vmlogsizes FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 LINES";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection("jdbc:mysql://localhost/feedback?user=sqluser&password=sqlusermd");
			PreparedStatement pst = (PreparedStatement) connect.prepareStatement(sql);
			pst.executeQuery();
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e){
			System.out.println("Run failed");
			throw e;
		}
	}
	
	public static void findLargeFiles() throws Exception {
		//int LARGE_LOG_BYTES = 20971520;
		String sql = "SELECT * from vmlogsizes where logsize > 20971520";
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection("jdbc:mysql://localhost/feedback?user=sqluser&password=sqlusermd");
			PreparedStatement pst = (PreparedStatement) connect.prepareStatement(sql);
			resultSet = pst.executeQuery(sql);
		}catch(Exception e){
			throw e;
		}
		
	}
	
	static void writeMetaData(ResultSet resultSet ) throws SQLException {
		System.out.println("The columns in the table are: ");
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount(); 
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
	    for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
	      System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
	    }
		while (resultSet.next()) {
		    for (int i = 1; i <= columnsNumber; i++) {
		        if (i > 1) System.out.print(",  ");
		        String columnValue = resultSet.getString(i);
		        //System.out.print(columnValue + " " + rsmd.getColumnName(i));
		        System.out.print(columnValue);
		    }
		    System.out.println("");
		}
	}
}