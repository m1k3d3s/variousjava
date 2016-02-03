import java.awt.Color;
import java.util.HashMap;


public class ChicagoTable extends ApplicationTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChicagoTable(HashMap<String,String> data){
		super(data);
		setBackground(Color.black);
		setForeground(Color.orange);
	}

}
