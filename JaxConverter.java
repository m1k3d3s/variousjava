package com.mpd.app;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JaxConverter {
	
	
	public static void main(String[] args){
		Date date = new Date();
		HistoricalPrice hp = new HistoricalPrice();
		hp.setAdjclose((float) 2.40);
		hp.setClose((float)2.38);
		hp.setDate(date.toString());
		hp.setHigh((float)3.01);
		hp.setLow((float)2.14);
		hp.setOpen((float)2.30);
		hp.setStock("TEST");
		hp.setVolume(123456);
		
		try {
			File file = new File("/home/mikedes/testfile.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(HistoricalPrice.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			jaxbMarshaller.marshal(hp, file);
			jaxbMarshaller.marshal(hp, System.out);
			
		} catch(JAXBException e) {
			e.printStackTrace();
			
			
		}
	}
}
