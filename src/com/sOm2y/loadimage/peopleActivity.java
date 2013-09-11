package com.sOm2y.loadimage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sOm2y.XML.people.Person;
import com.sOm2y.XML.people.vcardListener;
import com.sOm2y.http.http_people;
import com.sOm2y.parserXML.parserXML_people;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;

public class peopleActivity extends Activity {

	private ListView mListView;

	private List<String> stuffList;
	private ImageView image;
	List<Map<String, Object>> mList;
	List<VCard> stuff = new ArrayList<VCard>();
	Map<String, Object> mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.people);

			mListView = (ListView) findViewById(R.id.listview);

			getPeople();
			// mListView.setOnItemLongClickListener(new vcardListener(stuff,
			// this));
			mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String name = stuff.get(arg2).getFormattedName().getValue();

					String phone = stuff.get(arg2).getTelephoneNumbers().get(0)
							.getText();
					String result1 = phone.replace("x", ",");

					String mail = stuff.get(arg2).getEmails().get(0).getValue();

					byte[] btImage = stuff.get(arg2).getPhotos().get(0)
							.getData();
					Bitmap picture = BitmapFactory.decodeByteArray(btImage, 0,
							btImage.length);
					// TODO Auto-generated method stub
					insert(name, result1, mail, picture);
					Toast.makeText(peopleActivity.this,
							"Add contacter successfully!", Toast.LENGTH_LONG)
							.show();
					return false;
				}
			});
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mList = new ArrayList<Map<String, Object>>();
		mMap = null;
		stuffList = null;
		stuff = null;
	}

	public boolean insert(String given_name, String mobile_number,
			String work_email, Bitmap pic) {
		try {
			ContentValues values = new ContentValues();

			// 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
			Uri rawContactUri = getContentResolver().insert(
					RawContacts.CONTENT_URI, values);
			long rawContactId = ContentUris.parseId(rawContactUri);

			// 向data表插入姓名数据
			if (given_name != "") {
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
				values.put(StructuredName.GIVEN_NAME, given_name);
				getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
						values);
			}

			// 向data表插入电话数据
			if (mobile_number != "") {
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				values.put(Phone.NUMBER, mobile_number);
				values.put(Phone.TYPE, Phone.TYPE_MOBILE);
				getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
						values);
			}

			// 向data表插入Email数据
			if (work_email != "") {
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
				values.put(Email.DATA, work_email);
				values.put(Email.TYPE, Email.TYPE_WORK);
				getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
						values);
			}

			// 向data表插入头像数据

			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			// 将Bitmap压缩成PNG编码，质量为100%存储
			pic.compress(Bitmap.CompressFormat.PNG, 100, os);
			byte[] avatar = os.toByteArray();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
			values.put(Photo.PHOTO, avatar);
			getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
					values);
		}

		catch (Exception e) {
			return false;
		}
		return true;
	}

	public void getPeople() {
		try {

			String path = "http://redsox.tcs.auckland.ac.nz/CSS/CSService.svc/people";

			stuffList = new RetreiveFeedTaskOfStuff().execute(path).get();
			System.out.println(stuffList.size());

			String[] mFrom = new String[] { "img", "title1", "title2", "time" };
			int[] mTo = new int[] { R.id.img, R.id.title1, R.id.title2,
					R.id.time };

			mList = new ArrayList<Map<String, Object>>();
			mMap = null;

			for (int i = 0; i < stuffList.size(); i++) {

				VCard vcardList = new RetreiveFeedTaskOfVCard().execute(
						stuffList.get(i)).get();

				System.out.println(i);

				// Drawable d = new
				// BitmapDrawable(getResources(),decodedByte);
				mMap = new HashMap<String, Object>();
				String tlp = vcardList.getTelephoneNumbers().get(0).getText()
						.toString();
				String result1 = tlp.replace("x", ",");
				String result2 = result1.replaceAll("[(|)]", "");
				System.out.println(result2);
				mMap.put("img", R.drawable.tower);
				mMap.put("title1", vcardList.getFormattedName().getValue());
				mMap.put("title2", vcardList.getEmails().get(0).getValue());
				mMap.put("time", result2);
				stuff.add(vcardList);
				mList.add(mMap);

			}

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
						String tlp = vcardList.getTelephoneNumbers().get(0)
								.getText().toString();
						String result1 = tlp.replace("x", ",");
						String result2 = result1.replaceAll("[(|)]", "");
						System.out.println(result2);
						tv1.setText(vcardList.getFormattedName().getValue());
						tv2.setText(vcardList.getEmails().get(0).getValue());
						tv3.setText(result2);
						imageView.setImageBitmap(decodedByte);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return view;
				}
			};

			mListView.setAdapter(mAdapter);

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
