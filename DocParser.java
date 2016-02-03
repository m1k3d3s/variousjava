import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.text.html.parser.DocumentParser;
import javax.swing.text.html.parser.Parser;
public class DocParser {
	public static void main(String[] args) throws IOException{
		DocumentParser docp = new DocumentParser(null);
		Reader reader = new BufferedReader(null);
		docp.parse(reader);
	}
	
}
