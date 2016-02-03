package com.mpd.app;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import wdirsvc.DirWatcher;
import wdirsvc.WatchDirectory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BidAskScrape {
	
	public static void main(String[] args) throws Exception{
		LaunchBidAskScrape lbas = new LaunchBidAskScrape();
		GetProperties gp = new GetProperties();
		Logger LOGGER = Logger.getLogger(BidAskScrape.class.getName());
		DirWatcher dw = new DirWatcher();
		LOGGER.setUseParentHandlers(false);
		Document doc;
		String url = lbas.setUrl();
		URL aURL = new URL(url);
        String ticker = aURL.getQuery();
        String[] stocksymbol = ticker.split("=");
        String symbol = stocksymbol[1];
        doc = Jsoup.connect(url).get();
        String p = "/home/mikedes/historicaldata/csvfiles";
        dirwatcher.init(p);
		dirwatcher.doRounds();
		
		Elements yfnc = doc.getElementsByClass(gp.selectClassName());
		Float p_close;
		Float open;
		String bid;
		String ask;
		Float one_yr_target_est;
		Float beta;
		String earnings_date;
		String day_range;
		String year_range;
		int volume;
		
		try{
			Elements prev_close = yfnc.eq(0);
			String prev_close_text = prev_close.text();
			p_close = Float.parseFloat(prev_close_text);
			System.out.println(p_close);
		} catch(NumberFormatException e) {
			System.out.println("prev_close not available.");
			p_close = (float) 0.00;
		}
		
		try{
			Elements e_open = yfnc.eq(1);
			String open_text = e_open.text();
			open = Float.parseFloat(open_text);
			System.out.println(open);
		} catch(NumberFormatException e) {
			System.out.println("Open not available.");
			open = (float) 0.00;
		}
			
		
		Elements e_bid = yfnc.eq(2);
		bid = e_bid.text();
		if(bid != null && !bid.isEmpty()){
			System.out.println(bid);
		} else {
			System.out.println("Bid data not available.");
		}
		
		Elements e_ask = yfnc.eq(3);
		ask = e_ask.text();
		if(ask != null && !ask.isEmpty()){	
			System.out.println(ask);
		} else {
			System.out.println("Ask not available.");
		}
		
		try{	
			Elements e_oyte = yfnc.eq(4);
			String oyte_text = e_oyte.text();
			one_yr_target_est = Float.parseFloat(oyte_text);
			System.out.println(one_yr_target_est);
		} catch(NumberFormatException e){
			System.out.println("One year target data not available.");
			one_yr_target_est = (float) 0.00;
		}
			
		try{
			Elements e_beta = yfnc.eq(5);
			String beta_text = e_beta.text();
			beta = Float.parseFloat(beta_text);
			System.out.println(beta);
		} catch(NumberFormatException e){
			System.out.println("Beta not available.");
			beta = (float) 0.00;
		}
			
		
		Elements e_date = yfnc.eq(6);
		earnings_date = e_date.text();
		if(earnings_date != null && !earnings_date.isEmpty()){
			System.out.println(earnings_date);
		} else {
			System.out.println("Earnings data not available.");
		}
		
		Elements e_range = yfnc.eq(7);
		day_range = e_range.text();
		if(day_range != null && !day_range.isEmpty()){
			System.out.println(day_range);
		} else {
			System.out.println("Day range data not available.");
		}
		
		Elements e_year_range = yfnc.eq(8);
		year_range = e_year_range.text();
		if(year_range != null && !year_range.isEmpty()){
			System.out.println(year_range);
		}else{
			System.out.println("Year range data not available.");
		}
		
		try{
			Elements e_volume = yfnc.eq(9);
			String vol_text = e_volume.text();
			volume = Integer.parseInt(vol_text.replaceAll(",", ""));
			System.out.println(volume);
		} catch(NumberFormatException e){
			System.out.println("Volume not available.");
			volume = 0;
		}
		
		String[] credentials = {"jdbc:mysql://localhost/feedback","sqluser","sqlusermd"};
		Connection connect = null;
		PreparedStatement preparedStatement = null;
		
		java.util.Date date = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
		String text_date = df.format(date);
		
		try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connect = DriverManager
		          .getConnection(credentials[0],credentials[1],credentials[2]);
		preparedStatement = connect.prepareStatement("insert into feedback.parsehtmldata values (?,?,?,?,?,?,?,?,?,?,?,?)");
		preparedStatement.setFloat(1, p_close);
		preparedStatement.setFloat(2, open);
		preparedStatement.setString(3, bid);
		preparedStatement.setString(4, ask);
		preparedStatement.setFloat(5, one_yr_target_est);
		preparedStatement.setFloat(6, beta);
		preparedStatement.setString(7, earnings_date);
		preparedStatement.setString(8, day_range);
		preparedStatement.setString(9, year_range);
		preparedStatement.setInt(10, volume);
		preparedStatement.setString(11, symbol);
		preparedStatement.setString(12, text_date);
		preparedStatement.executeUpdate();
		String update = preparedStatement.toString();
		FileHandler fh;
		fh = new FileHandler(gp.selectLogFile());
		LOGGER.addHandler(fh);
		LOGGER.info(update);
		
		}catch(Exception e) {
	    	LOGGER.severe("Could not update database. " + e);
	    	//throw e;
		}
	}
}
	

