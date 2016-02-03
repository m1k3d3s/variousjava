import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.RowFilter;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Statusmonitor
{	
    public static String s_host;
    public static String s_port;
    
    public static void main(String[] args)
    {  	
    	
    	long runtime_mem = Runtime.getRuntime().totalMemory();
    	long runtime_total_mem = Runtime.getRuntime().freeMemory();
    	int available_procs = Runtime.getRuntime().availableProcessors();
    	if(args.length == 2 && Integer.parseInt(args[1]) <= 65535){
    		System.out.println("Connecting to:" + args[0]+ " on port:" + args[1]);
        	Statusmonitor.s_host = args[0];
        	Statusmonitor.s_port = args[1];
            MainFrame frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setBackground(new Color(0,0,128));
            frame.setVisible(true);
    	} else if(args.length > 2) {
    		System.out.println("Only <host> <port> is needed");
    	} else if(args.length < 2) {
    		System.out.println("Need <host> <port> to continue");
    		System.out.println("System runtime memory: " + runtime_mem);
    		System.out.println("System runtime total memory: " + runtime_total_mem);
    		System.out.println("Number of available processors: " + available_procs);
    	} else if(Integer.parseInt(args[1]) > 65535) {
    		System.out.println("Invalid port range");
    	}
    }    
}

class MainFrame extends JFrame 
{
   
	private static final long serialVersionUID = 1L;
	private Thread readerThread;
	private JTextField statusColor;
	private JPanel statusBar;
    private MyTableModel model;
    private JTable table;
    private TableColumn tcol;
    private JTextField searchField,statusField;
    private String s;
    private JButton logButton;
    private JMenuBar menuBar;
    private JMenu menu;
    //private JTextArea logText;
    //public String logTextString = null;
    JCheckBoxMenuItem a_MenuItem,x_MenuItem,r_MenuItem,d_MenuItem,l_MenuItem;
    private String hash_status = null;
    private String hash_machine = null;
    private String hash_instance = null;
    private String hash_vm_number = null;
    private String statushash_key = null;
    private String statushash_key_vm = null;
    private HashMap<String, String> statushash = null;
    private HashMap<String, String> statushash_vm = null;
    public Socket socket, socket_reconnect = null;
    public BufferedReader in = null;
    public DataOutputStream dos = null;
    public String bxbhost = Statusmonitor.s_host;
    public String bxbport = Statusmonitor.s_port;
    public int bxbport_int = Integer.parseInt(bxbport);
    public PrintWriter printwriter = null;
    public PrintWriter printlogs = null;
    DefaultTableCellRenderer renderer = new ColoredTableCellRenderer();
    DefaultTableCellRenderer tooltiprenderer = new ColoredTableCellRenderer();
    public TableRowSorter<TableModel> sorter;
    Logger log = Logger.getLogger("statusmonitor.log");
    protected Component file;

    public MainFrame()
    {
    	
    	try{
    		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    	}
    	catch (UnsupportedLookAndFeelException e) {
    	       // handle exception
    		System.out.println(e.toString());
    	}
    	catch (ClassNotFoundException e) {
    	       // handle exception
    		System.out.println(e.toString());
    	}
    	catch (InstantiationException e) {
    	       // handle exception
    		System.out.println(e.toString());
    	}
    	catch (IllegalAccessException e) {
    	       // handle exception
    		System.out.println(e.toString());
    	}
    	
    	try {

    			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
    			if ("Nimbus".equals(info.getName())){
    					UIManager.setLookAndFeel(info.getClassName());
    					break;
    				}
    			}
    		}catch (Exception e) {
    		    // If Nimbus is not available, you can set the GUI to another look and feel.
    			System.out.println(e + "Nimbus not available");
    		}
    	
    	if(bxbhost.contains("dev")) {
    		setTitle("Omega DEV System");
    	} else {
    		setTitle("Omega PROD System");
    	}
        statushash = new HashMap<String,String>();
        readerThread = new Thread(new Reader());
        readerThread.start();
        
        Font displayFont = new Font("Arial", Font.PLAIN, 10);
        model = new MyTableModel(statushash);
        JList items = new JList();
        searchField = new JTextField("");
        AutoCompleteDecorator.decorate(items, searchField);
        
