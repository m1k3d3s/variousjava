package com.mpd.app;

public class CsvDatabaseLoader {
	public static void main(String[] args) throws Exception{
		GetProperties gp = new GetProperties();
		String p = gp.selectWatchDirectory();
		DirWatcher dirwatcher = new DirWatcher();
		dirwatcher.init(p);
		dirwatcher.doRounds();
		ChartDataLoader.launch(args);
	}
}
