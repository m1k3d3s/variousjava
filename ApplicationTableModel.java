import java.util.HashMap;

import javax.swing.table.AbstractTableModel;


public class ApplicationTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private HashMap<String, String> statushash = null;
    private HashMap<String, String> rows;
    
	private String[] headers2 = {"Machine/Instance","Status"};
	
	public ApplicationTableModel(HashMap<String, String> data, String[] headers2){	
		super();
		this.rows = statushash;		
	}
	
	public void update()
	{   	  
      this.rows = statushash;
	}

	public int getColumnCount() {
		return headers2.length;
	}

	public int getRowCount() {
		return statushash.size();
	}
	public String getColumnName(int columnIndex){
		return headers2[columnIndex];
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
