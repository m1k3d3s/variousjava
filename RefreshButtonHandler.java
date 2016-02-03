package com.mpd.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RefreshButtonHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DataLoaderUserInterface frame = new DataLoaderUserInterface();
        frame.pack();
        frame.setVisible(true);
	}
	
}


