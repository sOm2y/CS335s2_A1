package com.sOm2y.RssFeed;

import java.util.List;

import com.sOm2y.loadimage.peopleActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

public class ListLongClickListener implements OnItemLongClickListener {
	List<RssItem> listItems;
	// Calling activity reference
	Activity activity;
	public ListLongClickListener(List<RssItem> aListItems, Activity anActivity) {
		listItems = aListItems;
		activity  = anActivity;
	}
	

	@Override
	public boolean onItemLongClick(AdapterView<?>parent, View view, int pos, long id) {
		// TODO Auto-generated method stub
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(listItems.get(pos).getLink()));
		
		
		activity.startActivity(i);
		return false;
	}

}
