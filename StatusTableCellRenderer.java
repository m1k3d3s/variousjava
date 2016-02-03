import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class StatusTableCellRenderer extends DefaultTableCellRenderer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Component getTableCellRenderer (JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column){
		Component cell = super.getTableCellRendererComponent(table, obj, false, false, row, column);
		int modelRow = table.convertRowIndexToView(row);
		String m = table.getModel().getValueAt(modelRow, column).toString();
		
		if(m.equalsIgnoreCase("STATE=DEAD")){
			cell.setBackground(Color.red);
		}
		else if (m.equalsIgnoreCase("STATE=WAITING")){
			cell.setBackground(Color.magenta);
		}
		else if (m.equalsIgnoreCase("STATE=LAGGED")){
			cell.setBackground(Color.yellow);
		}
		else if (m.equalsIgnoreCase("STATE=ACTIVE")){
			cell.setForeground(Color.black);
			cell.setBackground(Color.green);
		}
		else if (m.equalsIgnoreCase("STATE=UNKNOWN")){
			cell.setBackground(Color.blue);
		}
		else if (m.equalsIgnoreCase("STATE=RUNNING")){
			cell.setForeground(Color.green);
			cell.setBackground(Color.black);
		}
		
		return cell;
	}
}
