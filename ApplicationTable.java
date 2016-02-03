import java.awt.Color;
import java.util.HashMap;

import javax.swing.JTable;

public class ApplicationTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ApplicationTable(HashMap<String,String> data) {
		setVisible(true);
		setBackground(Color.white);
		setForeground(Color.black);
		setShowHorizontalLines(true);
		setShowVerticalLines(true);
		
	}
}
