import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class StatusFrame extends JFrame {
	/**
	 * 
	 */
	private StatusTable status_model;private static final long serialVersionUID = 1L;
	private Thread readerThread;
	private JTextField statusColor;
	private JPanel statusBar;
    private String s;
    private String hash_status = null;
    private String hash_machine = null;
    private String hash_instance = null;
    private String hash_vm_number = null;
    private String statushash_key = null;
    private HashMap<String, String> statushash = null;
    public Socket socket, socket_reconnect = null;
    public BufferedReader in = null;
    public DataOutputStream dos = null;
    public String bxbhost = StatusMonitor.s_host;
    public String bxbport = StatusMonitor.s_port;
    public int bxbport_int = Integer.parseInt(bxbport);
    public PrintWriter printwriter = null;
    DefaultTableCellRenderer renderer = new StatusTableCellRenderer();
    DefaultTableCellRenderer tooltiprenderer = new DefaultTableCellRenderer();
    
    public StatusFrame()
    {
    	if(bxbhost.contains("dev")) {
    		setTitle("Omega DEV System");
    	} else {
    		setTitle("Omega PROD System");
    	}
        statushash = new HashMap<String,String>();
        readerThread = new Thread((Runnable) new StatusReader());
        readerThread.start();
        
        Font displayFont = new Font("Arial", Font.PLAIN, 10);
        StatusTable model = new StatusTable();
        
        statusColor = new JTextField(2);
        statusColor.setBackground(Color.green);
        statusColor.setEditable(false);
        
        statusBar = new JPanel(new BorderLayout());
        statusBar.add(statusColor, BorderLayout.WEST);

        JTable table = new JTable(model);
        table.setBackground(Color.black);
        table.setForeground(Color.green);
        table.setFont(displayFont);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setOpaque(true);
        table.setAutoResizeMode(5);
        table.setFont(displayFont);
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        sorter.modelStructureChanged();
        
        TableColumn tcol = table.getColumnModel().getColumn(0);
        tcol.setCellRenderer(new StatusTableCellRenderer()); 
        tcol = table.getColumnModel().getColumn(1);
        tcol.setCellRenderer(new StatusTableCellRenderer());
        tcol.setResizable(true);
        tcol.sizeWidthToFit();
        renderer.setToolTipText("Status Monitor");
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.red);
 
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(370,630));
 
        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(scroll, BorderLayout.CENTER);
        mainPane.add(statusBar, BorderLayout.SOUTH);
 
        setContentPane(mainPane);
        pack();
    }
	
}

