package com.mpd.app;

import java.net.MalformedURLException;
import java.net.URL;

public class HistoryUrlParser {
	public static void main(String[] args){ 
	try {
		URL url = new URL("http://real-chart.finance.yahoo.com/table.csv?s=YHOO&a=11&b=1&c=2014&d=10&e=1&f=2015&g=d&ignore=.csv");
		}catch(MalformedURLException e) {
			System.out.println("Malformed URL:" + e.getMessage());
		}
	}
}
