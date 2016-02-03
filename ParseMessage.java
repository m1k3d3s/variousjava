import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

public class ParseMessage {
	public static HashMap<String,String> info;    
    public static String[] headers = {"test","test"};
    
	@SuppressWarnings("null")
	public static void main (String [] args) throws IOException {
    	TabbedStatusFrame frame = new TabbedStatusFrame();
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	
    	String[] headers = {"Column1","Column2"};
		JTabbedPane tabbedPane = new JTabbedPane();
		JComponent sectable = new SecaucusTable(info);
		JComponent chitable = new ChicagoTable(info);
		JComponent bxbtable = new BoxBTable(info);
		JComponent criticaltable = new ApplicationTable(info);
		
		tabbedPane.addTab("sec-dev", sectable);
		tabbedPane.addTab("chi-dev", chitable);
		tabbedPane.addTab("bxb-dev", bxbtable);
		tabbedPane.addTab("Critical apps", criticaltable);
		
    	ApplicationTableModel atm = new ApplicationTableModel(info,headers);
    	JTable table = new JTable((TableModel) atm);
    	table.setModel(atm);
    	
    	ConcurrentHashMap<Object, Object> strings = new ConcurrentHashMap<Object, Object>();
    	
    	Scanner scan = null;
    	String s;
    	try {
    		Socket sock = new Socket("sec-dev-launch01.intdata.com", 12348);
    		try {
    		BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    		s = in.readLine();
    		scan = new Scanner(in);
    		scan.useDelimiter("|");
    		int _keyint = 0;
    		while(scan.hasNext()) {
    			strings.put(_keyint, s);
    			// in this block of code, run through message enum_types
    			// and pull out any necessary messages to fill table
    			// discard any message that are not needed for col, row
    			_keyint++;
    			}
    		}finally {
    			System.out.println("socket closed");
    			sock.close();
    		}
    	} finally {
    		System.out.println("scanner dead");
   			scan.close();
   		}
   	}
}

