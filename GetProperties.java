package com.mpd.app;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;

public class GetProperties {
	Properties prop = new Properties();
	String stock = null;
	String url = null;
	String logfile = null;
	String classname = null;
	String mysqldriver = null;
	String mysqluser = null;
	String mysqlpasswd = null;
	String myjdbccxn = null;
	String mywatchdir = null;
	String mychartstock=null;
	String[] startdate=null;
	String[] enddate=null;
	boolean dbsubmit;
	String quotechar,valueseparator;
	int linestoskip;
	
	{
		try{
			prop.load(new FileInputStream("dbloader.config"));
		}catch(IOException e){
			System.out.println(e);
			System.out.println("Unable to continue.");
		}
		
		for(String key : prop.stringPropertyNames()) {
			String value = prop.getProperty(key);
			if(key.matches("stock") && value != null) {
				stock = value;
			}else if(key.matches("url") && value != null) {
				url = value;
			}else if(key.matches("logfile") && value != null){
				logfile=value;
			}else if(key.matches("classname") && value != null){
				classname=value;
			}else if(key.matches("mysqldriver") && value != null){
				mysqldriver=value;
			}else if(key.matches("mysqluser") && value != null){
				mysqluser=value;
			}else if(key.matches("mysqlpasswd") && value != null){
				mysqlpasswd=value;
			}else if(key.matches("myjdbccxn") && value != null){
				myjdbccxn=value;
			}else if(key.matches("mywatchdir") && value != null){
				mywatchdir=value;
			}else if(key.matches("stock") && value != null) {
				mychartstock=value;
			}else if(key.matches("startdate") && value != null) {
				startdate = value.split("/");
			}else if(key.matches("enddate") && value != null) {
				enddate = value.split("/");
			}else if(key.matches("dbsubmit") && value != null){
				if(value == "true"){
					dbsubmit = true;
				}else{
					dbsubmit = false;
				}
			}else if(key.matches("quotechar") && value != null){
				valueseparator = ",";
			}else if(key.matches("valueseparator") && value != null){
				valueseparator=value;
			}else if(key.matches("linestoskip") && value != null){
				linestoskip=Integer.valueOf(value);
			}else {
				System.out.println("Unknown key/value pair. Skipping."+key+" "+value);
				System.out.println(key + " " + value);
			}
		}
	}
	
	public String selectStock() {
		return stock;
	}
	public String selectUrl(){
		return url;
	}
	public String selectLogFile(){
		return logfile;
	}
	public String selectClassName(){
		return classname;
	}
	public String selectMysqlDriver(){
		return mysqldriver;
	}
	public String selectMysqlUser(){
		return mysqluser;
	}
	public String selectMysqlPasswd(){
		return mysqlpasswd;
	}
	public String selectJdbcDriver() {
		return myjdbccxn;
	}
	public String selectWatchDirectory() {
		return mywatchdir;
	}
	public String selectMyChartStock() {
		return mychartstock;
	}
	public int[] selectStartDate() {	
		int[] startdateint = new int[startdate.length];
		for(int i = 0;i < startdate.length; i++){
			startdateint[i] = Integer.parseInt(startdate[i]);
		}
		return startdateint;
	}
	public int[] selectEndDate() {
		int[] enddateint = new int[enddate.length];
		for(int i = 0;i < enddate.length;i++){
			enddateint[i]=Integer.parseInt(enddate[i]);
		}
		return enddateint;
	}
	public boolean selectDbSubmit() {
		return dbsubmit;
	}
	
}
