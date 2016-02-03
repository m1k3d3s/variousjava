package com.mpd.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

public class GetStockData {
	
	GetProperties gp = new GetProperties();
	Connection connect = null;
	String stock = gp.selectStock();
	String row = null;
	Float prev_cl = null;
	Float open = null;
	String bid = null;
	String ask = null;
	Float yr_tgt = null;
	Float beta = null;
	String earnings = null;
	String dy_rng = null;
	String yr_rng = null;
	int volume;
	String ticker = null;
	String date = null;
	
	{
		try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connect = DriverManager.getConnection(gp.selectJdbcDriver(),gp.selectMysqlUser(),gp.selectMysqlPasswd());
		String query = "Select * from parsehtmldata;";
		Statement st = connect.createStatement();
		ResultSet rs = st.executeQuery(query);
		writeResultSet(rs);
		st.close();
        buildTableModel(rs);

        //JTable table = new JTable(buildTableMode(rs));
        //JOptionPane.showMessageDialog(null, new JScrollPane(table));

		}catch(Exception e) {
			//LOGGER.severe("Could not update database. " + e);
			//throw e;
			System.out.println(e);
		}
	}


    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
         }

        return new DefaultTableModel(data, columnNames);

    }

	public String writeResultSet(ResultSet resultSet) throws SQLException {
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int numOfColumns = rsmd.getColumnCount();
		
		while(resultSet.next()){
			prev_cl = resultSet.getFloat(1);
			open = resultSet.getFloat(2);
			bid = resultSet.getString(3);
			ask = resultSet.getString(4);
			yr_tgt = resultSet.getFloat(5);
			beta = resultSet.getFloat(6);
			earnings = resultSet.getString(7);
			dy_rng = resultSet.getString(8);
			yr_rng = resultSet.getString(9);
			volume = resultSet.getInt(10);
			ticker = resultSet.getString(11);
			date = resultSet.getString(12);
		}
		return date;
	}
	
	
}

