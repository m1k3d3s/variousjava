package com.mpd.app;

import com.opencsv.CSVReader;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CsvDownloadReader {

	public static void main(String[] args){
	GetProperties gp = new GetProperties();
	try {
		boolean submittodb = gp.selectDbSubmit();
		int startDate[] = gp.selectStartDate();
		int endDate[] = gp.selectEndDate();
		int startmonth = (startDate[0]-1),startday=startDate[1],startyear=startDate[2];
		int endmonth = (endDate[0]-1),endday=endDate[1],endyear=endDate[2];
		String stock = gp.selectStock();
		URL stockURL = new URL("http://real-chart.finance.yahoo.com/table.csv?s="
		+stock+"&a="+startmonth+"&b="+startday+"&c="+startyear+
		"&d="+endmonth+"&e="+endday+"&f="+endyear+"&g=d&ignore=.csv");
		BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
		char valueseparator = ',';
		char quotechar = '"';
		int linestoskip = 1;
		@SuppressWarnings("resource")
		CSVReader csvReader = new CSVReader(in, valueseparator, quotechar, linestoskip);
		String[] outputread;
		
		//work on csvReader buffered reader one by one with readNext()
		while((outputread = csvReader.readNext()) != null) {
			//Date,Open,High,Low,Close,Volume,Adj Close
			//System.out.println(outputread.length);
			System.out.println(outputread[0]);
		}
	    
		// OR use slurp to store lot in list and iterate over
		//CSVIterator citer = new CSVIterator(csvReader);
		//while(citer.hasNext()){
		//	System.out.println(citer.next()[0]);
		//}
		
		}catch(MalformedURLException e) {
			System.out.println("Malformed URL:" + e.getMessage());
		}catch(IOException e){
			System.out.println(e);
		}
		
	}
}
