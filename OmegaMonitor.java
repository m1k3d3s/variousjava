package omega.monitor;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class OmegaMonitor 
{	
	public static void main(String[] args) throws Exception
	{
		// assigning all variables needed prior to runtime
		Properties props = new Properties();
		FileInputStream _fin = new FileInputStream("properties.config");
		props.load(_fin);
		final String _host = props.getProperty("login.location");
		String _port = props.getProperty("login.location.port");
		final int _intport = Integer.parseInt(_port);
		_fin.close();
		
		Socket _socket = null;
		PrintWriter _pout = null;
		BufferedReader _bin = null;
		String s = null;
		ArrayList<String> _list = new ArrayList<String>();
		
		OmegaTableFrame _frame = new OmegaTableFrame();
		JScrollPane _scrollpane = new JScrollPane();
		OmegaTableModel _tablemodel = new OmegaTableModel(_list);
		JTable _table = new JTable(_tablemodel);
		_table.setVisible(true);
		_scrollpane.add(_table);
		_scrollpane.setVisible(true);
		_frame.add(_scrollpane);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setVisible(true);
		
		FileInputStream fstream = new FileInputStream("loader_file.in");
		DataInputStream dinstream = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(dinstream));
		while ((br.readLine() != null)) {
				System.out.println(br.readLine());
			} 
		System.out.println("File ended.");
		dinstream.close();
	}
}