        statusField = new JTextField("");
        //logText = new JTextArea(logTextString);
       
        searchField.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newFilter();
                    }
                });
        
        statusField.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        statusFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        statusFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        statusFilter();
                    }
                });
        
        TextPrompt tp_search = new TextPrompt("Search for: ", searchField);
        tp_search.setForeground(Color.red);
        tp_search.changeAlpha(1.5f);
        tp_search.changeStyle(Font.BOLD + Font.ITALIC);
        
        TextPrompt status_search = new TextPrompt("Status filter: ", statusField);
        status_search.setForeground(Color.blue);
        status_search.changeAlpha(1.5f);
        status_search.changeStyle(Font.BOLD + Font.ITALIC);
        
        statusColor = new JTextField(2);
        statusColor.setBackground(Color.green);
        statusColor.setEditable(false);
        
        logButton = new JButton();
        logButton.setBackground(Color.white);
        logButton.setForeground(Color.black);
        logButton.setText("Logs");
        logButton.addActionListener(null);
        
        /*logButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					myButtonAction(evt);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
            }*/        
        
        	logButton.addActionListener(new java.awt.event.ActionListener() {
        		public void actionPerformed(java.awt.event.ActionEvent evt) {
        				try {
        					myLogButtonAction(evt);
        				} catch (IOException e) {
							e.printStackTrace();
						} 
        			}
        	
/*            private void myButtonAction(ActionEvent evt) throws FileNotFoundException{
           
            JFrame frame = new JFrame("Logs");
            JTextArea jta = new JTextArea();
            JScrollPane jsp = new JScrollPane(jta);
            
            File file = new File("logs.txt");
            String text = "";
        	int pos = text.length();
            try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String textline = "";
            while(textline!=null){
            	textline = br.readLine();
            	text = text + textline + "\n";
            }
            }catch(Exception ex){
            	ex.printStackTrace();
            }
           
            frame.setVisible(true);
            frame.setBounds(1300, 1400, 500, 400);
            jta.setVisible(true);
            jta.setText(text);
            jsp.setVisible(true);
            jsp.setPreferredSize(new Dimension(250,200));
            jsp.add(jta);
            frame.add(jsp.add(jta));
            };*/

			private void myLogButtonAction(ActionEvent evt) throws IOException {
				File file = new File("logs.txt");
				String filepath = file.getAbsolutePath();
				if(file.exists()){
					JFileChooser fc = new JFileChooser(filepath);
					System.out.println(filepath);
					fc.setFileSelectionMode(fc.FILES_ONLY);
					FileFilter ff = new FileFilter() {
						
					    public boolean accept(File file) {
					        return file.isFile() && file.toString().toLowerCase().endsWith("logs.txt");
					    }
						public String getDescription() {
					    	return "Text documents(*.txt)";
					    }
					};
					fc.setFileFilter(ff);	
					int returnVal = fc.showDialog(getParent(),"Open");
					if(returnVal == JFileChooser.OPEN_DIALOG) {
						Desktop.getDesktop().open(file);
					    }
					}
				}
        });
        
        statusBar = new JPanel(new BorderLayout());
        statusBar.add(statusColor, BorderLayout.WEST);
        statusBar.add(logButton, BorderLayout.EAST);
        statusBar.add(statusField, BorderLayout.CENTER);
        //statusBar.add(logText,BorderLayout.SOUTH);

        table = new JTable((TableModel) model);
        table.setBackground(Color.black);
        table.setForeground(Color.green);
        table.setFont(displayFont);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setOpaque(true);
        table.setAutoResizeMode(5);
        table.setFont(displayFont);
        
        sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        sorter.modelStructureChanged();
        
        tcol = table.getColumnModel().getColumn(0);
        tcol.setCellRenderer(new ColoredTableCellRenderer());
        tcol.setCellRenderer(renderer);
        tcol = table.getColumnModel().getColumn(1);
        tcol.setCellRenderer(new ColoredTableCellRenderer());
        tcol.setResizable(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.red);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(300,1200));
 
        JPanel StatusmonitorPane = new JPanel(new BorderLayout());
        StatusmonitorPane.add(scroll, BorderLayout.CENTER);
        StatusmonitorPane.add(statusBar, BorderLayout.SOUTH);
        StatusmonitorPane.add(searchField, BorderLayout.NORTH); 
        setContentPane(StatusmonitorPane);
        pack();
       

    }    
  
