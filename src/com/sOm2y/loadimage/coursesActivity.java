package com.sOm2y.loadimage;

import java.io.InputStream;
import java.util.List;

import com.sOm2y.XML.people.Course;
import com.sOm2y.http.http_courses;
import com.sOm2y.parserXML.parserXML_courses;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class coursesActivity extends Activity {
	private TextView course1;
	private String c1 = null;

	public coursesActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.courses);

			course1 = (TextView) this.findViewById(R.id.courses1);

			String path = "http://redsox.tcs.auckland.ac.nz/CSS/CSService.svc/courses";
			InputStream inputStream = http_courses.getCourse(path);
			List<Course> list = null;

			list = parserXML_courses.parseXML(inputStream, "utf-8");
			for (Course course : list) {
				c1 = course.toString();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		if (c1 == null)
			course1.setText("failed");
		}
	}

}
