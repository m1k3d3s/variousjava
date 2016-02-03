package com.mpd.app;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HistoricalPrice {
	String date,stock;
	float open,high,low,close,adjclose;
	int volume;
	Date timestamp;
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public float getOpen() {
		return open;
	}
	public void setOpen(float open) {
		this.open = (float)open;
	}
	public float getHigh() {
		return high;
	}
	public void setHigh(float high) {
		this.high = (float)high;
	}
	public float getLow() {
		return low;
	}
	public void setLow(float low) {
		this.low = (float)low;
	}
	public float getClose() {
		return close;
	}
	public void setClose(float d) {
		this.close = (float) d;
	}
	public float getAdjclose() {
		return adjclose;
	}
	public void setAdjclose(float d) {
		this.adjclose = (float) d;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
