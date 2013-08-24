package com.sOm2y.parserXML;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.sOm2y.XML.people.*;


public class parserXML_people {

	public parserXML_people() {
		// TODO Auto-generated constructor stub
	}

	
	public static List<Person> parseXML(InputStream inputStream, String encode)
			throws Exception {
		List<Person> list = null;
		Person person = null;
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		
		XmlPullParser parser = factory.newPullParser();
		parser.setInput(inputStream, encode);
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				list = new ArrayList<Person>();
				break;
			case XmlPullParser.START_TAG:
				if ("uPIField".equals(parser.getName())) {
					person = new Person();
					
					String uPIField = parser.nextText();
					person.setuPIField(uPIField);
				} 
				break;
			case XmlPullParser.END_TAG:
				if ("Person".equals(parser.getName())) {
					list.add(person);
					person = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return list;
	}

}
