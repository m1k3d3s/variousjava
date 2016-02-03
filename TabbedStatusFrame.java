import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class TabbedStatusFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TabbedStatusFrame() {
		setTitle("Omega Tab Monitor");
		setSize(800,300);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JComponent sectable = new SecaucusTable(ParseMessage.info);
		JComponent chitable = new ChicagoTable(ParseMessage.info);
		JComponent bxbtable = new BoxBTable(ParseMessage.info);
		JComponent criticaltable = new ApplicationTable(ParseMessage.info);
		
		tabbedPane.addTab("sec-dev", sectable);
		tabbedPane.addTab("chi-dev", chitable);
		tabbedPane.addTab("bxb-dev", bxbtable);
		tabbedPane.addTab("Critical apps", criticaltable);
		
		add(tabbedPane, "Center");
	}
}