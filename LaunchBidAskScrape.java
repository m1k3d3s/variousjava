package com.mpd.app;

public class LaunchBidAskScrape {
	static String stock;
	static String url;
	public static void main(String[] args) throws Exception {
		GetProperties gp = new GetProperties();
		GetStockData gsd = new GetStockData();
		stock = gp.selectStock();
		url = gp.selectUrl();
		System.out.println(gsd.row);
		BidAskScrape.main(null);
	}
	
	String setUrl() {
		String finurl = url + stock;
		return finurl;
		
	}
}
