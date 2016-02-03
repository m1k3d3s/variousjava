package adrmonitortree;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class statushashTree 
{
    public static void main(String[] args)
    {
        StatustreeFrame frame = new StatustreeFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
 
}

class StatustreeFrame extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread readerThread, reloadThread;
	private JTextField statusColor;
	private JPanel statusBar;
    private JTree m_tree;
    private DefaultTreeModel m_model;
    private DefaultTreeCellRenderer renderer = new ColoredTreeCellRenderer();
    private DefaultMutableTreeNode rootNode, groupNode, statusNode = null;
    private String statusstring;
    private String hash_status = null;
    private String hash_machine = null;
    private String hash_instance = null;
    private String statushash_key = null;
    private Hashtable<String, String> statushash = null;
    private Vector<String> machinevector = null;
    public Socket socket = null;
    public BufferedReader in = null;
    public DataOutputStream dos = null;
	public String bpfhost = "sec-dev-launch01.intdata.com";
    public int bpfport = 12346;
    public PrintWriter printwriter = null;
 
    public StatustreeFrame()
    {
        setTitle("Core Monitor");
        readerThread = new Thread(new Reader());
        readerThread.start();  
        reloadThread = new Thread(new Reload());
        reloadThread.start();
   
        statushash = new Hashtable<String,String>();
        machinevector = new Vector<String>();
        
        statusColor = new JTextField(2);
        statusColor.setBackground(Color.green);
        statusColor.setEditable(false);
        rootNode = new DefaultMutableTreeNode("CoreMonitor");
        groupNode = new DefaultMutableTreeNode();
        m_model = new DefaultTreeModel(rootNode);
        statusBar = new JPanel(new BorderLayout());
        statusBar.add(statusColor, BorderLayout.WEST);
        m_tree = new JTree(statushash);
        m_tree.setEditable(false);
        m_tree.setShowsRootHandles(false);
        m_tree.setCellRenderer(renderer);
        renderer.setOpaque(true);
        renderer.setClosedIcon(new ImageIcon("images/smallomega.gif"));
        renderer.setOpenIcon(new ImageIcon("images/smallomega.gif"));
        renderer.setLeafIcon(new ImageIcon("images/smallomega.gif"));  
        //implement the scroll 
        JScrollPane scroll = new JScrollPane(m_tree);
        scroll.setPreferredSize(new Dimension(300,640));
        //put scroll (JScrollPane) into mainPane
        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(scroll, BorderLayout.CENTER);
        mainPane.add(statusBar, BorderLayout.SOUTH); 
        setContentPane(mainPane);
        pack();
        addParentNodes();
        
        

    }
    
    class OmegaTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
        public void fireIntervalRemoval(TreeModelEvent e){
        }
        public void fireTreeNodesInserted(TreeModelEvent e){
        }
    }

    public class ColoredTreeCellRenderer extends DefaultTreeCellRenderer{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;				
		public Component getTreeCellRendererComponent (JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    		Component cell = super.getTreeCellRendererComponent(
                    tree, value, false, false, true, row, false);

    		if (value.toString().endsWith("=D")) {
    			cell.setBackground(Color.yellow);
    		}
    		else if (value.toString().endsWith("=R")) {
    			cell.setBackground(Color.white);
    		}
    		else if (value.toString().endsWith("=W")){
    			cell.setBackground(Color.magenta);
    		}
    		else if (value.toString().endsWith("=X")){
    			cell.setBackground(Color.blue);
    		}
    		else if (value.toString().endsWith("=A")){
    			cell.setBackground(Color.green);
    		}
    		else {
    			cell.setBackground(Color.white);
    		}
			return cell;
   		} 
    }
    
    private class Reload implements Runnable

    {
    	@SuppressWarnings("finally")
		public void run(){
    		try {
    			Thread.sleep(5000);
    		} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
    			return;
    		}
    	}
    }
    
