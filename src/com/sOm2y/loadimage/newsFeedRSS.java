package com.sOm2y.loadimage;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sOm2y.RssFeed.ListListener;
import com.sOm2y.RssFeed.ListLongClickListener;
import com.sOm2y.RssFeed.RssItem;
import com.sOm2y.RssFeed.RssReader;

/**
 * Main application activity.
 * 
 * Update: Downloading RSS data in an async task
 * 
 * @author ITCuties
 * 
 */
public class newsFeedRSS extends Activity {

	// A reference to the local object
	private newsFeedRSS local;

	/**
	 * This method creates main application view
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set view
		setContentView(R.layout.news);

		// Set reference to this activity
		local = this;

		GetRSSDataTask2 task = new GetRSSDataTask2();

		// Start download RSS task
		task.execute("http://www.cs.auckland.ac.nz/uoa/home/template/news_feed.rss?category=science_cs_news");

		// Debug the thread name
		Log.d("ITCRssReader", Thread.currentThread().getName());

	}

	private class GetRSSDataTask2 extends
			AsyncTask<String, Void, List<RssItem>> {
		@Override
		protected List<RssItem> doInBackground(String... urls) {

			// Debug the task thread name
			Log.d("ITCRssReader", Thread.currentThread().getName());

			try {
				// Create RSS reader
				RssReader rssReader = new RssReader(urls[0]);

				// Parse RSS, get items
				return rssReader.getItems();

			} catch (Exception e) {
				Log.e("ITCRssReader", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<RssItem> result) {

			// Get a ListView from main view
			ListView itcItems = (ListView) findViewById(R.id.listMainView2);

			// Create a list adapter
			ArrayAdapter<RssItem> adapter = new ArrayAdapter<RssItem>(local,
					android.R.layout.simple_list_item_1, result);
			// Set list adapter for the ListView
			itcItems.setAdapter(adapter);

			// Set list view item click listener
			itcItems.setOnItemClickListener(new ListListener(result, local));

			itcItems.setOnItemLongClickListener(new ListLongClickListener(
					result, local));
		}
	}

}