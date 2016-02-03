
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.RowFilter;

public class StatusmonitorTab {
	public static String s_host;
	public static String s_port;
	public static String datacenter_locale;

	public static void main(String[] args) throws Exception {
		Properties prop = new Properties();
		String configFile = "StatusMonitor.config";
		InputStream is = new FileInputStream(configFile);

		long runtime_mem = Runtime.getRuntime().totalMemory();
		long runtime_total_mem = Runtime.getRuntime().freeMemory();
		int available_procs = Runtime.getRuntime().availableProcessors();
		if (args.length == 2 && Integer.parseInt(args[1]) <= 65535) {
			System.out.println("Connecting to:" + args[0] + " on port:"
					+ args[1]);
			StatusmonitorTab.s_host = args[0];
			StatusmonitorTab.s_port = args[1];
			MainFrame frame = new MainFrame();
			//no need to set title. i wrote this comment to play with svn
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setBackground(new Color(0, 0, 128));
			frame.setVisible(true);
		} else if (args.length > 2) {
			//System.out.println("Only <host> <port> is needed");
			System.out.println("Defaulting to config file for <host> <port>");
			prop.load(is);
			StatusmonitorTab.s_host = prop.getProperty("login.location");
			StatusmonitorTab.s_port = prop.getProperty("login.location.port");
			StatusmonitorTab.datacenter_locale = prop.getProperty("datacenter.location");
			MainFrame frame = new MainFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setBackground(new Color(0, 0, 128));
			frame.setVisible(true);
		} else if (args.length < 2) {
			//System.out.println("Need <host> <port> to continue");
			System.out.println("System runtime memory: " + runtime_mem);
			System.out.println("System runtime total memory: "
					+ runtime_total_mem);
			System.out.println("Number of available processors: "
					+ available_procs);
			prop.load(is);
			StatusmonitorTab.s_host = prop.getProperty("login.location");
			StatusmonitorTab.s_port = prop.getProperty("login.location.port");
			StatusmonitorTab.datacenter_locale = prop.getProperty("datacenter.location");
			MainFrame frame = new MainFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setBackground(new Color(0, 0, 128));
			frame.setVisible(true);
		} else if (Integer.parseInt(args[1]) > 65535) {
			System.out.println("Invalid port range");
		}

	}
}

