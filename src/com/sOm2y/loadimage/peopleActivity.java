package com.sOm2y.loadimage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.sOm2y.XML.people.Course;
import com.sOm2y.XML.people.Person;
import com.sOm2y.http.http_people;
import com.sOm2y.parserXML.parserXML_courses;
import com.sOm2y.parserXML.parserXML_people;



import ezvcard.Ezvcard;
import ezvcard.VCard;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

public class peopleActivity extends Activity {
	private String s1 = "";
	private String vc1 = "";
	private TextView stuff1;
	private ImageView iv1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.people);
			stuff1 = (TextView) this.findViewById(R.id.stuff1);
			iv1 = (ImageView) this.findViewById(R.id.stuffImage1);

//			String path = "http://redsox.tcs.auckland.ac.nz/CSS/CSService.svc/people";
//			String vcard = "http://www.cs.auckland.ac.nz/our_staff/vcard.php?upi=";
//			List<Person> stuffList = new RetreiveFeedTaskOfStuff().execute(
//					path, null, null).get();
//
//			for (int i = 0; i < stuffList.size(); i++) {
//				s1 += vcard + stuffList.get(i).toString();
//
//			}
			VCard vcardList = new RetreiveFeedTaskOfVCard()
					.execute(
							"http://www.cs.auckland.ac.nz/our_staff/vcard.php?upi=rnic033")
					.get();

			vc1 += vcardList.getEmails().get(0).getValue() + "\n";
			vc1 += vcardList.getTelephoneNumbers().get(0).getText();
			
			byte[] btImage=vcardList.getPhotos().get(0).getData();
			if(btImage==null)
				stuff1.setText("vcard photo is null.");
			Bitmap bMap=new RetreiveFeedTaskOfVCardPhoto().execute(
					btImage).get();
			if(bMap==null)
				stuff1.setText("bit map is null.");
			iv1.setImageBitmap(bMap);
			//stuff1.setText(vc1);
			//stuff1.setMovementMethod(LinkMovementMethod.getInstance());
			// stuff1.setMovementMethod(new ScrollingMovementMethod());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}

class RetreiveFeedTaskOfStuff extends AsyncTask<String, Void, List<Person>> {
	private Exception exception;

	@Override
	protected List<Person> doInBackground(String... params) {
		// TODO Auto-generated method stub

		List<Person> list = null;

		try {
			InputStream inputStream = http_people.getVCard(params[0]);

			list = parserXML_people.parseXML(inputStream, "utf-8");

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

class RetreiveFeedTaskOfVCard extends AsyncTask<String, Void, VCard> {
	private Exception exception;

	@Override
	protected VCard doInBackground(String... params) {
		// TODO Auto-generated method stub
		URLConnection conn = null;
		InputStream inputStream = null;
		VCard listVcard = null;

		try {
			InputStream is = http_people.getVCard(params[0]);
			InputStreamReader isReader = new InputStreamReader(is);

			if (isReader.ready()) {
				listVcard = Ezvcard.parse(isReader).first();

			}

		} catch (Exception e) {
			this.exception = e;
			return null;
		}
		return listVcard;

	}

	protected void onPostExecute(InputStream feed) {
		// TODO: check this.exception
		// TODO: do something with the feed
	}

}

class RetreiveFeedTaskOfVCardPhoto extends AsyncTask<byte[], Void, Bitmap> {

	@Override
	protected Bitmap doInBackground(byte[]... params) {
		Bitmap decodedByte = null;
		
		try {
			// TODO Auto-generated method stub
			byte[] decodedString = Base64.decode(params[0], Base64.DEFAULT);
			 decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		} catch (Exception e) {
		}
		return decodedByte;

	}

}
