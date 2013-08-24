package com.sOm2y.parserXML;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.sOm2y.XML.people.*;
import com.sOm2y.parserXML.*;

public class parserXML_courses {

	public parserXML_courses() {
		// TODO Auto-generated constructor stub
	}

	public static List<Course> parseXML(InputStream inputStream, String encode)
			throws Exception {
		List<Course> list = null;
		Course course = null;

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

		XmlPullParser parser = factory.newPullParser();
		parser.setInput(inputStream, encode);

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				list = new ArrayList<Course>();
				break;
			case XmlPullParser.START_TAG:
				if ("codeField".equals(parser.getName())) {
					course = new Course();
					String codeField = parser.nextText();
					course.setCodeField(codeField);
					
				} else if ("semesterField".equals(parser.getName())) {
					String semesterField = parser.nextText();
					course.setSemesterField(semesterField);
					
				} else if ("titleField".equals(parser.getName())) {
					String titleField = parser.nextText();
					course.setTitleField(titleField);
					
				}
				break;
			case XmlPullParser.END_TAG:
				if ("Course".equals(parser.getName())) {
					list.add(course);
					course = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return list;
	}

}
