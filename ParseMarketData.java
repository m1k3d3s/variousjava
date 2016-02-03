package generics;


public class ParseMarketData{
	// "Date","Open","High","Low","Close","Volume","OI"
	// load and parse market data text file
	//11/01/1985,115.48,116.78,115.48,116.28,900900,0
	private String date;
	private double open;
	private double high;
	private double low;
	private double close;
	private int volume;
	private int oi;
	
	ParseMarketData(String d, double d_op_price, double d_high, double d_low, double d_close, int
					d_vol, int d_oi) {
		d = date;
		d_op_price = open;
		d_high = high;
		d_low = low;
		d_close = close;
		d_vol = volume;
		d_oi = oi;
	}
	
	public String toString() {
		return date + " " + open + " " + high + " " + low + " " + close + " " +
				volume + " " + oi + "\n";
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getOi() {
		return oi;
	}

	public void setOi(int oi) {
		this.oi = oi;
	}
	
	
}
