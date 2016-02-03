import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class NoteSystemUI {
	public void buildAndShow() {
		JFrame ns_frame = new JFrame("Note System");
		JMenuBar ns_menubar = new JMenuBar();
		ns_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//add components here
		//start with menu
		
		JMenuItem menuItem = new JMenuItem("NS Menu");
		menuItem.setPreferredSize(new Dimension(50,50));
		menuItem.add(createMenuBar());
		menuItem.add(createPopUp());
		JMenu m = createMenu("Menu 1");
		m.setPreferredSize(new Dimension(50,50));
		ns_menubar.add(Box.createVerticalGlue());
		ns_menubar.add(m);
		
		//set pane content
		ns_frame.add(ns_menubar);
		
		//display window
		
		ns_frame.setSize(400, 300);
		ns_frame.setVisible(true);
		
	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(Box.createVerticalGlue());
        return menuBar;
	}
	
	public JPopupMenu createPopUp() {
		JPopupMenu popUp = new JPopupMenu();
		return popUp;
	}
	
	public JMenu createMenu(String title) {
		JMenu m = new JMenu(title);
		m.add("1." + "Add note time.");
		//m.add("2." + title);
		//m.add("3." + title);
		//m.add("4." + title);
		//m.add("5." + title);
		//m.add("6." + title);
		return m;
		
	}
}


