import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class StatusMonitor {
	
	public static String s_host;
	public static String s_port;
	
	public enum MessageTags {
		TS,MACH,INST,MSG_TYPE,STATE,DROPS,SESSION
	}
	
	public static void main(String[] args) throws IOException{
		if(args.length == 2 && Integer.parseInt(args[1]) <= 65535) {
			StatusFrame sf = new StatusFrame();
			sf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			sf.setVisible(true);
			System.out.println("Connecting to: " + args[0] + " on port " + args[1]);
			StatusMonitor.s_host = args[0];
			StatusMonitor.s_port = args[1];
			System.out.println("This is trying to work");
		} else if(args.length > 2) {
			System.out.println("Only <host> <port> is needed");
		} else if(args.length < 2) {
			System.out.println("Need <host> <port> to continue");
		} else if(Integer.parseInt(args[1]) > 65535) {
			System.out.println("Invalid port range");
		}
	}
}
