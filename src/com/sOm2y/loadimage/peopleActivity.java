package com.sOm2y.loadimage;

import java.io.ByteArrayInputStream;
import java.io.File;
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
	private ArrayAdapter<VCard> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.people);

			mListView = (ListView) findViewById(R.id.listview);

			String path = "http://redsox.tcs.auckland.ac.nz/CSS/CSService.svc/people";

			List<String> stuffList = new RetreiveFeedTaskOfStuff().execute(path)
					.get();
			
				// 下面是数据映射关系,mFrom和mTo按顺序一一对应
				String[] mFrom = new String[] { "img", "title1", "title2",
						"time" };
				int[] mTo = new int[] { R.id.img, R.id.title1, R.id.title2,
						R.id.time };
				// 获取数据,这里随便加了10条数据,实际开发中可能需要从数据库或网络读取
				List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
				Map<String, Object> mMap = null;
				for (int i = 0; i <=2; i++) {
					VCard vcardList = new RetreiveFeedTaskOfVCard().execute(
							stuffList.get(i)).get();
					byte[] btImage = vcardList.getPhotos().get(0).getData();
					System.out.println(btImage.toString());
					Bitmap decodedByte = BitmapFactory.decodeByteArray(btImage, 0,
					btImage.length);
					//Drawable d = new BitmapDrawable(getResources(),decodedByte);
					mMap = new HashMap<String, Object>();
					mMap.put("img", R.drawable.tower);
					mMap.put("title1", vcardList.getFormattedName().getValue());
					mMap.put("title2", vcardList.getEmails().get(0).getValue());
					mMap.put("time", vcardList.getTelephoneNumbers().get(0).getText());
				
					mList.add(mMap);
				}
				// 创建适配器
				SimpleAdapter mAdapter = new SimpleAdapter(this, mList,
						R.layout.vcard, mFrom, mTo);
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
	private VCard vc ;

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
