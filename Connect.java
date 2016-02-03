package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Connect {
	public static String s = null;
    public static BufferedReader in = null;
    public static PrintWriter printwriter = null;
	public static void main(String[] args) throws UnknownHostException, IOException{
		run();
	}
	
	public static void run() throws IOException, IOException {
		while(true){
		try {
			String localhost = "localhost";
			int port = 1234;
			Socket socket = new Socket(localhost,port);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter printwriter = new PrintWriter(socket.getOutputStream(),true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			if(true){
				System.out.println("Connection successful");				
				try {
					s = in.readLine();
					System.out.println(s);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				for(int i =1 ; i<2;i++) {
					printwriter.print("buy msft 100 3.00");
				}
				System.out.println(s);
			}
			while(true){

			}
	}
}
	
}