/*    public String getTableCellText(MouseEvent e){
		java.awt.Point p = e.getPoint();
		int rowIndex = rowAtPoint(p);
		int colIndex = colAtPoint(p);
		String value = (String) model.getValueAt(rowIndex, colIndex);
		return value;
    }
    
    private int colAtPoint(Point p) {
		return 0;
	}

	private int rowAtPoint(Point p) {
		return 0;
	}*/

	public class ColoredTableCellRenderer extends DefaultTableCellRenderer{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public Component getTableCellRendererComponent (JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
    		Component cell = super.getTableCellRendererComponent(
                    table, obj, false, false, row, column);
    		int modelRow = table.convertRowIndexToModel(row);
    		String m = table.getModel().getValueAt(modelRow, column).toString();
    		//String hostapp = table.getModel().getValueAt(columnRow, modelRow).toString();
    		//tooltiprenderer.setToolTipText(hostapp);
    		//tcol.setCellRenderer(tooltiprenderer);
    		
    		if (m.equals("D")){
    			cell.setBackground(Color.red);
    		}
    		else if (m.equals("W")){
    			cell.setBackground(Color.magenta);
    		}
    		else if (m.equals("L")){
    			cell.setBackground(Color.yellow);
    		}
    		else if (m.equals("A")){
    			cell.setForeground(Color.black);
    			cell.setBackground(Color.green);
    		}
    		else if (m.equals("X")){
    			cell.setBackground(Color.blue);
    		}
    		else if (m.equals("R")){
    			cell.setForeground(Color.green);
    			cell.setBackground(Color.black);
    		}
				return cell;
		}
    }
    
    private void newFilter() {
        RowFilter<? super TableModel, ? super Integer> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("(?i)" + searchField.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private void statusFilter() {
    	RowFilter<? super TableModel, ? super Integer> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("(?i)" + statusField.getText(), 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

/*    private void noFilter() {
        //RowFilter<? super TableModel, ? super Integer> rf = null;
    	RowFilter rf = RowFilter.regexFilter("",1);
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("", 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private void newFilterA() {
        RowFilter<? super TableModel, ? super Integer> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("^A$",1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private void newFilterR() {
        RowFilter<? super TableModel, ? super Integer> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("^R$",1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private void newFilterX() {
        RowFilter<? super TableModel, ? super Integer> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("^X$",1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private void newFilterL() {
        RowFilter<? super TableModel, ? super Integer> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("^L$",1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private void newFilterD() {
        RowFilter<? super TableModel, ? super Integer> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("^D$",1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }*/
    
    public class MyTableModel extends AbstractTableModel { 	 	
    	/**
    	 * 
    	 */
    	private static final long serialVersionUID = 1L;
    	public String[] headers = {"Machine/Instance","Status"};
    	public HashMap<String, String> rows;
  	
    	public MyTableModel(HashMap<String, String> data){	
    		super();
    		this.rows = statushash;		
    	}
    	
    	public void update()
    	{   	  
          this.rows = statushash;
    	}

    	public int getColumnCount() {
    		return headers.length;
    	}

    	public int getRowCount() {
    		return statushash.size();
    	}
    	public String getColumnName(int columnIndex){
    		return headers[columnIndex];
    	}
   	
    	public Object getValueAt(int row, int col) {
   		
    		switch(col)
    		{
    			case 0:
    				return statushash.keySet().toArray()[row];
    			case 1:
    				return statushash.values().toArray()[row];
    		}		
    		return "";

    	}
    	
    	public void setValueAt(Object value, int row, int col){ 		
    		//nothing to do here
    		getValueAt(row,col);
    	}

    }
    
    private class Reader implements Runnable
    {
        public void run()
        {       	
			try {
				socket = new Socket(bxbhost,bxbport_int);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printwriter = new PrintWriter(socket.getOutputStream(),true);
				printlogs = new PrintWriter("logs.txt");
				} catch (UnknownHostException e1) {
					//e1.printStackTrace();
					//log.log(Level.WARNING, "Unable to connect to host:port");
					System.out.println("Unable to connect to host:port");
				} catch (IOException e1) {
					//e1.printStackTrace();
					//log.log(Level.WARNING, "Unable to read data");
					System.out.println("Unable to read data");
				}
            while(true)
            {
            	 try {
					s = in.readLine();
					System.out.println(s);
					printlogs.write(s+"\n");
            	 } catch (IOException e1) {
					//e1.printStackTrace();
					//log.log(Level.WARNING, "Unable to read data");
					System.out.println("Unable to read data");
					
				}
            	
            	if((s != null) && (s.length() != 0)){
            		
            		if(s.charAt(0)=='>')
                    {
                        try{Thread.sleep(3000);} catch(Exception e){}
                        printwriter.write("h\n");
                        printwriter.flush();
                        //logText.setBackground(Color.white);
                        //System.out.println(s);
                    }
                    else if(s.charAt(18)=='d'){
                      	String s1 = s.substring(20,s.length());
                        StringTokenizer st = new StringTokenizer(s1, ",", false);
                        hash_machine = st.nextToken().toString();
                        hash_instance = st.nextToken().toString();
                        //hash_vm_number = st.nextToken().toString();
                        statushash_key = ((hash_machine+"  "+hash_instance));
                        
                    	statushash.remove(statushash_key);                      	
                        model.update();
                        model.fireTableDataChanged(); 
                    } 
                    else if(s.charAt(18)=='a'){
                    	String s1 = s.substring(20,s.length());
                        StringTokenizer st = new StringTokenizer(s1, ",", false);
                        hash_machine = st.nextToken().toString();
                        hash_instance = st.nextToken().toString();

                    }
                    else if(s.charAt(18)=='s')
                    {                
                    	String logtextarray[] = null;
                    	String s1 = s.substring(20,s.length());
                    	// for logtext to display latest status message
                    	String logtext = s.substring(0, s.length());
                    	StringTokenizer lt = new StringTokenizer(logtext, ",", false);
                    	String datetime = lt.nextToken().toString();
                    	String messagetype = lt.nextToken().toString();
                    	String statusmessage = lt.nextToken().toString();
                    	String host = lt.nextToken().toString();
                    	String appname = lt.nextToken().toString();
                    	String vmnumber = lt.nextToken().toString();
                    	String state = lt.nextToken().toString();
                    	
                    	String logtextstring =  datetime + " " + host + " " + appname + " " + vmnumber + " " + state;
                    	//String logtextstring =  host + " " + appname + " " + vmnumber + " " + state;
                    	//logText.insert(logtextstring,0);
                    	//logText.setEditable(false);
                    	//logText.setBackground(Color.red);
                    	
                    	// end code for log text stuff.
                        StringTokenizer st = new StringTokenizer(s1, ",", false);
                        hash_status = st.nextToken().toString();
                        hash_machine = st.nextToken().toString();
                        hash_instance = st.nextToken().toString();
                        hash_vm_number = st.nextToken().toString();
                        if(s1.charAt(0)=='R'||s1.charAt(0)=='D'||s1.charAt(0)=='W'||s1.charAt(0)=='X'||s1.charAt(0)=='A'||s1.charAt(0)=='L'){
                        statushash_key = ((hash_machine+"  "+hash_instance));
                        statushash_key_vm = ((statushash_key+"  "+hash_vm_number));
                        System.out.println(statushash_key);
                        statushash.put(statushash_key, hash_status);
                        System.out.println(hash_vm_number);
                        //statushash_vm.put(statushash_key_vm, hash_vm_number);
                        // here is where i get a null pointer
                        model.update();
                        model.fireTableDataChanged(); 
                        
                        for (Map.Entry<String, String> entry: statushash.entrySet()) {              
                        	//System.out.println(entry.getValue()+" "+entry.getKey());
                        //Runtime.getRuntime().gc();
                        }                                               
                    }
            	}
            	}
            	else{
            		statusColor.setBackground(Color.red);
            		try {
							socket.close();					
							} catch (IOException e) {
								//e.printStackTrace();
								//log.log(Level.WARNING, "Socket has been closed");
								System.out.println("Socket closed");
							}
							System.exit(0);
            			}
            		}
        		}
    		}
		}