//    private void expandAll (JTree tree)	{
//    	int row = 0;
//    	while (row < tree.getRowCount()){
//    		tree.expandRow(row);
//    		row++;
//    	}
//    }
    
    private ActionListener removeNodeFromModel = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed1(java.awt.event.ActionEvent)
         */
        public void actionPerformed1(ActionEvent e) {

           TreePath currentSelection = m_tree.getSelectionPath();
           if (currentSelection != null) {
              DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection
                    .getLastPathComponent();
              DefaultTreeModel model = ((DefaultTreeModel) m_tree.getModel());
              model.removeNodeFromParent(node);
           }
        }

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
     };

    
    
    //code to add nodes(leafs) in sorted order. Not changeable after this.
    private void addNodeInSortedOrder(DefaultMutableTreeNode parent,
    	DefaultMutableTreeNode child){
    		int n = parent.getChildCount();
    		if(n==0){
    			parent.add(child);
    			return;
    		}
    			DefaultMutableTreeNode node=null;
    			for(int i=0;i<n;i++){
    				node = (DefaultMutableTreeNode)parent.getChildAt(i);
    				if(node.toString().compareTo(child.toString())>0){
    					parent.insert(child, i);
    					return;
    				}
    			}
    				parent.add(child);
    				return;
    	}
    
    
    private void addParentNodes() {
        for (String omegamachine : machinevector){
      	DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(omegamachine);
      	rootNode.add(groupNode);
      	//printDescendants(rootNode);
      	addNodeInSortedOrder(rootNode,groupNode);
      	for (Map.Entry<String,String> machine: statushash.entrySet()){
      		DefaultMutableTreeNode statusNode = new DefaultMutableTreeNode(machine);
      		if(statusNode.toString().contains(omegamachine.toString())){
      			groupNode.add(statusNode);
      		}
      	}
      }
      }
    
    private class Reader implements Runnable
    {
        public void run()
        {       	
			try {
				socket = new Socket(bpfhost,bpfport);
				in = new BufferedReader(new
                InputStreamReader(socket.getInputStream()));
                printwriter = new PrintWriter(socket.getOutputStream(),true);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            while(true)
            {
            	 try {
					statusstring = in.readLine();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            	if((statusstring != null) && (statusstring.length() != 0)){
            		
                    if(statusstring.charAt(0)=='>')
                    {
                        try{Thread.sleep(5000);
                        } catch(Exception e){}
                        	printwriter.write("h\n");
                        	printwriter.flush();
                    	}                                                                           
                    if(statusstring.charAt(0)=='d'){
                      	String s1 = statusstring.substring(1,statusstring.length());
                        StringTokenizer st = new StringTokenizer(s1, ",", false);
                        hash_machine = st.nextToken().toString();
                        hash_instance = st.nextToken().toString();
                        statushash_key = ((hash_machine+"  "+hash_instance));
                        statushash.remove(statushash_key);
                        TreePath currentSelection = m_tree.getSelectionPath();
                        if (currentSelection != null)	{
                        	 groupNode = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
                        	 m_model = ((DefaultTreeModel) m_tree.getModel());
                        	 m_model.removeNodeFromParent(groupNode);
                        }
                        
                    }                                      
                    if(statusstring.charAt(0)=='s')
                    {                       	
                    	String s1 = statusstring.substring(1,statusstring.length());
                        StringTokenizer st = new StringTokenizer(s1, ",", false);
                        hash_status = st.nextToken().toString();
                        hash_machine = st.nextToken().toString();
                        hash_instance = st.nextToken().toString(); 
                        if(statusstring.charAt(1)=='A'||statusstring.charAt(1)=='R'||statusstring.charAt(1)=='D'||statusstring.charAt(1)=='W'||statusstring.charAt(1)=='X'){
                        statushash_key = ((hash_machine+"  "+hash_instance));
                        statushash.put(statushash_key, hash_status);
                        if(!machinevector.contains(hash_machine)){
                        	machinevector.add(hash_machine);
                        }
                    }
            	}
            	}
            	else{
            		statusColor.setBackground(Color.red);
            		try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
            }
        }
    }
}