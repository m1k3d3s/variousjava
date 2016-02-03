package generics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GenericFrame extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String args[]) throws InterruptedException{
		(new Thread(new GenericFrame())).start();
		int rand_int = 5;
		System.out.println(rand_int);
		System.out.println(getRandomInteger());
		System.out.print(getThreadLocalRandomInteger()); 
	}
	
	public static int getThreadLocalRandomInteger() {
		return ThreadLocalRandom.current().nextInt();
	}
	
	public static int getRandomInteger() {
		final Random random = new Random();
		return random.nextInt(500);
	}
	
	public GenericFrame() {
		JPanel jp = new JPanel();
		jp.setVisible(true);
		JTextArea ta = new JTextArea();
		
		BufferedReader reader = null;
		StringBuffer buffer = new StringBuffer( 1024 );
		
		File file = new File("/home/mikedes/marketdata.txt");		
		
		try {
		    reader = new BufferedReader ( new FileReader( file ) );
		    String line;
		    String marketTokens[];
		    buffer.append("Date" + "\t" + "Open" + "\t" + "High" + "\t" + "Low" + "\t" +
		        			"Close" + "\t" + "Volume" + "\t" + "IO" + "\n" + "\n");
		    while( (line = reader.readLine()) != null ) {
		        marketTokens = line.split(",");
		        	//11/01/1985,115.48,116.78,115.48,116.28,900900,0
		        	//"Date","Open","High","Low","Close","Volume","OI"
		        	String date = marketTokens[0];
		        	double open = Double.parseDouble(marketTokens[1].toString());
		        	double high = Double.parseDouble(marketTokens[2].toString());
		        	double low = Double.parseDouble(marketTokens[3].toString());
		        	double close = Double.parseDouble(marketTokens[4].toString());
		        	int volume = Integer.parseInt(marketTokens[5].toString());
		        	int io = Integer.parseInt(marketTokens[6].toString());
		        
		        	buffer.append(date + "\t" + open + "\t" + high + "\t" + low + "\t" +
		        			close + "\t" + volume + "\t" + io + "\n");
		        	buffer.append("\n");
		        }
		} catch (IOException e) {
		    System.out.println("No input file.");
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		        System.out.println("Could not close file. Rogue process possible.");
		    }
		}

		    ta.setText( buffer.toString() );
		    ta.setEditable(false);
		
		JScrollPane sp = new JScrollPane(ta);
		add(sp);
		sp.setSize(300, 300);
		this.setSize(310, 310);
		this.setVisible(true);
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

}
