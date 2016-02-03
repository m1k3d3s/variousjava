package omega.monitor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

	public class OmegaTableCellRenderer extends DefaultTableCellRenderer 
	{
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
			String m = maintable.getModel().getValueAt(modelRow, column).toString();
			this.setToolTipText(m);
			if (m.contains("COULD NOT CREATE SOCKET")||m.contains("END OF SESSION")) {
				cell.setForeground(Color.black);
				cell.setBackground(Color.magenta);
			} else if (m.contains("ADMIN DOWN")||(m.contains("NO DATA FROM STATUS"))) {
				cell.setBackground(Color.red);
				cell.setForeground(Color.black);
			} else if (m.contains("LAGGED") || m.contains("QUEUE")) {
				cell.setBackground(Color.yellow);
				cell.setForeground(Color.black);
			} else if (m.contains("TIMEOUT")) {
				cell.setForeground(Color.white);
				cell.setBackground(Color.blue);
			} else if (m.contains("RUNNING") && !(m.contains("FAILOVER")) || m.contains("WAITING") || m.contains("IDLE")) {
				cell.setBackground(Color.green);
				cell.setForeground(Color.black);
			} else if (m.contains("SEQUENCING=FAILOVER")) {
				cell.setForeground(Color.green);
				cell.setBackground(Color.black);
			}
		return cell;
	}
}
