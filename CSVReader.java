package csvreader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;
public class CSVReader {
public static void main(String[] args) throws IOException {
	
	try {
 
//csv file containing data
		String dataFile = "/home/mikedes/Downloads/table.csv";
	//create BufferedReader to read csv file
	//BufferedReader br = new BufferedReader( new FileReader(strFile));
	// for binary reading
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFile)));
		//RandomAccessFile in = new RandomAccessFile(dataFile, "r");
		int version = in.readInt();
		byte type = in.readByte();
		int beginOfData = in.readInt();
		byte[] tempId;
		//while (true) {
			//System.out.println(version + type + beginOfData + tempId);
			System.out.println(version);
			System.out.println(beginOfData);
		//}
			
		//read comma separated file line by line

		} catch(EOFException e) {
			System.out.println(e);
		}
	}
}