class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Thread readerThread;
	private JTextField statusColor;
	private MyTableModel model;
	private MyCriticalTableModel model_critical;
	private JTable maintable, criticaltable;
	private JTabbedPane tabbedPane;
	private TableColumn tcol, crit_col;
	private JTextField searchField, statusField, ignoreField;
	private String s, s_output;
	JCheckBoxMenuItem a_MenuItem, x_MenuItem, r_MenuItem, d_MenuItem,l_MenuItem;
	private String hash_status = null;
	private String hash_machine = null;
	private String hash_instance = null;
	private String hash_vm_number = null;
	private String hash_session = null;
	private String hash_maxsequence = null;
	private String hash_sequence = null;
	private String hash_state = null;
	private String hash_value_extra = null;
	private String statushash_key = null;
	private URLConnection yc = null;
	private ConcurrentHashMap<String,String> statushash = null;
	private ConcurrentHashMap<String,String> statushash_critical = null;
	public Socket socket, socket_chi, socket_bxb = null;
	public BufferedReader in, in_chi, in_bxb = null;
	public DataOutputStream dos = null;
	public PrintWriter printwriter = null;
	public PrintWriter printlogs = null;
	public String bxbhost = StatusmonitorTab.s_host;
	public String bxbport = StatusmonitorTab.s_port;
	public int bxbport_int = Integer.parseInt(bxbport);
	DefaultTableCellRenderer renderer = new ColoredTableCellRenderer();
	DefaultTableCellRenderer tooltiprenderer = new ColoredTableCellRenderer();
	public TableRowSorter<TableModel> sorter, sorter_critical;
	Logger log = Logger.getLogger("statusmonitor.log");
	protected Component file;
	Properties location = new Properties();
	String datacenter_locale = location.getProperty("datacenter.location");

	public MainFrame() {

		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
			System.out.println(e.toString());
		} catch (ClassNotFoundException e) {
			// handle exception
			System.out.println(e.toString());
		} catch (InstantiationException e) {
			// handle exception
			System.out.println(e.toString());
		} catch (IllegalAccessException e) {
			// handle exception
			System.out.println(e.toString());
		}

		try {

			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
			System.out.println(e + "Nimbus not available");
		}
		
		
		statushash = new ConcurrentHashMap<String, String>();
		statushash_critical = new ConcurrentHashMap<String, String>();
		readerThread = new Thread(new Reader());
		readerThread.start();
		Font displayFont = new Font("Arial", Font.PLAIN, 10);
		model = new MyTableModel(statushash);
		model_critical = new MyCriticalTableModel(statushash_critical);
		searchField = new JTextField("");
		statusField = new JTextField("");
		ignoreField = new JTextField("");

		TextPrompt tp_search = new TextPrompt("Full list: ", searchField);
		//TextPrompt tp_ignore = new TextPrompt("Ignore text: ", ignoreField);
		tp_search.setForeground(Color.red);
		tp_search.changeAlpha(1.5f);
		tp_search.changeStyle(Font.BOLD + Font.ITALIC);

		TextPrompt status_search = new TextPrompt("Status filter: ",
				statusField);
		status_search.setForeground(Color.blue);
		status_search.changeAlpha(1.5f);
		status_search.changeStyle(Font.BOLD + Font.ITALIC);

		statusColor = new JTextField(2);
		statusColor.setBackground(Color.green);
		statusColor.setEditable(false);

		maintable = new JTable((TableModel) model);
		maintable.setBackground(Color.black);
		maintable.setForeground(Color.green);
		maintable.setFont(displayFont);
		maintable.setShowHorizontalLines(false);
		maintable.setShowVerticalLines(false);
		maintable.setOpaque(true);
		maintable.setAutoResizeMode(5);
		maintable.setFont(displayFont);

		criticaltable = new JTable((TableModel) model_critical);
		criticaltable.setBackground(Color.black);
		criticaltable.setForeground(Color.green);
		criticaltable.setFont(displayFont);
		criticaltable.setShowHorizontalLines(false);
		criticaltable.setShowVerticalLines(false);
		criticaltable.setOpaque(true);
		criticaltable.setAutoResizeMode(5);
		criticaltable.setFont(displayFont);
		tabbedPane = new JTabbedPane();

		setTitle("Omega Tab Monitor");
		setSize(300, 800);
		add(tabbedPane, "Center");
		
		sorter = new TableRowSorter<TableModel>(model);
		maintable.setRowSorter(sorter);
		sorter.modelStructureChanged();

		sorter_critical = new TableRowSorter<TableModel>(model_critical);
		criticaltable.setRowSorter(sorter_critical);
		
		tcol = maintable.getColumnModel().getColumn(0);
		tcol.setCellRenderer(new ColoredTableCellRenderer());
		tcol.setCellRenderer(renderer);
		tcol = maintable.getColumnModel().getColumn(1);
		tcol.setCellRenderer(new ColoredTableCellRenderer());
		tcol.setResizable(true);
		
		crit_col = criticaltable.getColumnModel().getColumn(0);
		crit_col.setCellRenderer(new ColoredTableCellRenderer());
		crit_col.setCellRenderer(renderer);
		crit_col = criticaltable.getColumnModel().getColumn(1);
		crit_col.setCellRenderer(new ColoredTableCellRenderer());
		crit_col.setResizable(true);
		JTableHeader header = maintable.getTableHeader();
		JTableHeader crit_header = criticaltable.getTableHeader();
		header.setBackground(Color.red);
		crit_header.setBackground(Color.red);
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.add(statusColor, BorderLayout.WEST);
        statusBar.add(searchField, BorderLayout.CENTER);
        //statusBar.add(ignoreField, BorderLayout.SOUTH);
		JScrollPane secscroll = new JScrollPane(maintable);
		JScrollPane criticalscroll = new JScrollPane(criticaltable);
		tabbedPane.addTab(StatusmonitorTab.datacenter_locale, null , secscroll, "Full list of all running Omega apps");
		tabbedPane.addTab("Critical apps", null, criticalscroll, "List of apps with status other than Active");
		add(tabbedPane,BorderLayout.CENTER);
		add(statusBar,BorderLayout.SOUTH);
		
        searchField.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newSearchFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        newSearchFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newSearchFilter();
                    }
                });
		
	
        statusField.getDocument().addDocumentListener(
            new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    newStatusFilter();
                }
                public void insertUpdate(DocumentEvent e) {
                    newStatusFilter();
                }
                public void removeUpdate(DocumentEvent e) {
                    newStatusFilter();
                }
            });
