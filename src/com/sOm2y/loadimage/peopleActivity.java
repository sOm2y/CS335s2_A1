package com.sOm2y.loadimage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.sOm2y.XML.people.Course;
import com.sOm2y.XML.people.Person;
import com.sOm2y.http.http_people;
import com.sOm2y.parserXML.parserXML_courses;
import com.sOm2y.parserXML.parserXML_people;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.util.org.apache.commons.codec.binary.Base64;
import android.R.drawable;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class peopleActivity extends Activity {

	private ListView mListView;
	
	private List<String> stuffList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.people);

			mListView = (ListView) findViewById(R.id.listview);
		
			String path = "http://redsox.tcs.auckland.ac.nz/CSS/CSService.svc/people";

			stuffList = new RetreiveFeedTaskOfStuff().execute(path).get();
			System.out.println(stuffList.size());

			String[] mFrom = new String[] { "img", "title1", "title2", "time" };
			int[] mTo = new int[] { R.id.img, R.id.title1, R.id.title2,
					R.id.time };

			List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
			Map<String, Object> mMap = null;

			for (int i = 0; i < stuffList.size(); i++) {

				VCard vcardList = new RetreiveFeedTaskOfVCard().execute(
						stuffList.get(i)).get();

				System.out.println(i);
				
					// Drawable d = new
					// BitmapDrawable(getResources(),decodedByte);
					mMap = new HashMap<String, Object>();

					mMap.put("img", R.drawable.tower);
					mMap.put("title1", vcardList.getFormattedName().getValue());
					mMap.put("title2", vcardList.getEmails().get(0).getValue());
					mMap.put("time", vcardList.getTelephoneNumbers().get(0)
							.getText());

					mList.add(mMap);
				

			}
			// ¥¥Ω®  ≈‰∆˜
			SimpleAdapter mAdapter = new SimpleAdapter(this, mList,
					R.layout.vcard, mFrom, mTo) {
				@Override
				public View getView(final int position, View convertView,
						ViewGroup parent) {
					View view = super.getView(position, convertView, parent);

					@SuppressWarnings("unchecked")
					final HashMap<String, Object> map = (HashMap<String, Object>) this
							.getItem(position);
					ImageView imageView = (ImageView) view
							.findViewById(R.id.img);
					TextView tv1 = (TextView) view.findViewById(R.id.title1);
					TextView tv2 = (TextView) view.findViewById(R.id.title2);
					TextView tv3 = (TextView) view.findViewById(R.id.time);
					try {
						if (map.get("img") == null) {
							throw new IOException();
						}
						VCard vcardList = new RetreiveFeedTaskOfVCard()
								.execute(stuffList.get(position)).get();
						byte[] btImage = vcardList.getPhotos().get(0).getData();

						Bitmap decodedByte = BitmapFactory.decodeByteArray(
								btImage, 0, btImage.length);
						tv1.setText(vcardList.getFormattedName().getValue());
						tv2.setText(vcardList.getEmails().get(0).getValue());
						tv3.setText(vcardList.getTelephoneNumbers().get(0)
								.getText());
						imageView.setImageBitmap(decodedByte);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return view;
				}
			};

			mListView.setAdapter(mAdapter);

			// String path =
			// "http://redsox.tcs.auckland.ac.nz/CSS/CSService.svc/people";
			//
			// List<String> stuffList = new RetreiveFeedTaskOfStuff().execute(
			// path, null, null).get();
			//
			// List<VCard> vcardList = new RetreiveFeedTaskOfVCard().execute(
			// stuffList, null, null).get();
			// adapter = new ArrayAdapter<VCard>(peopleActivity.this,
			// android.R.layout.simple_list_item_1, vcardList);
			// lv.setAdapter(adapter);
			// vc1 += vcardList.getEmails().get(i).getValue() + "\n";
			// vc1 += vcardList.getTelephoneNumbers().get(0).getText();
			//
			// byte[] btImage = vcardList.getPhotos().get(0).getData();
			// Bitmap decodedByte = BitmapFactory.decodeByteArray(btImage, 0,
			// btImage.length);
			//
			// iv1.setImageBitmap(decodedByte);
			// stuff1.setText(vc1);

			// stuff1.setMovementMethod(LinkMovementMethod.getInstance());
			// stuff1.setMovementMethod(new ScrollingMovementMethod());

		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}

class RetreiveFeedTaskOfStuff extends AsyncTask<String, Void, List<String>> {
	private Exception exception;
	public static List<String> url = new ArrayList<String>();

	@Override
	protected List<String> doInBackground(String... params) {
		// TODO Auto-generated method stub

		List<Person> list = null;

		try {
			InputStream inputStream = http_people.getVCard(params[0]);

			list = parserXML_people.parseXML(inputStream, "utf-8");

			final String vcard = "http://www.cs.auckland.ac.nz/our_staff/vcard.php?upi=";

			for (int i = 0; i < list.size(); i++) {

				String s1 = vcard + list.get(i).toString();
				System.out.println(s1);
				url.add(s1);
				System.out.print(url.size());
			}

		} catch (Exception e) {
			this.exception = e;
			return null;
		}
		return url;

	}

	protected void onPostExecute(InputStream feed) {
		// TODO: check this.exception
		// TODO: do something with the feed
	}

}

class RetreiveFeedTaskOfVCard extends AsyncTask<String, Void, VCard> {

	private Exception exception;
	private VCard vc;

	@Override
	protected VCard doInBackground(String... params) {
		// TODO Auto-generated method stub

		try {

			InputStream is = http_people.getVCard(params[0]);
			InputStreamReader isReader = new InputStreamReader(is);

			if (isReader.ready()) {
				vc = Ezvcard.parse(isReader).first();

			}

		} catch (Exception e) {
			this.exception = e;
			return null;
		}
		return vc;
	}

}
