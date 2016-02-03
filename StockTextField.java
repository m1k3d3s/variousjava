package com.mpd.app;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;

public class StockTextField extends JFormattedTextField implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFormattedTextField stockField;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();	
		if(source == stockField){
			
		}
	}

}