/*        
        ignoreField.getDocument().addDocumentListener(
        		new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newIgnoreFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        newIgnoreFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newIgnoreFilter();
                    }
                });
        */
}
	
    private void newSearchFilter() {
        RowFilter<? super TableModel, ? super Integer> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("(?i)" + searchField.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void newStatusFilter() {
        RowFilter<? super TableModel, ? super Integer> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("(?i)" + statusField.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter_critical.setRowFilter(rf);
    }
    
/*    private void newIgnoreFilter() {
    	RowFilter<? super TableModel, ? super Integer> rf = null;
    	RowFilter<? super TableModel, ? super Integer> nf = null;
    	//If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("(?i)"+ searchField.getText(), 0);
            nf = RowFilter.notFilter(rf);
            sorter.setRowFilter(nf);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        //sorter.setRowFilter(rf);
    	
    }*/
    
    
    
	public class ColoredTableCellRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable maintable,
				Object obj, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cell = super.getTableCellRendererComponent(maintable, obj,
					false, false, row, column);
			int modelRow = maintable.convertRowIndexToModel(row);
			String state_color = "none";
			
			String m = maintable.getModel().getValueAt(modelRow, column).toString();
			this.setToolTipText(m);
			//System.out.println(m);
			if (m.contains("COULD NOT CREATE SOCKET")||m.contains("END OF SESSION")) {
				cell.setForeground(Color.black);
				cell.setBackground(Color.magenta);
				state_color = "magenta";
			} else if (m.contains("DOWN")||(m.contains("NO DATA FROM STATUS"))||m.contains("STOP")||m.contains("ZOMBIE")) {
				cell.setBackground(Color.red);
				cell.setForeground(Color.black);
				state_color = "red";
			} else if (m.contains("LAGGED") || m.contains("QUEUE")) {
				cell.setBackground(Color.yellow);
				cell.setForeground(Color.black);
				state_color = "yellow";
			} else if (m.contains("TIMEOUT")) {
				cell.setForeground(Color.white);
				cell.setBackground(Color.blue);
				state_color = "blue";
			} else if (m.contains("NOSESSION")) {
				cell.setBackground(Color.cyan);
				cell.setForeground(Color.black);
				state_color = "cyan";
			} else if (m.contains("STARTED=0")) {
				cell.setForeground(Color.green);
				cell.setBackground(Color.black);
				state_color = "black";
			} else if (m.contains("RUNNING") && !(m.contains("FAILOVER")) || m.contains("WAITING") || m.contains("IDLE")) {
				cell.setBackground(Color.green);
				cell.setForeground(Color.black);
				state_color = "green";
			} else if (m.contains("SEQUENCING=FAILOVER")) {
				cell.setForeground(Color.green);
				cell.setBackground(Color.black);
				state_color = "black";
			} 
			return cell;
		}
	}
	//filter for critical apps
	private void newFilter() {
		RowFilter<? super TableModel, ? super Integer> rf = null;
		// If current expression doesn't parse, don't update.
		   ArrayList<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object,Object>>(2);
		   filters.add(RowFilter.regexFilter("TIMEOUT"));
		   filters.add(RowFilter.regexFilter("LAGGED"));
		   filters.add(RowFilter.regexFilter("WAITING"));
		   filters.add(RowFilter.regexFilter("ADMIN DOWN"));
		   filters.add(RowFilter.regexFilter("COULD NOT CREATE SOCKET"));
		   filters.add(RowFilter.regexFilter("NO DATA"));
		   filters.add(RowFilter.regexFilter("QUEUE"));
		   filters.add(RowFilter.regexFilter("END OF SESSION"));
		   filters.add(RowFilter.regexFilter("IDLE"));
		   filters.add(RowFilter.regexFilter("FAILOVER"));
		   filters.add(RowFilter.regexFilter("STARTED=0"));
		   //dead, stop , running , zombie
		   filters.add(RowFilter.regexFilter("DEAD"));
		   filters.add(RowFilter.regexFilter("STOP"));
		   filters.add(RowFilter.regexFilter("ZOMBIE"));
		try {
			rf = RowFilter.orFilter(filters);
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorter_critical.setRowFilter(rf);
	}

	
	
	public class MyTableModel extends AbstractTableModel {
		/**
    	 * 
    	 */
		private static final long serialVersionUID = 1L;
		public String[] headers = { "Machine/Instance", "State" };
		public ConcurrentHashMap<String, String> rows;

		public MyTableModel(ConcurrentHashMap<String, String> statushash1) {
			super();
			this.rows = statushash1;
		}

		public void update() {
			this.rows = statushash;
		}

		public int getColumnCount() {
			return headers.length;
		}

		public int getRowCount() {
			return statushash.size();
		}

		public String getColumnName(int columnIndex) {
			return headers[columnIndex];
		}

		public Object getValueAt(int row, int col) {

			switch (col) {
			case 0:
				return statushash.keySet().toArray()[row];
			case 1:
				return statushash.values().toArray()[row];
			}
			return "";

		}

		public void setValueAt(Object value, int row, int col) {
			// nothing to do here
			getValueAt(row, col);
		}

	}

	public class MyCriticalTableModel extends AbstractTableModel {
		/**
    	 * 
    	 */
		private static final long serialVersionUID = 1L;
		public String[] headers = { "Machine/Instance", "State" };
		public ConcurrentHashMap<String, String> rows;

		public MyCriticalTableModel(ConcurrentHashMap<String, String> data) {
			super();
			this.rows = data;
		}

		public void update() {
			this.rows = statushash_critical;
		}

		public int getColumnCount() {
			return headers.length;
		}

		public int getRowCount() {
			return statushash_critical.size();
		}

		public String getColumnName(int columnIndex) {
			return headers[columnIndex];
		}

		public Object getValueAt(int row, int col) {

			switch (col) {
			case 0:
				return statushash_critical.keySet().toArray()[row];
			case 1:
				return statushash_critical.values().toArray()[row];
			}
			return "";

		}

		public void setValueAt(Object value, int row, int col) {
			// nothing to do here
			getValueAt(row, col);
		}

	}

	private class Reader implements Runnable {
		public void run() {
			try {
				socket = new Socket(bxbhost,bxbport_int);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				printwriter = new PrintWriter(socket.getOutputStream(), true);
				printlogs = new PrintWriter("logs.txt");
			} catch (UnknownHostException e1) {
				System.out.println("Unable to connect to host:port");
			} catch (IOException e1) {
				System.out.println("Unable to read data");
			}
			while (true) {
				try {
					s = in.readLine();
				} catch (IOException e1) {
					System.out.println("Unable to read data.");
				} 
				if ((s != null) && (s.length() != 0)) {
					if (s.charAt(0) == '>') {
						System.out.println(s);
						try {
							Thread.sleep(3000);
							newFilter();
						} catch (Exception e) {
							System.out.println("resize");
						}
						printwriter.write("h\n");
						printwriter.flush();
					} else if (s.contains("MSG_TYPE=R")) {
						StringTokenizer st = new StringTokenizer(s, "|", false);
						String timestamp = st.nextToken().toString();
						String message_type = st.nextToken().toString();
						hash_machine = st.nextToken().toString();
						hash_instance = st.nextToken().toString();
						String hash_m = hash_machine.substring(5);
						String hash_i = hash_instance.substring(5);
						statushash_key = ((hash_m + "  " + hash_i));
						statushash.remove(statushash_key);
						statushash_critical.remove(statushash_key);
						//model_critical.update();
						model_critical.fireTableDataChanged();
						//model.update();
						model.fireTableDataChanged();
					} else if (s.contains("MSG_TYPE=A")) {
						StringTokenizer st = new StringTokenizer(s, "|", false);
						String timestamp = st.nextToken().toString();
						String message_type = st.nextToken().toString();
						hash_machine = st.nextToken().toString();
						hash_instance = st.nextToken().toString();
						String hash_m = hash_machine.substring(5);
						String hash_i = hash_instance.substring(5);
					} else if (s.contains("MSG_TYPE=C")) {
						System.out.println("Starting to get messages");
					} else if (s.contains("MSG_TYPE=U")) {
						StringTokenizer st = new StringTokenizer(s, "|", false);
						//int s1 = s.indexOf("STATE", 0);
						//System.out.println(s_output);
						String timestamp = st.nextToken().toString();
						String message_type = st.nextToken().toString();
						hash_machine = st.nextToken().toString();
						hash_instance = st.nextToken().toString();
						hash_status = st.nextToken().toString();
						if(st.hasMoreTokens()){
							String more_tokes = st.nextToken().toString();
						}
						String hs[] = hash_status.split("=");
						String hash_m = hash_machine.substring(5);
						String hash_i = hash_instance.substring(5);
						statushash_key = ((hash_m + "  " + hash_i));
						statushash.put(statushash_key, s);
						statushash_critical.put(statushash_key, s);
						model_critical.update();
						model_critical.fireTableDataChanged();
						model.update();
						model.fireTableDataChanged();
					}
				} else {
					statusColor.setBackground(Color.red);
					try {
						socket.close();
					} catch (IOException e) {
						System.out.println("Socket closed");
					}
				}
			}
		}
	}
}
