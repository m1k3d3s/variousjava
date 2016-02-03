import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class StatusReader implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		String s = null;
		int s_port_to_int = Integer.parseInt(StatusMonitor.s_port);
		try {
			Socket socket = new Socket(StatusMonitor.s_host, s_port_to_int);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e1) {
			System.out.println("Unable to connecto to host:port");
		} catch (IOException e1){
			System.out.println("Unable to read data");
		}
		
		while(true){
			try {
				s = in.readLine();
			} catch (IOException e1){
				System.out.println("Unable to read data");
			}
			if((s != null) && (s.length() != 0)) {
				System.out.println(s);
				StringTokenizer st = new StringTokenizer(s,"|", false);
			}
		}
	}

}
