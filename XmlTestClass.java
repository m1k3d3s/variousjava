package xmltesting;

import java.io.File;
import java.io.Serializable;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stax.*;
import javax.xml.transform.stream.*;

public class XmlTestClass {
	public static void main(String[] args)
	{
		File fXmlFile = new File("exemel.xml");
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			Document doc = parser.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			System.out.println("-------------------");
			NodeList listOf = doc.getElementsByTagName("company");
			
			for(int i = 0; i < listOf.getLength(); i++){
				   Node nNode = listOf.item(i);
				   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
				      Element eElement = (Element) nNode;
		 
				      System.out.println("First Name : " + getTagValue("firstname", eElement));
				      System.out.println("Last Name : " + getTagValue("lastname", eElement));
			              System.out.println("Nick Name : " + getTagValue("nickname", eElement));
				      System.out.println("Salary : " + getTagValue("salary", eElement));
		 
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	
		
	}
	
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
			Node nValue = (Node) nlList.item(0);
			return nValue.getNodeValue();
		  }
}




