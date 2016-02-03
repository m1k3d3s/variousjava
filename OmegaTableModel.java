package omega.monitor;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class OmegaTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	String[] _headers = { "Machine/Instance", "State" };
	List<String> _list;
	int _row;
	int _column;
	Object[][] _thingslist;
	
	public OmegaTableModel(List<String> list) {
		this._list = list;
		_column=3;
		_row=this._list.size();
		
		_headers = new String[_column];
	}

	public int getColumnCount() {
		return _column;
	}

	public int getRowCount() {
		return _row;
	}

	public String getColumnName(int i)
	{
		return _headers[i];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		if(rowIndex < 0 || rowIndex > getColumnCount())
			return "";
		{
			switch(columnIndex)
			{
				case 0: return _list.get(columnIndex);
				case 1: return _list.get(columnIndex);
				case 2: return _list.get(columnIndex);
			}
		}
		return "";
	}
	
	public Object setValueAt(int rowIndex, int columnIndex)
	{
		return _list.get(columnIndex);
	}
}
