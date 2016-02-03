package com.mpd.app;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import static java.nio.file.StandardWatchEventKinds.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.logging.FileHandler;

public class DirWatcher {
	
	GetProperties gp = new GetProperties();
    private Path path = null;
    private WatchService watchService = null;
    private String csvfile = null;
    public void init(String p) {
        path = Paths.get(p);
        try {
            watchService = FileSystems.getDefault().newWatchService();
            //path.register(watchService, ENTRY_CREATE,ENTRY_DELETE,
            	//	ENTRY_MODIFY, OVERFLOW);
            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);
        } catch (IOException e) {
			System.out.println("Store directory may be empty");
            System.out.println("IOException" + e.getMessage());
        }
    }

    public void doRounds() throws SQLException {
        WatchKey key = null;
        while(true) {
            try {
                    key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()){
                        Kind<?> kind = event.kind();
                        System.out.println("Event on " + event.context().toString() + " is " + kind);
                        csvfile = event.context().toString();
                        //System.out.println(csvfile);
                        doDataLoad();
                    }
            } catch (InterruptedException e) {
                System.out.println("InterruptedException: " + e.getMessage());
            }
            boolean reset = key.reset();
            if(!reset)
                break;
        }
    }
    
    public void doDataLoad() throws SQLException {
		Connection connect = null;
		String sql = "LOAD DATA LOCAL INFILE '/home/mikedes/historicaldata/csvfiles/"+csvfile+ "' INTO TABLE historicalprices FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 LINES";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection(gp.selectJdbcDriver(),gp.selectMysqlUser(),gp.selectMysqlPasswd());
			PreparedStatement pst = (PreparedStatement) connect.prepareStatement(sql);
			pst.execute();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch(InstantiationException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			e.printStackTrace();
			
		}
    }
}
