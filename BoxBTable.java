import java.awt.Color;
import java.util.HashMap;


public class BoxBTable extends ApplicationTable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BoxBTable(HashMap<String,String> data) {
		super(data);
		setBackground(Color.lightGray);
		setForeground(Color.black);
	}

}
