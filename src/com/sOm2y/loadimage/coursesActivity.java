package com.sOm2y.loadimage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sOm2y.XML.people.Course;

import com.sOm2y.parserXML.parserXML_courses;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class coursesActivity extends Activity {

	private TextView course01;
	private String c1 = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.courses);

			course01 = (TextView) this.findViewById(R.id.courses1);
			// course01.setText("test start!");
			String path = "http://redsox.tcs.auckland.ac.nz/CSS/CSService.svc/courses";

			List<Course> courseList = new RetreiveFeedTask().execute(path,
					null, null).get();

			for (Course course : courseList) {
				c1 += course.toString() + "\n";

			}
			if (c1 == null)
				course01.setText("c1 is null");

			course01.setText(c1);
			course01.setMovementMethod(new ScrollingMovementMethod());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class RetreiveFeedTask extends AsyncTask<String, Void, List<Course>> {
	private Exception exception;

	@Override
	protected List<Course> doInBackground(String... params) {
		// TODO Auto-generated method stub
		URLConnection conn = null;
		InputStream inputStream = null;
		List<Course> list = null;

		try {
			URL url = new URL(params[0]);
			conn = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}

			list = parserXML_courses.parseXML(inputStream, "utf-8");

		} catch (Exception e) {
			this.exception = e;
			return null;
		}
		return list;

	}

	protected void onPostExecute(InputStream feed) {
		// TODO: check this.exception
		// TODO: do something with the feed
	}

}
