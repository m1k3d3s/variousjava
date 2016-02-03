package com.mpd.app;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JFormattedTextField;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.JScrollBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DataLoaderUserInterface extends JFrame{
	
	public DataLoaderUserInterface() {
		ArrayList columnNames = new ArrayList();
		ArrayList data = new ArrayList();
		
		//connect to mysql database. fix as properties later.
		String url = "jdbc:mysql://localhost:3306/feedback";
		String user = "sqluser";
		String pw = "sqlusermd";
		String sql = "SELECT * from historicalprices where stock = 'ICE'";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        try (Connection connection = DriverManager.getConnection( url, user, pw );
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery( sql ))
            {
                ResultSetMetaData md = rs.getMetaData();
                int columns = md.getColumnCount();

                //  Get column names
                for (int i = 1; i <= columns; i++)
                {
                    columnNames.add( md.getColumnName(i) );
                }

                //  Get row data
                while (rs.next())
                {
                    ArrayList row = new ArrayList(columns);

                    for (int i = 1; i <= columns; i++)
                    {
                        row.add( rs.getObject(i) );
                    }

                    data.add( row );
                }
            }
            catch (SQLException e)
            {
                System.out.println( e.getMessage() );
            }
        
        
        Vector columnNamesVector = new Vector();
        Vector dataVector = new Vector();

        for (int i = 0; i < data.size(); i++)
        {
            ArrayList subArray = (ArrayList)data.get(i);
            Vector subVector = new Vector();
            for (int j = 0; j < subArray.size(); j++)
            {
                subVector.add(subArray.get(j));
            }
            dataVector.add(subVector);
        }

        for (int i = 0; i < columnNames.size(); i++ )
            columnNamesVector.add(columnNames.get(i));

        //  Create table with database data    
        final JTable table = new JTable(dataVector, columnNamesVector)
        {
            public Class getColumnClass(int column)
            {
                for (int row = 0; row < getRowCount(); row++)
                {
                    Object o = getValueAt(row, column);

                    if (o != null)
                    {
                        return o.getClass();
                    }
                }

                return Object.class;
            }
        };
        
        String stock = "ICE";	
        PricingTable model = new PricingTable();
        table.setBackground(Color.white);
        table.setForeground(Color.black);
        
        JScrollPane scrollPane = new JScrollPane( table );
        getContentPane().add( scrollPane );
        
        JFormattedTextField ftf = new JFormattedTextField();
        ftf.setValue(stock);
        ftf.setColumns(5);
        
        JButton refresh_button = new JButton();
        refresh_button.setText("Refresh");
        refresh_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Trying to refresh database display.");
				DefaultTableModel cleartable = new DefaultTableModel();
				table.setModel(cleartable);
				displayDatabaseFrame();
			}
			
        });
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refresh_button);
        buttonPanel.add(ftf);
        
        getContentPane().add( buttonPanel, BorderLayout.NORTH );
        getContentPane().add(scrollPane, BorderLayout.SOUTH);
    
    }
        

    private static void displayDatabaseFrame() {
		// TODO Auto-generated method stub
    	DataLoaderUserInterface frame = new DataLoaderUserInterface();
    	frame.dispose();
        frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
        frame.pack();
        frame.setVisible(true);
	}

	public static void main(String[] args) {
        //DataLoaderUserInterface frame = new DataLoaderUserInterface();
        //frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
        //frame.pack();
        //frame.setVisible(true);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				displayDatabaseFrame();
			}

		}
				);
    }
				
}
	

