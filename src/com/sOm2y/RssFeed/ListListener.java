package com.sOm2y.RssFeed;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



/**
 * Class implements a list listener
 * 
 * @author ITCuties
 *
 */
public class ListListener implements OnItemClickListener {

	// List item's reference
	List<RssItem> listItems;
	// Calling activity reference
	Activity activity;
	
	public ListListener(List<RssItem> aListItems, Activity anActivity) {
		listItems = aListItems;
		activity  = anActivity;
	}
	
	/**
	 * Start a browser with url from the rss item.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		Toast.makeText(activity, listItems.get(pos).getDescription()+"\n"+listItems.get(pos).getPubDate(),
				Toast.LENGTH_LONG).show();
		activity.startActivity(i);
		
	}
	
	
}



