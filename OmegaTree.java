import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JTree;


public class OmegaTree extends JFrame {

	JTree omegatree;
	
	String[][] omegaData = { { "Amy" }, { "Brandon", "Bailey" }, { "Jodi" },
		      { "Trent", "Garrett", "Paige", "Dylan" }, { "Donn" },
		      { "Nancy", "Donald", "Phyllis", "John", "Pat" }, { "Ron" },
		      { "Linda", "Mark", "Lois", "Marvin" } };
	
	public OmegaTree() {
		super("Omega Tree");
		setSize(500,300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public void init() {
		Hashtable h = new Hashtable();
		
		for (int i = 0; i < omegaData.length; i += 2) {
		      h.put(omegaData[i][0], omegaData[i + 1]);
		    }
		    omegatree = new JTree(h);
		    getContentPane().add(omegatree, BorderLayout.CENTER);
	}
	
	 public static void main(String args[]) {
		    OmegaTree tt = new OmegaTree();
		    tt.init();
		    tt.setVisible(true);
		  }

//code for hash(es)
//host_hash, host_instance_hash

	 public class Translation extends JFrame {
		 
		 public void paint(Graphics g) {
			 super.paint(g);
			 
			 Graphics2D g2d = (Graphics2D) g;
			 g2d.setColor(Color.blue);
			 g2d.fillRect(20, 20, 80, 50);
			 g2d.translate(150, 50);
			 g2d.fillRect(20, 20, 80, 50);
		 }
		 
	 }

//code to read in stream , fill in hash(es)



//code to grab all hosts



//code to grab host/instance combination
}